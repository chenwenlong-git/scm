package com.hete.supply.scm.demo;

import com.hete.supply.scm.server.scm.enums.ReportBizType;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import com.hete.supply.scm.server.scm.service.base.IReportServer;
import com.hete.supply.scm.server.scm.service.base.IReportStatisticStrategy;
import com.hete.supply.scm.server.scm.service.base.ReportServerFactory;
import com.hete.supply.scm.server.scm.service.base.ReportStatisticStrategyFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author yanjiawei
 * Created on 2024/6/20.
 */
@SpringBootTest
@ActiveProfiles("local")
public class ProcReportTest {

    @Autowired
    private ReportServerFactory reportServerFactory;
    @Autowired
    private ReportStatisticStrategyFactory reportStatisticStrategyFactory;

    @Test
    void testPdReport() {
        IReportServer reportServer = reportServerFactory.getReportServer(ReportBizType.PROC_DAILY_PlAT_PD_REPORT);
        IReportStatisticStrategy statisticStrategy
                = reportStatisticStrategyFactory.getStatisticStrategy(ReportStatisticStrategyType.PROC_PLAT_PD_DAILY);
        reportServer.setStatisticStrategy(statisticStrategy);
        reportServer.generateReport("2024-06-18");
    }
}
