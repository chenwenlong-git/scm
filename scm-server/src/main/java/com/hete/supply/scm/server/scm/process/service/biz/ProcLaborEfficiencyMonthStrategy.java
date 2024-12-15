package com.hete.supply.scm.server.scm.process.service.biz;

import com.hete.supply.mc.api.tables.entity.dto.FeiShuTablesBatchCreateDto;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.builder.FeiShuTableBuilder;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import com.hete.supply.scm.server.scm.process.builder.ProcLaborEfficiencyReportBoBuilder;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcLaborEfficiencyReportBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ReportTimeRangeBo;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderReportBaseService;
import com.hete.supply.scm.server.scm.service.base.FeiShuOnlineSheetBaseService;
import com.hete.supply.scm.server.scm.service.base.IFeiShuOnlineSheet;
import com.hete.supply.scm.server.scm.service.base.IReportStatisticStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 加工部月维度人效报表
 *
 * @author yanjiawei
 * Created on 2024/3/15.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcLaborEfficiencyMonthStrategy implements IReportStatisticStrategy<List<ProcLaborEfficiencyReportBo>>,
        IFeiShuOnlineSheet<List<ProcLaborEfficiencyReportBo>> {
    private String appToken;
    private String tableId;

    private final ProcessOrderReportBaseService processOrderReportBaseService;
    private final ProcessOrderReportBizService processOrderReportBizService;
    private final McRemoteService mcRemoteService;
    private final FeiShuOnlineSheetBaseService feiShuOnlineSheetBaseService;

    @Override
    public void initFeiShuOnlineSheetConfig(String appToken,
                                            String tableId) {
        this.appToken = appToken;
        this.tableId = tableId;
    }

    @Override
    public List<ProcLaborEfficiencyReportBo> doStatistic() {
        return doStatistic("");
    }

    @Override
    public List<ProcLaborEfficiencyReportBo> doStatistic(String dateTimeStr) {
        // 实现月度加工部人效报表统计逻辑
        log.info("执行2023年加工部平均人效报表统计逻辑");

        YearMonth yearMonth = processOrderReportBaseService.getBeforeYearMonth(dateTimeStr);
        ProcLaborEfficiencyReportBo reportResult
                = ProcLaborEfficiencyReportBoBuilder.buildProcLaborEfficiencyReportBo(yearMonth);

        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        LocalDateTime lastSecondOfDay = lastDayOfMonth.atTime(23, 59, 59);
        long timestamp = lastSecondOfDay.toInstant(ZoneOffset.of("+8"))
                .toEpochMilli();
        reportResult.setTimestamp(timestamp);

        ReportTimeRangeBo reportTimeRangeBo = processOrderReportBaseService.getReportTimeRangeBo(yearMonth);
        LocalDateTime startUtc = reportTimeRangeBo.getStartUtc();
        LocalDateTime endUtc = reportTimeRangeBo.getEndUtc();
        processOrderReportBizService.doCalProcLaborEfficiencyReport(startUtc, endUtc, reportResult);

        return Collections.singletonList(reportResult);
    }

    @Override
    public ReportStatisticStrategyType getReportStatisticStrategyType() {
        return ReportStatisticStrategyType.PROC_LABOR_EFFICIENCY_MONTHLY;
    }

    @Override
    public void doPush(List<ProcLaborEfficiencyReportBo> procLaborEfficiencyReportBos) {
        doFeiShuPush(procLaborEfficiencyReportBos);
    }


    @Override
    public String getTableId() {
        return this.tableId;
    }

    @Override
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Override
    public String getAppToken() {
        return this.appToken;
    }

    @Override
    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    @Override
    public void doFeiShuPush(List<ProcLaborEfficiencyReportBo> reportResults) {
        List<Map<String, Object>> fields = feiShuOnlineSheetBaseService.parseObjectList(
                this.getReportStatisticStrategyType(), reportResults);

        FeiShuTablesBatchCreateDto feiShuTablesBatchCreateDto
                = FeiShuTableBuilder.buildFeiShuTablesBatchCreateDto(fields);
        mcRemoteService.batchCreateRecord(this.getAppToken(), this.getTableId(), feiShuTablesBatchCreateDto);
    }
}
