package com.hete.supply.scm.server.scm.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.entity.bo.FeiShuSheetConfigBo;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.springsupport.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
@Component
public class ReportStatisticStrategyFactory {
    @Autowired
    private FeiShuOnlineSheetBaseService feiShuOnlineSheetBaseService;

    private static final Map<ReportStatisticStrategyType, IReportStatisticStrategy> reportStatisticStrategyMap
            = new HashMap<>(16);

    /**
     * 初始化策略映射，将不同的策略和筛选策略关联起来。
     */
    @PostConstruct
    public synchronized void initReportServerMap() {
        Map<String, IReportStatisticStrategy> beansOfType = SpringContext.getBeansOfType(
                IReportStatisticStrategy.class);

        beansOfType.forEach((beanName, reportStatisticStrategy) -> {
            ReportStatisticStrategyType strategyType = reportStatisticStrategy.getReportStatisticStrategyType();


            FeiShuSheetConfigBo configByReportBizType = feiShuOnlineSheetBaseService.getConfigByReportBizType(
                    strategyType);
            String appToken = configByReportBizType.getAppToken();
            String tableId = configByReportBizType.getTableId();
            reportStatisticStrategy.initFeiShuOnlineSheetConfig(appToken, tableId);

            reportStatisticStrategyMap.put(strategyType, reportStatisticStrategy);
        });
    }

    public IReportStatisticStrategy getStatisticStrategy(ReportStatisticStrategyType reportStatisticStrategyType) {
        if (CollectionUtils.isEmpty(reportStatisticStrategyMap)) {
            initReportServerMap();
        }

        IReportStatisticStrategy reportServer = reportStatisticStrategyMap.get(reportStatisticStrategyType);
        if (Objects.isNull(reportServer)) {
            throw new ParamIllegalException("无法通过{}报表业务类型获取报表服务，请检查！", reportStatisticStrategyType);
        }
        return reportServer;
    }


}
