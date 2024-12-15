package com.hete.supply.scm.server.scm.process.service.biz;

import com.hete.supply.scm.server.scm.enums.ReportBizType;

import com.hete.supply.scm.server.scm.service.base.IReportServer;
import com.hete.supply.scm.server.scm.service.base.IReportStatisticStrategy;
import com.hete.supply.sda.api.scm.process.entity.vo.DailyPlatformDemandVo;
import com.hete.support.api.exception.BizException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 平台需求报表服务类
 *
 * @author yanjiawei
 * Created on 2024/6/19.
 */
@Service
public class DailyProcPdReportServer implements IReportServer<DailyPlatformDemandVo> {

    private IReportStatisticStrategy<DailyPlatformDemandVo> statisticStrategy;

    @Override
    public void setStatisticStrategy(IReportStatisticStrategy<DailyPlatformDemandVo> statisticStrategy) {
        this.statisticStrategy = statisticStrategy;
    }

    @Override
    public void generateReport() {
        if (Objects.isNull(statisticStrategy)) {
            throw new BizException("报表执行异常！策略为空，请设置策略");
        }
        DailyPlatformDemandVo statisticResult = statisticStrategy.doStatistic();
        if (Objects.isNull(statisticResult)) {
            return;
        }
        statisticStrategy.doPush(statisticResult);
    }

    @Override
    public void generateReport(String dateTimeStr) {
        if (Objects.isNull(statisticStrategy)) {
            throw new BizException("报表执行异常！策略为空，请设置策略");
        }
        DailyPlatformDemandVo statisticResult = statisticStrategy.doStatistic(dateTimeStr);
        if (Objects.isNull(statisticResult)) {
            return;
        }
        statisticStrategy.doPush(statisticResult);
    }

    @Override
    public ReportBizType getReportBizType() {
        return ReportBizType.PROC_DAILY_PlAT_PD_REPORT;
    }
}