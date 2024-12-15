package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.builder.BatchCodeCostBuilder;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeItemBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAvgPriceBo;
import com.hete.supply.scm.server.scm.enums.CostCalculationStrategyType;
import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import com.hete.supply.scm.server.scm.process.dao.BatchCodeCostDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderItemDao;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.process.entity.po.BatchCodeCostPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.enums.CostType;
import com.hete.supply.scm.server.scm.process.handler.BatchCodePriceUpdateHandler;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculationStrategy;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderMaterialBaseService;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderScanBaseService;
import com.hete.supply.scm.server.scm.service.base.SkuAvgPriceBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * @author yanjiawei
 * @Description 非返工订单成本计算策略。
 * @Date 2024/3/4 14:37
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NonReworkOrderCostStrategy implements CostCalculationStrategy {

    private final ProcessOrderDao processOrderDao;
    private final BatchCodeCostDao batchCodeCostDao;
    private final ProcessOrderItemDao processOrderItemDao;
    private final SkuAvgPriceBaseService skuAvgPriceBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final CostCoefficientsBizService costCoefficientsBizService;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final ProcessOrderMaterialBaseService processOrderMaterialBaseService;

    public static final int PROCESSING_COST_RATIO = 1;

    /**
     * 计算加工单批次码总成本。
     *
     * @param relateOrderNo 相关订单号
     * @return 计算是否成功，成功返回true，否则返回false
     */
    @Override
    public boolean calculateBatchCodeCost(String relateOrderNo) {
        // 前置校验
        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(relateOrderNo);
        if (Objects.isNull(processOrderPo)) {
            log.error("计算加工单批次码总成本失败！原因:{}，加工单号:{}", "信息列表为空", relateOrderNo);
            return false;
        }
        List<ProcessOrderItemPo> processOrderItemPos = processOrderItemDao.getByProcessOrderNo(relateOrderNo);
        if (CollectionUtils.isEmpty(processOrderItemPos)) {
            log.error("计算加工单批次码总成本失败！原因:{},加工单号:{}", "成品信息列表为空", relateOrderNo);
            return false;
        }
        ProcessOrderItemPo oneOfProcessItem = processOrderItemPos.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (Objects.isNull(oneOfProcessItem)) {
            log.error("计算加工单批次码总成本失败！原因:{},加工单号:{}", "成品信息为空", relateOrderNo);
            return false;
        }
        BigDecimal processNum = BigDecimal.valueOf(
                oneOfProcessItem.getProcessNum() != null ? oneOfProcessItem.getProcessNum() : 0);
        if (processNum.compareTo(BigDecimal.ZERO) == 0) {
            log.error("计算加工单批次码总成本失败！原因:{},加工单号:{}", "加工数量等于0", relateOrderNo);
            return false;
        }
        String skuBatchCode = oneOfProcessItem.getSkuBatchCode();
        if (StrUtil.isBlank(skuBatchCode)) {
            log.warn("计算加工批次码成本价结束！原因:{},加工单号:{}", "无批次码", relateOrderNo);
            return true;
        }

        // 清空已存在加工单成本信息
        clearProcessOrderCost(relateOrderNo);

        // 计算原料成本
        BigDecimal materialTotalCost = processOrderMaterialBaseService.calculateMaterialTotalCost(relateOrderNo);
        BigDecimal materialCostPerSku = materialTotalCost.divide(processNum, 2, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BatchCodeCostPo materialCostPo = BatchCodeCostBuilder.buildBatchCodeCostPo(processOrderPo, oneOfProcessItem,
                CostType.RAW_MATERIAL,
                materialCostPerSku);
        batchCodeCostDao.insert(materialCostPo);

        // 计算人力成本
        BigDecimal laborTotalCost = processOrderScanBaseService.calculateLaborCost(relateOrderNo);
        BigDecimal laborCostPerSku = laborTotalCost.divide(processNum, 2, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BatchCodeCostPo laborCostPo = BatchCodeCostBuilder.buildBatchCodeCostPo(processOrderPo, oneOfProcessItem,
                CostType.PROCESS_SCAN_MANPOWER,
                laborCostPerSku);
        batchCodeCostDao.insert(laborCostPo);

        // 计算固定成本
        BigDecimal coefficient = costCoefficientsBizService.getLatestCoefficient();
        if (Objects.isNull(coefficient) || coefficient.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("计算加工单批次码总成本失败！ 原因:{}", "尚未配置成本系数，请配置成本系数");
        }
        BigDecimal fixedTotalCost = calculateFixedCost(laborCostPerSku, coefficient);
        BatchCodeCostPo fixedCostPo
                = BatchCodeCostBuilder.buildBatchCodeCostPo(processOrderPo, oneOfProcessItem, CostType.FIXED_LOSS,
                fixedTotalCost);
        batchCodeCostDao.insert(fixedCostPo);

        // 计算总成本
        BigDecimal batchCodeTotalCost = BigDecimal.ZERO.add(materialCostPerSku)
                .add(laborCostPerSku)
                .add(fixedTotalCost)
                .setScale(2, RoundingMode.HALF_UP);
        if (batchCodeTotalCost.compareTo(BigDecimal.ZERO) < 0) {
            log.error("计算加工单批次码总成本发现异常！原因：{} 加工单号:{}", "存在总成本负数，请相关同事注意！",
                    relateOrderNo);
            return false;
        }

        // 处理成本后续操作
        String sku = oneOfProcessItem.getSku();
        handleCostPostProcessing(skuBatchCode, sku, batchCodeTotalCost);
        return true;
    }

    /**
     * 清除与指定相关订单号相关的加工单成本信息。
     * 如果存在与指定相关订单号相关的加工单成本信息，则将它们的删除时间戳设置为当前时间，并更新数据库。
     *
     * @param relateOrderNo 相关订单号
     */
    public void clearProcessOrderCost(String relateOrderNo) {
        // 根据相关订单号查询已存在的加工单成本信息列表
        List<BatchCodeCostPo> existBatchCodeCostPos = batchCodeCostDao.listByRelateOrderNo(relateOrderNo);

        // 如果存在加工单成本信息，则将它们的删除时间戳设置为当前时间，并更新数据库
        if (CollectionUtils.isNotEmpty(existBatchCodeCostPos)) {
            existBatchCodeCostPos.forEach(
                    existBatchCodeCostPo -> existBatchCodeCostPo.setDelTimestamp(DateUtil.current()));
            batchCodeCostDao.updateBatchByIdVersion(existBatchCodeCostPos);
        }
    }

    /**
     * 处理加工成本后的后续操作。
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
        queryBatchCodes.setAccrueCnt(PROCESSING_COST_RATIO);
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
        UpdateBatchCodePriceDto updateBatchCodePriceDto
                = BatchCodeCostBuilder.buildUpdateBatchCodePriceDto(batchCode, latestTotalCost);

        // 执行MQ消息发送，更新批次价格
        consistencySendMqService.execSendMq(BatchCodePriceUpdateHandler.class, updateBatchCodePriceDto);
    }


    /**
     * 计算固定成本
     *
     * @param laborCost   人力成本
     * @param coefficient 系数
     * @return 固定成本
     */
    public BigDecimal calculateFixedCost(BigDecimal laborCost,
                                         BigDecimal coefficient) {
        if (laborCost == null || coefficient == null) {
            return BigDecimal.ZERO;
        }
        return laborCost.multiply(coefficient)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public CostCalculationStrategyType getCostCalculationStrategyType() {
        return CostCalculationStrategyType.NON_REWORK_PROCESS_ORDER;
    }
}
