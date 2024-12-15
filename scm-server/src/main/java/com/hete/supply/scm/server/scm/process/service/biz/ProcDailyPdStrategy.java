package com.hete.supply.scm.server.scm.process.service.biz;

import com.google.common.collect.Lists;
import com.hete.supply.mc.api.tables.entity.dto.FeiShuTablesBatchCreateDto;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.builder.FeiShuTableBuilder;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcPdReportDto;
import com.hete.supply.scm.server.scm.service.base.FeiShuOnlineSheetBaseService;
import com.hete.supply.scm.server.scm.service.base.IFeiShuOnlineSheet;
import com.hete.supply.scm.server.scm.service.base.IReportStatisticStrategy;
import com.hete.supply.sda.api.scm.process.entity.vo.DailyPlatformDemandVo;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 各平台加工需求（当月）
 *
 * @author yanjiawei
 * Created on 2024/6/19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcDailyPdStrategy implements IReportStatisticStrategy<DailyPlatformDemandVo>, IFeiShuOnlineSheet<List<ProcPdReportDto>> {

    private String appToken;
    private String tableId;

    private final McRemoteService mcRemoteService;
    private final SdaRemoteService sdaRemoteService;
    private final FeiShuOnlineSheetBaseService feiShuOnlineSheetBaseService;

    @Override
    public void initFeiShuOnlineSheetConfig(String appToken, String tableId) {
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
    public DailyPlatformDemandVo doStatistic() {
        LocalDateTime localDateTime = TimeUtil.convertZone(LocalDateTime.now().minusDays(1), TimeZoneId.UTC, TimeZoneId.CN);
        return sdaRemoteService.queryDailyDemandForPlatforms(localDateTime.toLocalDate());
    }

    @Override
    public DailyPlatformDemandVo doStatistic(String dateTimeStr) {
        return sdaRemoteService.queryDailyDemandForPlatforms(LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Override
    public ReportStatisticStrategyType getReportStatisticStrategyType() {
        return ReportStatisticStrategyType.PROC_PLAT_PD_DAILY;
    }

    @Override
    public void doPush(DailyPlatformDemandVo dailyPlatformDemandVo) {
        long timestamp
                = dailyPlatformDemandVo.getCreateDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        List<ProcPdReportDto> feishuReportDtoList = Lists.newArrayList();

        ProcPdReportDto limited = new ProcPdReportDto();
        limited.setPlatForm("limited");
        limited.setProcessNum(dailyPlatformDemandVo.getLimited());
        feishuReportDtoList.add(limited);

        ProcPdReportDto weiChuangXin = new ProcPdReportDto();
        weiChuangXin.setPlatForm("微创新");
        weiChuangXin.setProcessNum(dailyPlatformDemandVo.getWeiChuangXin());
        feishuReportDtoList.add(weiChuangXin);

        ProcPdReportDto b2b = new ProcPdReportDto();
        b2b.setPlatForm("B2B");
        b2b.setProcessNum(dailyPlatformDemandVo.getB2b());
        feishuReportDtoList.add(b2b);

        ProcPdReportDto siChuang = new ProcPdReportDto();
        siChuang.setPlatForm("思创");
        siChuang.setProcessNum(dailyPlatformDemandVo.getSiChuang());
        feishuReportDtoList.add(siChuang);

        ProcPdReportDto jiaozhi = new ProcPdReportDto();
        jiaozhi.setPlatForm("娇致");
        jiaozhi.setProcessNum(dailyPlatformDemandVo.getJiaoZhi());
        feishuReportDtoList.add(jiaozhi);

        ProcPdReportDto amazon = new ProcPdReportDto();
        amazon.setPlatForm("亚马逊");
        amazon.setProcessNum(dailyPlatformDemandVo.getAmazon());
        feishuReportDtoList.add(amazon);

        feishuReportDtoList.forEach(dto -> dto.setTimestamp(timestamp));
        doFeiShuPush(feishuReportDtoList);
    }

    @Override
    public void doFeiShuPush(List<ProcPdReportDto> feishuReportDtoList) {
        List<Map<String, Object>> fields
                = feiShuOnlineSheetBaseService.parseObjectList(this.getReportStatisticStrategyType(), feishuReportDtoList);
        FeiShuTablesBatchCreateDto feiShuTablesBatchCreateDto
                = FeiShuTableBuilder.buildFeiShuTablesBatchCreateDto(fields);
        mcRemoteService.batchCreateRecord(this.getAppToken(), this.getTableId(), feiShuTablesBatchCreateDto);
    }
}
