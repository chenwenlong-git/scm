package com.hete.supply.scm.server.scm.service.base;

import com.hete.supply.scm.server.scm.enums.ReportBizType;

/**
 * @author yanjiawei
 * Created on 2024/3/14.
 */
public interface IReportServer<T> {

    void setStatisticStrategy(IReportStatisticStrategy<T> statisticStrategy);

    void generateReport();

    void generateReport(String dateTimeStr);

    ReportBizType getReportBizType();

}
