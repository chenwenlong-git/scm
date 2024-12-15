package com.hete.supply.scm.server.scm.process.service.biz;

import com.hete.supply.scm.server.scm.enums.ReportBizType;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcLaborEfficiencyReportBo;
import com.hete.supply.scm.server.scm.service.base.IReportServer;
import com.hete.supply.scm.server.scm.service.base.IReportStatisticStrategy;
import com.hete.support.api.exception.BizException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 出货人效表服务类
 *
 * @author yanjiawei
 * Created on 2024/3/14.
 */
@Service
public class ProcLaborEfficiencyReportServer implements IReportServer<List<ProcLaborEfficiencyReportBo>> {


    private IReportStatisticStrategy<List<ProcLaborEfficiencyReportBo>> statisticStrategy;

    @Override
    public void setStatisticStrategy(IReportStatisticStrategy<List<ProcLaborEfficiencyReportBo>> statisticStrategy) {
        this.statisticStrategy = statisticStrategy;
    }

    @Override
    public void generateReport() {
        generateReport("");
    }

    @Override
    public void generateReport(String dateTimeStr) {
        if (Objects.isNull(statisticStrategy)) {
            throw new BizException("报表执行异常！策略为空，请设置策略");
        }
        List<ProcLaborEfficiencyReportBo> laborEfficiencyReportBos = statisticStrategy.doStatistic(dateTimeStr);
        statisticStrategy.doPush(laborEfficiencyReportBos);
    }

    @Override
    public ReportBizType getReportBizType() {
        return ReportBizType.PROC_LABOR_EFFICIENCY_REPORT;
    }

}
