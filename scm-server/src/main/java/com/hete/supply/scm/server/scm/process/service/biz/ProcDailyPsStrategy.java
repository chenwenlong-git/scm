package com.hete.supply.scm.server.scm.process.service.biz;

import com.google.common.collect.Lists;
import com.hete.supply.mc.api.tables.entity.dto.FeiShuTablesBatchCreateDto;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.builder.FeiShuTableBuilder;
import com.hete.supply.scm.server.scm.enums.ReportStatisticStrategyType;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcPdReportDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcPsReportDto;
import com.hete.supply.scm.server.scm.service.base.FeiShuOnlineSheetBaseService;
import com.hete.supply.scm.server.scm.service.base.IFeiShuOnlineSheet;
import com.hete.supply.scm.server.scm.service.base.IReportStatisticStrategy;
import com.hete.supply.sda.api.scm.process.entity.vo.DailyPlatformShipmentVo;
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
 * @author yanjiawei
 * Created on 2024/6/19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcDailyPsStrategy implements IReportStatisticStrategy<DailyPlatformShipmentVo>, IFeiShuOnlineSheet<List<ProcPsReportDto>> {

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
    public DailyPlatformShipmentVo doStatistic() {
        LocalDateTime localDateTime = TimeUtil.convertZone(LocalDateTime.now().minusDays(1), TimeZoneId.UTC, TimeZoneId.CN);
        return sdaRemoteService.queryDailyShipments(localDateTime.toLocalDate());
    }

    @Override
    public DailyPlatformShipmentVo doStatistic(String dateTimeStr) {
        return sdaRemoteService.queryDailyShipments(LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Override
    public ReportStatisticStrategyType getReportStatisticStrategyType() {
        return ReportStatisticStrategyType.PROC_PLAT_PS_DAILY;
    }

    @Override
    public void doPush(DailyPlatformShipmentVo dailyPlatformShipmentVo) {
        long timestamp
                = dailyPlatformShipmentVo.getStoredDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        List<ProcPsReportDto> feishuReportDtoList = Lists.newArrayList();

        ProcPsReportDto limited = new ProcPsReportDto();
        limited.setPlatForm("limited");
        limited.setStoredNum(dailyPlatformShipmentVo.getLimited());
        feishuReportDtoList.add(limited);

        ProcPsReportDto weiChuangXin = new ProcPsReportDto();
        weiChuangXin.setPlatForm("微创新");
        weiChuangXin.setStoredNum(dailyPlatformShipmentVo.getWeiChuangXin());
        feishuReportDtoList.add(weiChuangXin);

        ProcPsReportDto b2b = new ProcPsReportDto();
        b2b.setPlatForm("B2B");
        b2b.setStoredNum(dailyPlatformShipmentVo.getB2b());
        feishuReportDtoList.add(b2b);

        ProcPsReportDto siChuang = new ProcPsReportDto();
        siChuang.setPlatForm("思创");
        siChuang.setStoredNum(dailyPlatformShipmentVo.getSiChuang());
        feishuReportDtoList.add(siChuang);

        ProcPsReportDto jiaozhi = new ProcPsReportDto();
        jiaozhi.setPlatForm("娇致");
        jiaozhi.setStoredNum(dailyPlatformShipmentVo.getJiaoZhi());
        feishuReportDtoList.add(jiaozhi);

        ProcPsReportDto amazon = new ProcPsReportDto();
        amazon.setPlatForm("亚马逊");
        amazon.setStoredNum(dailyPlatformShipmentVo.getAmazon());
        feishuReportDtoList.add(amazon);

        feishuReportDtoList.forEach(dto -> dto.setTimestamp(timestamp));
        doFeiShuPush(feishuReportDtoList);
    }

    @Override
    public void doFeiShuPush(List<ProcPsReportDto> feishuReportDtoList) {
        List<Map<String, Object>> fields
                = feiShuOnlineSheetBaseService.parseObjectList(this.getReportStatisticStrategyType(), feishuReportDtoList);
        FeiShuTablesBatchCreateDto feiShuTablesBatchCreateDto
                = FeiShuTableBuilder.buildFeiShuTablesBatchCreateDto(fields);
        mcRemoteService.batchCreateRecord(this.getAppToken(), this.getTableId(), feiShuTablesBatchCreateDto);
    }
}
