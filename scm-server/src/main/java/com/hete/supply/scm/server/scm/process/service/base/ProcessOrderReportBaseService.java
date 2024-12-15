package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.process.entity.bo.ReportTimeRangeBo;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/3/14.
 */
@Service
@RequiredArgsConstructor
public class ProcessOrderReportBaseService {

    /**
     * 根据给定的时间字符串和维度，获取表示日期范围的LocalDate对象列表。
     *
     * @param cnTimeStr 时间字符串，格式为 "yyyy-MM-dd HH:mm:ss"。
     * @return 表示日期范围的LocalDate对象列表。
     */
    public List<LocalDate> getLocalDateRange(String cnTimeStr) {
        List<LocalDate> localDates = Lists.newArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime curCnTime = StrUtil.isNotBlank(cnTimeStr) ?
                LocalDateTime.parse(cnTimeStr, formatter) :
                TimeUtil.convertZone(LocalDateTime.now(), TimeZoneId.UTC, TimeZoneId.CN);
        LocalDateTime beforeCnTime = curCnTime.minusDays(1);

        if (StrUtil.isNotBlank(cnTimeStr)) {
            YearMonth yearMonth = YearMonth.from(beforeCnTime);
            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            for (LocalDate date = firstDayOfMonth; !date.isAfter(beforeCnTime.toLocalDate()); date = date.plusDays(1)) {
                localDates.add(date);
            }
        } else {
            localDates.add(beforeCnTime.toLocalDate());
        }
        return localDates;
    }

    /**
     * 根据给定的时间字符串和维度，获取表示月份范围的YearMonth对象列表。
     *
     * @param cnTimeStr 时间字符串，格式为 "yyyy-MM-dd HH:mm:ss"。
     * @return 表示月份范围的YearMonth对象列表。
     */
    public YearMonth getBeforeYearMonth(String cnTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime curCnTime = StrUtil.isNotBlank(cnTimeStr) ?
                LocalDateTime.parse(cnTimeStr, formatter) :
                TimeUtil.convertZone(LocalDateTime.now(), TimeZoneId.UTC, TimeZoneId.CN);
        YearMonth curYearMonth = YearMonth.from(curCnTime);

        return curYearMonth.minusMonths(1);
    }

    public BigDecimal calculatePercentage(int dividend,
                                          int divisor) {
        // 如果除数为0，直接返回0
        if (divisor == 0) {
            return BigDecimal.ZERO;
        }

        // 使用 BigDecimal 进行精确的除法计算
        BigDecimal dividendBigDecimal = BigDecimal.valueOf(dividend);
        BigDecimal divisorBigDecimal = BigDecimal.valueOf(divisor);

        // 计算百分比，并保留两位小数，四舍五入
        return dividendBigDecimal.divide(divisorBigDecimal, 2, RoundingMode.HALF_UP);
    }

    /**
     * 根据给定的日期计算当天的开始和结束时间（UTC 时间）
     *
     * @param yearMonthDay 给定的日期
     * @return TimeRange 对象，包含当天的开始和结束时间（UTC 时间）
     */
    public ReportTimeRangeBo getReportTimeRangeBo(LocalDate yearMonthDay) {
        LocalDateTime startUtc = ScmTimeUtil.toDailyStartUtc(yearMonthDay);
        LocalDateTime endUtc = ScmTimeUtil.toDailyEndUtc(yearMonthDay);
        return new ReportTimeRangeBo(startUtc, endUtc);
    }

    /**
     * 根据给定的年月计算当月的开始和结束时间（UTC 时间）
     *
     * @param yearMonth 给定的年月
     * @return TimeRange 对象，包含当月的开始和结束时间（UTC 时间）
     */
    public ReportTimeRangeBo getReportTimeRangeBo(YearMonth yearMonth) {
        LocalDateTime startUtc = ScmTimeUtil.toMonthlyStartUtc(yearMonth);
        LocalDateTime endUtc = ScmTimeUtil.toMonthlyEndUtc(yearMonth);
        return new ReportTimeRangeBo(startUtc, endUtc);
    }

}
