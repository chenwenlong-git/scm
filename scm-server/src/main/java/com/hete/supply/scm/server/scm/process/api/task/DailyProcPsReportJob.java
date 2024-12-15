package com.hete.supply.scm.server.scm.process.api.task;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.server.scm.enums.ReportBizType;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import com.hete.supply.scm.server.scm.service.base.IReportServer;
import com.hete.supply.scm.server.scm.service.base.IReportStatisticStrategy;
import com.hete.supply.scm.server.scm.service.base.ReportServerFactory;
import com.hete.supply.scm.server.scm.service.base.ReportStatisticStrategyFactory;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/6/20.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DailyProcPsReportJob {
    private final ReportServerFactory reportServerFactory;
    private final ReportStatisticStrategyFactory reportStatisticStrategyFactory;

    @XxlJob(value = "dailyProcPsReportTask")
    public void dailyProcPsReportTask() {
        String dayCnTimeStr = XxlJobHelper.getJobParam();
        try {
            IReportServer reportServer = reportServerFactory.getReportServer(ReportBizType.PROC_DAILY_PlAT_PS_REPORT);
            IReportStatisticStrategy statisticStrategy
                    = reportStatisticStrategyFactory.getStatisticStrategy(ReportStatisticStrategyType.PROC_PLAT_PS_DAILY);
            reportServer.setStatisticStrategy(statisticStrategy);

            if (StrUtil.isBlank(dayCnTimeStr)) {
                reportServer.generateReport();
            } else {
                reportServer.generateReport(dayCnTimeStr);
            }
        } catch (Exception e) {
            log.error("各平台出库数（当月）报表失败！ ", e);
        }
    }
}
