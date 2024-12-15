package com.hete.supply.scm.server.scm.qc.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.QcExportType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.springsupport.SpringContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 质检导出策略处理器，用于管理不同的质检明细导出策略和相应的筛选策略。
 *
 * @author yanjiawei
 */
@Component
public class QcExportStrategyFactory {

    /**
     * 存储质检明细导出策略和筛选策略的映射
     */
    private static final Map<QcExportType, QcExportFilterStrategy> STRATEGY_MAP
            = new HashMap<>(16);

    /**
     * 初始化策略映射，将不同的策略和筛选策略关联起来。
     */
    @PostConstruct
    public synchronized void initStrategyMap() {
        Map<String, QcExportFilterStrategy> beansOfType = SpringContext.getBeansOfType(QcExportFilterStrategy.class);

        beansOfType.forEach((beanName, strategy) -> {
            QcExportType qcExportType = strategy.getQcDetailExportStrategy();
            STRATEGY_MAP.put(qcExportType, strategy);
        });
    }

    /**
     * 获取特定质检明细导出策略对应的筛选策略。
     *
     * @param strategyType 质检明细导出策略类型
     * @return 质检明细导出策略对应的筛选策略
     * @throws ParamIllegalException 如果无法通过指定的策略类型获取筛选策略，将抛出异常
     */
    public QcExportFilterStrategy getQcExportFilterStrategy(QcExportType strategyType) {
        if (CollectionUtils.isEmpty(STRATEGY_MAP)) {
            initStrategyMap();
        }

        QcExportFilterStrategy qcExportFilterStrategy = STRATEGY_MAP.get(strategyType);
        if (Objects.isNull(qcExportFilterStrategy)) {
            throw new ParamIllegalException("无法通过{}类型获取策略，请检查！", strategyType);
        }
        return qcExportFilterStrategy;
    }
}

