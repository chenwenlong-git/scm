package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.builder.BatchCodeCostBuilder;
import com.hete.supply.scm.server.scm.enums.CostCalculationStrategyType;
import com.hete.supply.scm.server.scm.process.dao.BatchCodeCostDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderItemDao;
import com.hete.supply.scm.server.scm.process.entity.po.BatchCodeCostPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.enums.CostType;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculationStrategy;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderMaterialBaseService;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderScanBaseService;
import com.hete.support.api.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/1/31.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReworkOrderCostStrategy implements CostCalculationStrategy {

    private final ProcessOrderDao processOrderDao;
    private final BatchCodeCostDao batchCodeCostDao;
    private final ProcessOrderItemDao processOrderItemDao;
    private final CostCoefficientsBizService costCoefficientsBizService;
    private final ProcessOrderScanBaseService processOrderScanBaseService;
    private final ProcessOrderMaterialBaseService processOrderMaterialBaseService;

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

        // 批次码总成本
        BigDecimal batchCodeTotalCost = BigDecimal.ZERO;

        // 计算当前返工单原材料成本
        BigDecimal materialTotalCost = processOrderMaterialBaseService.calculateMaterialTotalCost(relateOrderNo);
        BigDecimal materialCostPerSku = materialTotalCost.divide(processNum, 2, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BatchCodeCostPo materialCostPo
                = BatchCodeCostBuilder.buildBatchCodeCostPo(processOrderPo, oneOfProcessItem, CostType.RAW_MATERIAL,
                materialCostPerSku);
        batchCodeCostDao.insert(materialCostPo);

        // 获取原加工单号 & 原加工单的批次码成本列表
        String parentProcessOrderNo = processOrderPo.getParentProcessOrderNo();
        List<BatchCodeCostPo> batchCodeCostPos = batchCodeCostDao.listByRelateOrderNo(parentProcessOrderNo);

        // 如果有原加工单的批次码成本列表，则使用原加工单总成本 否则重新计算原加工单总成本
        BigDecimal parentProcessOrderCost
                = CollectionUtils.isNotEmpty(batchCodeCostPos) ? getBatchCostTotalCost(
                batchCodeCostPos) : calculateBatchCostTotalCost(parentProcessOrderNo, processNum);
        BatchCodeCostPo parentCostPo
                = BatchCodeCostBuilder.buildBatchCodeCostPo(processOrderPo, oneOfProcessItem, CostType.REWORK,
                parentProcessOrderCost);
        batchCodeCostDao.insert(parentCostPo);

        // 汇总当前批次码总成本
        batchCodeTotalCost = batchCodeTotalCost.add(materialCostPerSku)
                .add(parentProcessOrderCost)
                .setScale(2, RoundingMode.HALF_UP);
        if (batchCodeTotalCost.compareTo(BigDecimal.ZERO) < 0) {
            log.error("计算返工单批次码总成本发现异常！原因：{} 加工单号:{}", "存在总成本负数，请相关同事注意！",
                    relateOrderNo);
            return false;
        }
        return true;
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

    /**
     * @Description 计算批次码总成本
     * @author yanjiawei
     * @Date 2024/2/1 09:28
     */
    public BigDecimal calculateBatchCostTotalCost(String parentProcessOrderNo,
                                                  BigDecimal processNum) {
        BigDecimal materialTotalCost = processOrderMaterialBaseService.calculateMaterialTotalCost(parentProcessOrderNo);
        BigDecimal materialCostPerSku = materialTotalCost.divide(processNum, 2, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal laborTotalCost = processOrderScanBaseService.calculateLaborCost(parentProcessOrderNo);
        BigDecimal laborCostPerSku = laborTotalCost.divide(processNum, 2, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal coefficient = costCoefficientsBizService.getLatestCoefficient();
        if (Objects.isNull(coefficient) || coefficient.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("计算加工单批次码总成本失败！ 原因:{}", "尚未配置成本系数，请配置成本系数");
        }
        BigDecimal fixedTotalCost = calculateFixedCost(laborCostPerSku, coefficient);

        return BigDecimal.ZERO.add(materialCostPerSku)
                .add(laborCostPerSku)
                .add(fixedTotalCost)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @Description 计算批次码总成本
     * @author yanjiawei
     * @Date 2024/2/1 09:28
     */
    public BigDecimal getBatchCostTotalCost(List<BatchCodeCostPo> batchCodeCostPos) {
        return batchCodeCostPos.stream()
                .map(BatchCodeCostPo::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    @Override
    public CostCalculationStrategyType getCostCalculationStrategyType() {
        return CostCalculationStrategyType.REWORK_PROCESS_ORDER;
    }
}
