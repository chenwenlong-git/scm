package com.hete.supply.scm.server.scm.process.builder;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcLaborEfficiencyReportBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcProcedureEfficiencyReportBo;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/3/14.
 */
public class ProcLaborEfficiencyReportBoBuilder {

    public static List<ProcLaborEfficiencyReportBo> buildProcLaborEfficiencyReportBos(List<LocalDate> localDateRange) {
        return (CollectionUtils.isEmpty(localDateRange)) ? Collections.emptyList() :
                localDateRange.stream()
                        .map(date -> {
                            ProcLaborEfficiencyReportBo reportBo = new ProcLaborEfficiencyReportBo();
                            reportBo.setYearMonthDay(date);
                            return reportBo;
                        })
                        .collect(Collectors.toList());
    }

    public static List<ProcProcedureEfficiencyReportBo> buildProcProcedureEfficiencyReportBos(List<LocalDate> localDateRange) {
        return (CollectionUtils.isEmpty(localDateRange)) ? Collections.emptyList() :
                localDateRange.stream()
                        .map(date -> {
                            ProcProcedureEfficiencyReportBo reportBo = new ProcProcedureEfficiencyReportBo();
                            reportBo.setYearMonthDay(date);
                            return reportBo;
                        })
                        .collect(Collectors.toList());
    }

    public static ProcLaborEfficiencyReportBo buildProcLaborEfficiencyReportBo(YearMonth yearMonth) {
        ProcLaborEfficiencyReportBo reportBo = new ProcLaborEfficiencyReportBo();
        reportBo.setYearMonth(yearMonth);
        return reportBo;
    }

    public static ProcProcedureEfficiencyReportBo buildProcProcedureEfficiencyReportBo(YearMonth yearMonth) {
        ProcProcedureEfficiencyReportBo reportBo = new ProcProcedureEfficiencyReportBo();
        reportBo.setYearMonth(yearMonth);
        return reportBo;
    }
}
