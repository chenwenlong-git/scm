package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.util.StrUtil;
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

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcProcedureEfficiencyMonthStrategy implements IReportStatisticStrategy<List<ProcProcedureEfficiencyReportBo>>,
        IFeiShuOnlineSheet<List<ProcProcedureEfficiencyReportBo>> {
    private String appToken;
    private String tableId;


    private final ProcessOrderReportBizService processOrderReportBizService;
    private final ProcessOrderReportBaseService processOrderReportBaseService;
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
        // 实现月度加工工序人效报表统计逻辑
        log.info("执行加工工序人效（年）报表统计逻辑");

        YearMonth yearMonth = processOrderReportBaseService.getBeforeYearMonth(dateTimeStr);
        ProcProcedureEfficiencyReportBo reportResult
                = ProcLaborEfficiencyReportBoBuilder.buildProcProcedureEfficiencyReportBo(yearMonth);
        reportResult.setDateStr(StrUtil.format("{}年{}月", yearMonth.getYear(), yearMonth.getMonth()
                .getValue()));

        ReportTimeRangeBo reportTimeRangeBo = processOrderReportBaseService.getReportTimeRangeBo(yearMonth);
        LocalDateTime startUtc = reportTimeRangeBo.getStartUtc();
        LocalDateTime endUtc = reportTimeRangeBo.getEndUtc();
        processOrderReportBizService.doCalProcProcedureEfficiencyReport(startUtc, endUtc, reportResult);

        return Collections.singletonList(reportResult);
    }

    @Override
    public ReportStatisticStrategyType getReportStatisticStrategyType() {
        return ReportStatisticStrategyType.PROC_PROCEDURE_EFFICIENCY_MONTHLY;
    }

    @Override
    public void doPush(List<ProcProcedureEfficiencyReportBo> procProcedureEfficiencyReportBos) {
        doFeiShuPush(procProcedureEfficiencyReportBos);
    }

    @Override
    public void doFeiShuPush(List<ProcProcedureEfficiencyReportBo> reportResults) {
        List<Map<String, Object>> fields = feiShuOnlineSheetBaseService.parseObjectList(
                this.getReportStatisticStrategyType(), reportResults);
        FeiShuTablesBatchCreateDto feiShuTablesBatchCreateDto
                = FeiShuTableBuilder.buildFeiShuTablesBatchCreateDto(fields);
        mcRemoteService.batchCreateRecord(this.getAppToken(), this.getTableId(), feiShuTablesBatchCreateDto);
    }
}
