package com.hete.supply.scm.server.scm.process.api.task;

import cn.hutool.core.exceptions.ExceptionUtil;
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
 * Created on 2024/3/13.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MonthlyReportJob {
    private final ReportServerFactory reportServerFactory;
    private final ReportStatisticStrategyFactory reportStatisticStrategyFactory;

    @XxlJob(value = "monthlyReportTask")
    public void execute() {
        String dayCnTimeStr = XxlJobHelper.getJobParam();

        try {
            IReportServer reportServer = reportServerFactory.getReportServer(
                    ReportBizType.PROC_LABOR_EFFICIENCY_REPORT);
            IReportStatisticStrategy statisticStrategy = reportStatisticStrategyFactory.getStatisticStrategy(
                    ReportStatisticStrategyType.PROC_LABOR_EFFICIENCY_MONTHLY);
            reportServer.setStatisticStrategy(statisticStrategy);
            if (StrUtil.isBlank(dayCnTimeStr)) {
                reportServer.generateReport();
            } else {
                reportServer.generateReport(dayCnTimeStr);
            }
        } catch (Exception e) {
            log.error("统计加工部人效报表失败 {}", ExceptionUtil.stacktraceToString(e));
        }

        try {
            IReportServer reportServer = reportServerFactory.getReportServer(
                    ReportBizType.PROC_PROCEDURE_EFFICIENCY_REPORT);
            IReportStatisticStrategy statisticStrategy = reportStatisticStrategyFactory.getStatisticStrategy(
                    ReportStatisticStrategyType.PROC_PROCEDURE_EFFICIENCY_MONTHLY);
            reportServer.setStatisticStrategy(statisticStrategy);

            if (StrUtil.isBlank(dayCnTimeStr)) {
                reportServer.generateReport();
            } else {
                reportServer.generateReport(dayCnTimeStr);
            }
        } catch (Exception e) {
            log.error("统计加工工序人效报表失败！ {}", ExceptionUtil.stacktraceToString(e));
        }
    }
}
