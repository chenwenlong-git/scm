package com.hete.supply.scm.server.scm.service.base;

import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;

/**
 * @author yanjiawei
 * Created on 2024/3/14.
 */
public interface IReportStatisticStrategy<T> {

    void initFeiShuOnlineSheetConfig(String appToken,
                                     String tableId);

    T doStatistic();

    T doStatistic(String dateTimeStr);

    ReportStatisticStrategyType getReportStatisticStrategyType();

    void doPush(T t);
}
