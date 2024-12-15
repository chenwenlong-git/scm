package com.hete.supply.scm.server.scm.process.service.biz;

import com.hete.supply.mc.api.tables.entity.dto.FeiShuTablesBatchCreateDto;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.builder.FeiShuTableBuilder;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import com.hete.supply.scm.server.scm.process.builder.ProcLaborEfficiencyReportBoBuilder;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcProcedureEfficiencyReportBo;
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
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcProcedureEfficiencyDailyStrategy implements IReportStatisticStrategy<List<ProcProcedureEfficiencyReportBo>>,
        IFeiShuOnlineSheet<List<ProcProcedureEfficiencyReportBo>> {

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
    public List<ProcProcedureEfficiencyReportBo> doStatistic() {
        return doStatistic("");
    }

    @Override
    public List<ProcProcedureEfficiencyReportBo> doStatistic(String dateTimeStr) {
        // 实现日度加工工序人效报表统计逻辑
        log.info("执行加工工序人效（当月）报表统计逻辑");

        List<LocalDate> localDateRange = processOrderReportBaseService.getLocalDateRange(dateTimeStr);
        List<ProcProcedureEfficiencyReportBo> reportResults
                = ProcLaborEfficiencyReportBoBuilder.buildProcProcedureEfficiencyReportBos(localDateRange);

        for (ProcProcedureEfficiencyReportBo reportResult : reportResults) {
            LocalDate yearMonthDay = reportResult.getYearMonthDay();
            LocalDateTime localDateTime = yearMonthDay.atTime(0, 0);
            long timestampMillis = localDateTime.toInstant(ZoneOffset.of("+8"))
                    .toEpochMilli();

            reportResult.setTimestamp(timestampMillis);

            ReportTimeRangeBo reportTimeRangeBo = processOrderReportBaseService.getReportTimeRangeBo(yearMonthDay);
            LocalDateTime startUtc = reportTimeRangeBo.getStartUtc();
            LocalDateTime endUtc = reportTimeRangeBo.getEndUtc();

            processOrderReportBizService.doCalProcProcedureEfficiencyReport(startUtc, endUtc, reportResult);
        }
        return reportResults;
    }

    @Override
    public ReportStatisticStrategyType getReportStatisticStrategyType() {
        return ReportStatisticStrategyType.PROC_PROCEDURE_EFFICIENCY_DAILY;
    }

    @Override
    public void doPush(List<ProcProcedureEfficiencyReportBo> procProcedureEfficiencyReportBos) {
        doFeiShuPush(procProcedureEfficiencyReportBos);
    }

    @Override
    public void doFeiShuPush(List<ProcProcedureEfficiencyReportBo> reportResults) {
        List<Map<String, Object>> fields = feiShuOnlineSheetBaseService.parseObjectList(
                this.getReportStatisticStrategyType(), reportResults);

        FeiShuTablesBatchCreateDto feiShuTablesBatchCreateDto = FeiShuTableBuilder.buildFeiShuTablesBatchCreateDto(
                fields);
        mcRemoteService.batchCreateRecord(this.getAppToken(), this.getTableId(), feiShuTablesBatchCreateDto);
    }
}

