package com.hete.supply.scm.server.scm.process.service.base;

import com.hete.supply.scm.server.scm.enums.CostCalculationStrategyType;

/**
 * @author yanjiawei
 * Created on 2024/1/31.
 */
public interface CostCalculationStrategy {

    /**
     * 根据提供的单号计算批次码成本。
     *
     * @param relateOrderNo 要计算成本的加工单号。
     * @return 如果成本计算适用或符合特定条件，则返回 true；否则返回 false。
     */
    boolean calculateBatchCodeCost(String relateOrderNo);

    /**
     * 获取当前成本计算策略的类型。
     *
     * @return 成本计算策略类型。
     */
    CostCalculationStrategyType getCostCalculationStrategyType();
}
