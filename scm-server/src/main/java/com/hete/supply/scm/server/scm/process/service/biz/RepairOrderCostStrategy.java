package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.builder.BatchCodeCostBuilder;
import com.hete.supply.scm.server.scm.cost.handler.UpdMonWightAvgHandler;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeItemBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAvgPriceBo;
import com.hete.supply.scm.server.scm.enums.CostCalculationStrategyType;
import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import com.hete.supply.scm.server.scm.ibfs.service.base.BatchCodeCostBaseService;
import com.hete.supply.scm.server.scm.process.builder.MaterialBuilder;
import com.hete.supply.scm.server.scm.process.builder.ProcOrderMaterialBuilder;
import com.hete.supply.scm.server.scm.process.dao.BatchCodeCostDao;
import com.hete.supply.scm.server.scm.process.dao.RepairOrderItemDao;
import com.hete.supply.scm.server.scm.process.dao.RepairOrderResultDao;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.enums.CostType;
import com.hete.supply.scm.server.scm.process.handler.BatchCodePriceUpdateHandler;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculationStrategy;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderMaterialBaseService;
import com.hete.supply.scm.server.scm.service.base.SkuAvgPriceBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.consistency.core.service.ConsistencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/31.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepairOrderCostStrategy implements CostCalculationStrategy {

    private final Environment environment;
    private final BatchCodeCostDao batchCodeCostDao;
    private final ConsistencyService consistencyService;
    private final RepairOrderItemDao repairOrderItemDao;
    private final RepairOrderResultDao repairOrderResultDao;
    private final SkuAvgPriceBaseService skuAvgPriceBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProcessOrderMaterialBaseService processOrderMaterialBaseService;
    private final BatchCodeCostBaseService batchCodeCostBaseService;

    public static final int REPAIRING_COST_RATIO = 1;

    /**
     * 计算返修单批次码总成本。
     *
     * @param repairOrderNo 返修号
     * @return 计算是否成功，成功返回true，否则返回false
     */
    @Override
    public boolean calculateBatchCodeCost(String repairOrderNo) {
        // 根据返修单号查询返修单条目列表
        List<RepairOrderItemPo> repairOrderItemPos = repairOrderItemDao.getListByRepairOrderNo(repairOrderNo);
        // 如果返修单条目列表为空，则记录错误日志并返回false
        if (CollectionUtils.isEmpty(repairOrderItemPos)) {
            log.error("计算返修单批次码总成本失败！原因:{},返修单号:{}", "返修信息列表为空", repairOrderNo);
            return false;
        }

        // 根据返修原料收货单查询原料价格
        MaterialAssociationBo materialAssociation
                = processOrderMaterialBaseService.getRepairOrderMaterialAssociation(repairOrderNo);
        List<ProcessMaterialReceiptPo> materialReceiptList = materialAssociation.getMaterialReceiptList();
        List<ProcessMaterialReceiptItemPo> materialReceiptItemList = materialAssociation.getMaterialReceiptItemList();
        List<SkuPriceQueryBo> skuPriceQueryBos
                = MaterialBuilder.buildSkuPriceQueryBos(materialReceiptList, materialReceiptItemList);
        final List<SkuPriceResultBo> materialPrices = calMaterialSkuPrices(skuPriceQueryBos);

        // 遍历返修单条目列表
        for (RepairOrderItemPo repairOrderItemPo : repairOrderItemPos) {
            BigDecimal actProcessedCompleteCnt = BigDecimal.valueOf(repairOrderItemPo.getActProcessedCompleteCnt());
            if (actProcessedCompleteCnt.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            Long repairOrderItemId = repairOrderItemPo.getRepairOrderItemId();
            String skuBatchCode = repairOrderItemPo.getBatchCode();
            String sku = repairOrderItemPo.getSku();

            // 清空已存在批次码成本信息
            clearProcessOrderCost(repairOrderNo, skuBatchCode);

            // 计算原料成本
            BigDecimal materialTotalCost = this.calculateMaterialTotalCost(repairOrderItemId, materialPrices);
            BigDecimal materialCostPerSku = materialTotalCost.divide(actProcessedCompleteCnt, 2, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            BatchCodeCostPo materialCostPo = BatchCodeCostBuilder.buildBatchCodeCostPo(repairOrderNo, repairOrderItemPo,
                    CostType.RAW_MATERIAL,
                    materialCostPerSku);
            batchCodeCostDao.insert(materialCostPo);

            // 计算人力成本
            BigDecimal laborTotalCost = calculateTotalLaborCost(skuBatchCode);
            BigDecimal laborCostPerSku = laborTotalCost.divide(actProcessedCompleteCnt, 2, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            BatchCodeCostPo laborCostPo = BatchCodeCostBuilder.buildBatchCodeCostPo(repairOrderNo, repairOrderItemPo,
                    CostType.REPAIR_MANPOWER,
                    laborCostPerSku);
            batchCodeCostDao.insert(laborCostPo);

            // 计算固定成本
            BigDecimal fixedTotalCost = calculateFixedCost(skuBatchCode);
            BigDecimal fixedCostPerSku = fixedTotalCost.divide(actProcessedCompleteCnt, 2, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            BatchCodeCostPo fixedCostPo = BatchCodeCostBuilder.buildBatchCodeCostPo(repairOrderNo, repairOrderItemPo,
                    CostType.FIXED_LOSS,
                    fixedCostPerSku);
            batchCodeCostDao.insert(fixedCostPo);

            // 计算总成本
            BigDecimal batchCodeTotalCost = BigDecimal.ZERO.add(materialCostPerSku)
                    .add(laborCostPerSku)
                    .add(fixedCostPerSku)
                    .setScale(2, RoundingMode.HALF_UP);
            if (batchCodeTotalCost.compareTo(BigDecimal.ZERO) < 0) {
                log.error("计算返修单批次码总成本发现异常！原因：{} 返修单号:{}", "存在总成本负数，请相关同事注意！",
                        repairOrderNo);
                return false;
            }

            // 处理成本后续操作
            handleCostPostProcessing(skuBatchCode, sku, batchCodeTotalCost);
        }

        return true;
    }

    /**
     * 获取物料SKU的价格信息，并根据一定逻辑处理价格为0的结果。
     *
     * @param skuPriceQueryBos 包含SKU价格查询对象的列表
     * @return 经处理后的物料SKU价格信息列表
     */
    public List<SkuPriceResultBo> calMaterialSkuPrices(List<SkuPriceQueryBo> skuPriceQueryBos) {
        // 获取批次单价 & 月初加权价
        List<SkuPriceResultBo> materialSkuPrices
                = processOrderMaterialBaseService.getMaterialSkuPrices(skuPriceQueryBos);

        // 筛选出价格为0的结果 & 如果没有价格为0的结果，则直接返回原始的原料SKU价格信息
        List<SkuPriceResultBo> withoutPrice = materialSkuPrices.stream()
                .filter(skuPriceResultBo -> Objects.isNull(skuPriceResultBo.getPrice()) ||
                        skuPriceResultBo.getPrice()
                                .compareTo(BigDecimal.ZERO) == 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(withoutPrice)) {
            return materialSkuPrices;
        }

        // 计算加权平均价
        List<QueryMonthStartWeightPriceBo> queryMonthStartWeiPrice
                = ProcOrderMaterialBuilder.buildQueryMonthStartWeightPriceBos(withoutPrice);
        List<MonthStartWeightAvgPriceResultBo> monthStartWeightAvgPrices
                = processOrderMaterialBaseService.getMonthStartWeightAvgPrices(queryMonthStartWeiPrice);

        // 更新原料SKU价格信息，将价格为0的结果替换为加权平均价
        for (SkuPriceResultBo skuPriceResultBo : materialSkuPrices) {
            String sku = skuPriceResultBo.getSku();
            BigDecimal price = skuPriceResultBo.getPrice();

            if (price.compareTo(BigDecimal.ZERO) == 0) {
                BigDecimal matchMonthStartWeightAvgPrice = monthStartWeightAvgPrices.stream()
                        .filter(monthStartWeightAvgPrice -> Objects.equals(sku, monthStartWeightAvgPrice.getSku()))
                        .findFirst()
                        .map(MonthStartWeightAvgPriceResultBo::getMonthStartWeightedAveragePrice)
                        .orElse(null);
                if (Objects.nonNull(matchMonthStartWeightAvgPrice) &&
                        matchMonthStartWeightAvgPrice.compareTo(BigDecimal.ZERO) > 0) {
                    skuPriceResultBo.setPrice(matchMonthStartWeightAvgPrice);

                    // 异步更新月初加权平均价
                    updateMonthStartWeightedAverage(sku, matchMonthStartWeightAvgPrice);
                }
            }
        }

        return materialSkuPrices;
    }

    /**
     * 计算原料总成本。
     * 该方法根据给定的返修订单项ID和原料价格列表，计算与之相关的原料总成本。
     *
     * @param repairOrderItemId 返修订单项ID
     * @param materialPrices    原料价格列表
     * @return 原料总成本
     * @throws BizException 如果无法计算原料批次码成本，则抛出BizException
     */
    private BigDecimal calculateMaterialTotalCost(Long repairOrderItemId,
                                                  List<SkuPriceResultBo> materialPrices) {
        // 初始化原料总成本为零
        BigDecimal materialTotalCost = BigDecimal.ZERO;

        // 查询与返修订单项ID相关的返修记录列表
        List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultDao.listByRepairOrderItemId(repairOrderItemId);

        // 根据批次码对使用数量进行分组求和
        Map<String, Integer> sumUseNumBySkuBatchCode = repairOrderResultPos.stream()
                .collect(Collectors.groupingBy(RepairOrderResultPo::getMaterialBatchCode,
                        Collectors.summingInt(RepairOrderResultPo::getMaterialUsageQuantity)));

        // 遍历原料收货记录明细，计算原料总成本
        for (Map.Entry<String, Integer> useEntry : sumUseNumBySkuBatchCode.entrySet()) {
            String materialBatchCode = useEntry.getKey();
            BigDecimal actUseNum = BigDecimal.valueOf(useEntry.getValue());

            // 查询批次码对应的成本价格，如果价格不存在或小于零，则抛出异常
            BigDecimal matchMaterialPrice = materialPrices.stream()
                    .filter(materialPrice -> Objects.equals(materialBatchCode, materialPrice.getBatchCode()))
                    .findFirst()
                    .map(SkuPriceResultBo::getPrice)
                    .orElse(null);
            if (Objects.isNull(matchMaterialPrice)) {
                throw new BizException("计算原料批次码成本失败，批次码成本价格不存在！批次码:{}", materialBatchCode);
            }
            if (matchMaterialPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new BizException("计算原料批次码成本失败，批次码成本价格小于0！批次码:{}", materialBatchCode);
            }
            if (matchMaterialPrice.compareTo(BigDecimal.ZERO) == 0) {
                log.warn("计算原料批次码成本过程发现批次码成本为0，请相关同事校验是否合法！批次码:{}", materialBatchCode);

                //通过成本为0的批次码匹配sku编码，发送飞书提醒
                materialPrices.stream().filter(item -> Objects.equals(materialBatchCode, item.getBatchCode()))
                        .findFirst().ifPresent(item -> batchCodeCostBaseService.sendAppTips(item.getSku()));
            }
            materialTotalCost = materialTotalCost.add(actUseNum.multiply(matchMaterialPrice).setScale(2, RoundingMode.HALF_UP));
        }

        return materialTotalCost;
    }

    /**
     * 处理返修成本后的后续操作。
     * 该方法用于处理特定批次的成本信息，包括更新成本并触发相关操作。
     *
     * @param batchCode    用于标识需要处理成本的唯一批次代码。
     * @param sku          与批次相关联的库存单位（SKU），以便更具体地进行标识。
     * @param curTotalCost 当前批次的总成本，用于进行成本处理的基础数据。
     */
    public void handleCostPostProcessing(String batchCode,
                                         String sku,
                                         BigDecimal curTotalCost) {
        // 构造查询平均成本的参数对象
        SkuAndBatchCodeBo queryBatchCodeAvgCostParam = new SkuAndBatchCodeBo();
        queryBatchCodeAvgCostParam.setSkuAvgPriceBizType(SkuAvgPriceBizType.PROCESS_ORDER);

        // 构造查询特定批次成本的参数对象
        SkuAndBatchCodeItemBo queryBatchCodes = new SkuAndBatchCodeItemBo();
        queryBatchCodes.setSku(sku);
        queryBatchCodes.setSkuBatchCode(batchCode);
        queryBatchCodes.setAccrueCnt(REPAIRING_COST_RATIO);
        queryBatchCodes.setAccruePrice(curTotalCost);
        queryBatchCodeAvgCostParam.setSkuAndBatchCodeItemBoList(List.of(queryBatchCodes));

        // 查询批次平均价格信息
        List<SkuAvgPriceBo> skuAvgPriceList = skuAvgPriceBaseService.getSkuAvgPrice(queryBatchCodeAvgCostParam);

        // 获取当前批次的平均价格，若不存在则默认为0
        BigDecimal latestTotalCost = skuAvgPriceList.stream()
                .filter(skuAvgPriceBo -> Objects.equals(batchCode, skuAvgPriceBo.getSkuBatchCode()))
                .findFirst()
                .map(SkuAvgPriceBo::getAvgPrice)
                .orElse(BigDecimal.ZERO);

        // 构造更新批次价格的DTO
        UpdateBatchCodePriceDto updateBatchCodePriceDto = BatchCodeCostBuilder.buildUpdateBatchCodePriceDto(batchCode,
                latestTotalCost);

        // 执行MQ消息发送，更新批次价格
        consistencySendMqService.execSendMq(BatchCodePriceUpdateHandler.class, updateBatchCodePriceDto);
    }

    /**
     * 计算返修固定总成本。
     * 该方法根据给定的批次码，计算与之相关的返修固定成本总额。
     *
     * @param batchCode 批次码
     * @return 返修固定总成本
     * @throws IllegalArgumentException 如果未找到与批次码相关的返修明细信息，则抛出IllegalArgumentException
     */
    public BigDecimal calculateFixedCost(String batchCode) {
        // 初始化固定总成本为零
        BigDecimal fixedCost = BigDecimal.ZERO;

        // 获取返修单个固定成本金额
        BigDecimal fixedCommission = environment.getProperty("repair.fixedCommission", BigDecimal.class);
        ParamValidUtils.requireNotNull(fixedCommission, "计算返修固定总成本失败！尚未配置返修单个固定成本金额。");

        // 查询与批次码相关的返修明细信息
        RepairOrderItemPo repairOrderItemPo = ParamValidUtils.requireNotNull(
                repairOrderItemDao.getByBatchCode(batchCode),
                StrUtil.format("计算返修人力总成本失败！未找到与该 {} 批次码相关的返修明细信息。", batchCode));

        // 查询与返修明细关联的返修记录
        Long repairOrderItemId = repairOrderItemPo.getRepairOrderItemId();
        List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultDao.listByRepairOrderItemId(
                repairOrderItemId);

        // 如果不存在与批次码相关的返修记录，则返回固定总成本为零
        if (CollectionUtils.isEmpty(repairOrderResultPos)) {
            return fixedCost;
        }

        // 计算总完成数量
        int totalCompletedQuantity = repairOrderResultPos.stream()
                .mapToInt(RepairOrderResultPo::getMaterialUsageQuantity)
                .sum();

        // 计算返修固定总成本
        fixedCost = BigDecimal.valueOf(totalCompletedQuantity)
                .multiply(fixedCommission)
                .setScale(2, RoundingMode.HALF_UP);

        return fixedCost;
    }

    /**
     * 计算返修人力总成本的方法。
     *
     * @param batchCode 批次码
     * @return 返修人力总成本
     */
    public BigDecimal calculateTotalLaborCost(String batchCode) {
        // 初始化人力总成本为零
        BigDecimal laborCost = BigDecimal.ZERO;

        BigDecimal preCommission = environment.getProperty("repair.preCommission", BigDecimal.class);
        ParamValidUtils.requireNotNull(preCommission, "计算返修人力总成本失败！尚未配置返修单个提成金额。");

        // 查询返修单明细信息
        RepairOrderItemPo repairOrderItemPo = ParamValidUtils.requireNotNull(
                repairOrderItemDao.getByBatchCode(batchCode),
                StrUtil.format("计算返修人力总成本失败！未找到与该 {} 批次码相关的返修明细信息 {} ", batchCode));

        // 查询返修单关联的返修记录
        Long repairOrderItemId = repairOrderItemPo.getRepairOrderItemId();
        List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultDao.listByRepairOrderItemId(
                repairOrderItemId);
        if (CollectionUtils.isEmpty(repairOrderResultPos)) {
            return laborCost;
        }

        int totalCompletedQuantity = repairOrderResultPos.stream()
                .mapToInt(RepairOrderResultPo::getMaterialUsageQuantity)
                .sum();
        laborCost = BigDecimal.valueOf(totalCompletedQuantity)
                .multiply(preCommission)
                .setScale(2, RoundingMode.HALF_UP);
        return laborCost;
    }

    /**
     * 清除与指定相关订单号和批次码相关的加工单成本信息。
     * 如果存在与指定相关订单号和批次码相关的加工单成本信息，则将它们的删除时间戳设置为当前时间，并更新数据库。
     *
     * @param relateOrderNo 相关订单号
     * @param batchCode     批次码
     */
    public void clearProcessOrderCost(String relateOrderNo,
                                      String batchCode) {
        // 根据相关订单号和批次码查询已存在的加工单成本信息列表
        List<BatchCodeCostPo> existBatchCodeCostPos = batchCodeCostDao.listByRelateOrderNoNBatchCode(relateOrderNo,
                batchCode);

        // 如果存在加工单成本信息，则将它们的删除时间戳设置为当前时间，并更新数据库
        if (CollectionUtils.isNotEmpty(existBatchCodeCostPos)) {
            existBatchCodeCostPos.forEach(
                    existBatchCodeCostPo -> existBatchCodeCostPo.setDelTimestamp(DateUtil.current()));
            batchCodeCostDao.updateBatchByIdVersion(existBatchCodeCostPos);
        }
    }


    public void updateMonthStartWeightedAverage(String materialSku, BigDecimal matchMonthStartWeightAvgPrice) {
        if (Objects.isNull(matchMonthStartWeightAvgPrice) ||
                matchMonthStartWeightAvgPrice.compareTo(BigDecimal.ZERO) == 0) {
            log.info("更新月初加权价结束！sku:{} 价格为空或为0:", materialSku);
            return;
        }

        MonthStartWeightAvgPriceResultBo monthStartWeightAvgPriceResultBo = new MonthStartWeightAvgPriceResultBo();
        monthStartWeightAvgPriceResultBo.setSku(materialSku);
        monthStartWeightAvgPriceResultBo.setMonthStartWeightedAveragePrice(matchMonthStartWeightAvgPrice);
        consistencyService.execAsyncTask(UpdMonWightAvgHandler.class, monthStartWeightAvgPriceResultBo);
    }

    @Override
    public CostCalculationStrategyType getCostCalculationStrategyType() {
        return CostCalculationStrategyType.REPAIR_ORDER;
    }
}
