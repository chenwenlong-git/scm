package com.hete.supply.scm.server.scm.process.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.enums.ProcessType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.springsupport.SpringContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class CommissionDetailCalculationStrategyFactory {

    private static final Map<ProcessType, CommissionDetailsCalculationStrategy> STRATEGY_MAP
            = new HashMap<>(16);

    /**
     * 初始化策略映射，将不同的策略和筛选策略关联起来。
     */
    @PostConstruct
    public synchronized void initStrategyMap() {
        Map<String, CommissionDetailsCalculationStrategy> beansOfType = SpringContext.getBeansOfType(
                CommissionDetailsCalculationStrategy.class);

        beansOfType.forEach((beanName, strategy) -> {
            ProcessType processType = strategy.getProcessType();
            STRATEGY_MAP.put(processType, strategy);
        });
    }

    public CommissionDetailsCalculationStrategy getStrategy(ProcessType processType) {
        if (CollectionUtils.isEmpty(STRATEGY_MAP)) {
            initStrategyMap();
        }

        CommissionDetailsCalculationStrategy commissionDetailsCalculationStrategy = STRATEGY_MAP.get(processType);
        if (Objects.isNull(commissionDetailsCalculationStrategy)) {
            throw new ParamIllegalException("无法通过{}类型获取策略，请检查！", processType);
        }
        return commissionDetailsCalculationStrategy;
    }
}

