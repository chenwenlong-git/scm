package com.hete.supply.scm.server.scm.process.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.enums.CostCalculationStrategyType;
import com.hete.support.core.springsupport.SpringContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanjiawei
 * Created on 2024/1/31.
 */
@Component
public class CostCalculatorStrategyFactory {

    private static final Map<CostCalculationStrategyType, CostCalculationStrategy> STRATEGY_MAP
            = new HashMap<>(16);

    /**
     * 初始化策略映射，将不同的策略和筛选策略关联起来。
     */
    @PostConstruct
    public synchronized void initStrategyMap() {
        Map<String, CostCalculationStrategy> beansOfType = SpringContext.getBeansOfType(CostCalculationStrategy.class);

        beansOfType.forEach((beanName, strategy) -> {
            CostCalculationStrategyType costCalculationStrategyType = strategy.getCostCalculationStrategyType();
            STRATEGY_MAP.put(costCalculationStrategyType, strategy);
        });
    }

    /**
     * 根据成本计算策略类型获取相应的成本计算策略实例。
     *
     * @param costCalculationStrategyType 成本计算策略类型。
     * @return 成本计算策略实例。
     * @throws IllegalArgumentException 如果找不到对应类型的成本计算策略，抛出异常。
     */
    public CostCalculationStrategy getCostCalculationStrategy(CostCalculationStrategyType costCalculationStrategyType) {
        // 如果策略映射为空，进行初始化
        if (CollectionUtils.isEmpty(STRATEGY_MAP)) {
            initStrategyMap();
        }

        // 从映射中获取成本计算策略
        CostCalculationStrategy costCalculationStrategy = STRATEGY_MAP.get(costCalculationStrategyType);

        // 检查是否成功获取策略，如果没有，抛出异常
        ParamValidUtils.requireNotNull(costCalculationStrategy,
                "无法通过{}类型获取策略，请检查！" + costCalculationStrategyType);

        return costCalculationStrategy;
    }

}
