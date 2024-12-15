package com.hete.supply.scm.common.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.hete.supply.scm.server.scm.entity.bo.TimeRangeBo;
import com.hete.supply.scm.server.scm.entity.bo.WorkDurationBo;
import com.hete.supply.scm.server.scm.enums.EndDelayStatus;
import com.hete.supply.scm.server.scm.enums.StartDelayStatus;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * 时间处理工具
 *
 * @author weiwenxin
 * @date 2022/12/10 17:38
 */
@Validated
@Slf4j
public class ScmTimeUtil {

    public static void checkDateBeforeNow(@NotEmpty List<LocalDateTime> dateList) {
        final LocalDateTime now = DateUtil.beginOfDay(new Date())
                .toLocalDateTime();
        dateList.forEach(date -> {
            if (date.isBefore(now)) {
                throw new BizException("选择的时间不能小于当前时间");
            }
        });
    }

    public static void checkDateAfterNow(@NotEmpty List<LocalDateTime> dateList) {
        final LocalDateTime now = DateUtil.endOfDay(new Date())
                .toLocalDateTime();
        dateList.forEach(date -> {
            if (date.isAfter(now)) {
                throw new BizException("选择的时间不能大于当前时间");
            }
        });
    }

    /**
     * 获取上个月的第1天
     * eg.当前是2023-02-15 15:07:55
     * 则返回值是 2023-01-01T00:00
     *
     * @return
     */
    public static LocalDateTime getStartOfLastMonth() {
        final LocalDateTime now = TimeUtil.now(TimeZoneId.CN);

        return now.minusMonths(1)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取上个月的第1天
     * eg.当前是2023-02-15 15:07:55
     * 则返回值是 2023-01-31T23:59:59.999999999
     *
     * @return
     */
    public static LocalDateTime getEndOfLastMonth() {
        final LocalDateTime now = TimeUtil.now(TimeZoneId.CN);

        return now.withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .minusNanos(1);
    }

    /**
     * 通过字符串转换获取上个月的第1天
     *
     * @param yearMonthString 格式：yyyy-MM
     * @return LocalDateTime
     * @author ChenWenLong
     * @date 2023/6/5 17:25
     */
    public static LocalDateTime getStartOfLastMonthByString(@NotBlank String yearMonthString) {
        if (!yearMonthString.matches("\\d{4}-\\d{2}")) {
            throw new BizException("时间字符串参数不符合格式 yyyy-MM");
        }
        YearMonth yearMonth = YearMonth.parse(yearMonthString);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        return LocalDateTime.of(firstDayOfMonth, LocalTime.MIDNIGHT);
    }

    /**
     * 通过字符串转换获取上个月的最后1天
     *
     * @param yearMonthString 格式：yyyy-MM
     * @return LocalDateTime
     * @author ChenWenLong
     * @date 2023/6/5 17:56
     */
    public static LocalDateTime getEndOfLastMonthByString(@NotBlank String yearMonthString) {
        if (!yearMonthString.matches("\\d{4}-\\d{2}")) {
            throw new BizException("时间字符串参数不符合格式 yyyy-MM");
        }
        YearMonth yearMonth = YearMonth.parse(yearMonthString);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        return lastDayOfMonth.atTime(LocalTime.MAX);
    }

    /**
     * LocalDateTime类型转String
     *
     * @param localDateTime
     * @param timeZoneId
     * @param datePattern
     * @return
     */
    public static String localDateTimeToStr(LocalDateTime localDateTime,
                                            @NotNull TimeZoneId timeZoneId,
                                            @NotBlank String datePattern) {
        if (null == localDateTime) {
            return "";
        }

        final LocalDateTime cnLocalDateTime = TimeUtil.utcConvertZone(localDateTime, timeZoneId);
        // 将本地日期时间转换为ZonedDateTime对象
        final ZonedDateTime zonedDateTime = TimeUtil.zoned(cnLocalDateTime, timeZoneId);
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        // 将ZonedDateTime对象转换为字符串
        return zonedDateTime.format(formatter);

    }

    public static String localDateTimeToStr(LocalDateTime localDateTime, @NotNull TimeZoneId timeZoneId, @NotNull DateTimeFormatter formatter) {
        if (null == localDateTime) {
            return "";
        }

        final LocalDateTime cnLocalDateTime = TimeUtil.utcConvertZone(localDateTime, timeZoneId);
        final ZonedDateTime zonedDateTime = TimeUtil.zoned(cnLocalDateTime, timeZoneId);
        return zonedDateTime.format(formatter);
    }

    /**
     * 字符串转LocalDateTime
     *
     * @param timeString
     * @param datePattern
     * @return
     */
    public static LocalDateTime dateStrToLocalDateTime(@NotBlank String timeString,
                                                       @NotBlank String datePattern) {
        if (StringUtils.isEmpty(timeString)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return LocalDateTime.parse(timeString, formatter);
    }

    /**
     * 获取该日期的当天最后一秒
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getLastSecondTimeOfDayForTime(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        return TimeUtil.convertZone(localDateTime.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0), TimeZoneId.CN, TimeZoneId.UTC);
    }

    /**
     * 判断两个时间段是否存在冲突。
     *
     * @param start1 第一个时间段的起始时间。
     * @param end1   第一个时间段的结束时间。
     * @param start2 第二个时间段的起始时间。
     * @param end2   第二个时间段的结束时间。
     * @return 如果两个时间段存在重叠冲突，则返回 true；否则返回 false。
     */
    public static boolean isTimeSlotConflict(LocalDateTime start1,
                                             LocalDateTime end1,
                                             LocalDateTime start2,
                                             LocalDateTime end2) {
        if (Objects.isNull(start1)) {
            throw new ParamIllegalException("start1 is null");
        }
        if (Objects.isNull(end1)) {
            throw new ParamIllegalException("end1 is null");
        }
        if (Objects.isNull(start2)) {
            throw new ParamIllegalException("start2 is null");
        }
        if (Objects.isNull(end2)) {
            throw new ParamIllegalException("end2 is null");
        }
        if (start1.isAfter(end1) || start2.isAfter(end2)) {
            throw new ParamIllegalException("Invalid time range");
        }
        return end1.isAfter(start2) && end2.isAfter(start1);
    }


    /**
     * 判断两个时间段是否有交集
     *
     * @param start1 第一个时间段的起始时间
     * @param end1   第一个时间段的终止时间
     * @param start2 第二个时间段的起始时间
     * @param end2   第二个时间段的终止时间
     * @return true表示两个时间段有交集，false表示两个时间段没有交集
     */
    public static boolean isTimeOverlap(LocalTime start1,
                                        LocalTime end1,
                                        LocalTime start2,
                                        LocalTime end2) {
        if (start1.isAfter(end1) || start2.isAfter(end2)) {
            throw new IllegalArgumentException("Invalid time range");
        }
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    /**
     * 计算结束时间，确保耗时的时间不会处于中午12:00到13:30这段休息时间中
     *
     * @param startTime 起始时间
     * @param duration  耗时
     * @return 终止时间
     */
    public static LocalDateTime calculateEndTime(LocalDateTime startTime,
                                                 Duration duration,
                                                 int h1,
                                                 int m1,
                                                 int h2,
                                                 int m2) {
        // 如果耗时的时间跨过了中午12:00到13:30，则需要将耗时的时间段移动到中午13:30之后
        LocalTime lunchStart = LocalTime.of(h1, m1);
        LocalTime lunchEnd = LocalTime.of(h2, m2);
        LocalDateTime endTime = startTime.plus(duration);
        LocalTime endTimeOfDay = endTime.toLocalTime();
        if (isTimeOverlap(lunchStart, lunchEnd, startTime.toLocalTime(), endTimeOfDay)) {
            // 耗时的时间段处于中午12:00到13:30之间，需要将其移动到中午13:30之后
            Duration lunchDuration = Duration.between(lunchStart, lunchEnd);
            endTime = endTime.plus(lunchDuration);
        }

        return endTime;
    }

    /**
     * 将小时转换为向上取整的10分钟间隔的分钟数
     *
     * @param hours 耗时（小时）
     * @return 分钟数
     */
    public static int convertToMinutes(BigDecimal hours) {
        // 将小时转换为分钟
        int minutes = hours.multiply(BigDecimal.valueOf(60))
                .intValue();
        if (minutes % 60 == 0) {
            return minutes;
        } else {
            return (minutes / 10 + 1) * 10;
        }
    }

    /**
     * 校验结束时间是否延期
     *
     * @param actualEndTime
     * @param expectedEndTime
     * @return
     */
    public static EndDelayStatus checkEndDelay(LocalDateTime actualEndTime,
                                               LocalDateTime expectedEndTime) {
        if (actualEndTime != null) {
            if (actualEndTime.isAfter(expectedEndTime)) {
                return EndDelayStatus.DELAYED;
            } else {
                return EndDelayStatus.ON_TIME;
            }
        } else {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isAfter(expectedEndTime)) {
                return EndDelayStatus.DELAYED;
            } else {
                return EndDelayStatus.ON_TIME;
            }
        }
    }

    /**
     * 校验开始时间是否延期
     *
     * @param actualStartTime
     * @param expectedStartTime
     * @return
     */
    public static StartDelayStatus checkStartDelay(LocalDateTime actualStartTime,
                                                   LocalDateTime expectedStartTime) {
        if (actualStartTime != null) {
            if (actualStartTime.isAfter(expectedStartTime)) {
                return StartDelayStatus.DELAYED;
            } else {
                return StartDelayStatus.ON_TIME;
            }
        } else {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isAfter(expectedStartTime)) {
                return StartDelayStatus.DELAYED;
            } else {
                return StartDelayStatus.ON_TIME;
            }
        }
    }

    /**
     * 根据计划开始时间和结束时间计算每日耗时
     *
     * @param planStart
     * @param planEnd
     * @param workStart
     * @param workEnd
     * @param lunchStart
     * @param lunchEnd
     * @param holidays
     */
    public static TreeSet<WorkDurationBo> calculateDailyWorkDuration(LocalDateTime planStart,
                                                                     LocalDateTime planEnd,
                                                                     LocalTime workStart,
                                                                     LocalTime workEnd,
                                                                     LocalTime lunchStart,
                                                                     LocalTime lunchEnd,
                                                                     Set<LocalDate> holidays) {
        LocalDate currentDate = planStart.toLocalDate();
        LocalDateTime currentDateTime = planStart;
        TreeSet<WorkDurationBo> workDurationBos = new TreeSet<>(Comparator.comparing(WorkDurationBo::getDate));

        while (!currentDate.isAfter(planEnd.toLocalDate())) {
            if (!isHoliday(currentDateTime.toLocalDate(), holidays)) {
                LocalDateTime workDayStart = currentDate.atTime(workStart);
                LocalDateTime workDayEnd = currentDate.atTime(workEnd);

                if (currentDate.equals(planStart.toLocalDate())) {
                    workDayStart = planStart;
                }
                if (currentDate.equals(planEnd.toLocalDate())) {
                    workDayEnd = planEnd;
                }

                if (workDayStart.isBefore(workDayEnd)) {
                    Duration totalWorkDuration = Duration.between(workDayStart, workDayEnd);
                    if (workDayStart.toLocalTime()
                            .isBefore(lunchEnd) && workDayEnd.toLocalTime()
                            .isAfter(lunchStart)) {
                        totalWorkDuration = totalWorkDuration.minus(Duration.between(lunchStart, lunchEnd));
                        workDurationBos.add(new WorkDurationBo(currentDate, totalWorkDuration));
                    } else {
                        workDurationBos.add(new WorkDurationBo(currentDate, totalWorkDuration));
                    }
                }
            }
            currentDateTime = currentDateTime.plusDays(1);
            currentDate = currentDateTime.toLocalDate();
        }
        return workDurationBos;
    }

    /**
     * 判断给定日期是否为节假日。
     *
     * @param date     要判断的日期。
     * @param holidays 节假日的集合。
     * @return 如果给定日期是节假日，则返回 true；否则返回 false。
     */
    public static boolean isHoliday(LocalDate date,
                                    Set<LocalDate> holidays) {
        return holidays.contains(date);
    }


    /**
     * 计算给定的开始时间之后经过指定的小时数和分钟数后的结束时间，考虑工作时间和节假日。
     *
     * @param startTime       开始时间，作为计算的起始点。
     * @param durationHours   持续的小时数。
     * @param durationMinutes 持续的分钟数。
     * @param timeRanges      工作时间范围的集合，按照起始时间排序。
     * @param holidays        节假日的集合。
     * @return 计算得到的结束时间的 LocalDateTime。
     */
    public static LocalDateTime calculateEndTime(LocalDateTime startTime,
                                                 long durationHours,
                                                 long durationMinutes,
                                                 TreeSet<TimeRangeBo> timeRanges,
                                                 Set<LocalDate> holidays) {
        LocalDateTime endTime = startTime;
        long remainingHours = durationHours;
        long remainingMinutes = durationMinutes;

        while (remainingHours > 0 || remainingMinutes > 0) {
            endTime = endTime.plusMinutes(1);

            // 跳过非工作时间
            if (!isWorkingTime(endTime.toLocalTime(), timeRanges)) {
                continue;
            }

            // 跳过节假日
            if (isHoliday(endTime.toLocalDate(), holidays)) {
                continue;
            }

            remainingMinutes--;
            if (remainingMinutes == -1) {
                remainingHours--;
                remainingMinutes = 59;
            }
        }
        return adjustToNextFiveMinutes(endTime);
    }

    /**
     * 判断给定的时间是否处于工作时间范围内。
     *
     * @param targetTime     要判断的时间。
     * @param workTimeRanges 工作时间范围的集合，按照起始时间排序。
     * @return 如果给定时间在任何一个工作时间范围内，则返回 true；否则返回 false。
     */
    public static boolean isWorkingTime(LocalTime targetTime,
                                        TreeSet<TimeRangeBo> workTimeRanges) {
        for (TimeRangeBo workTimeRange : workTimeRanges) {
            if (targetTime.compareTo(workTimeRange.getStartTime()) >= 0 && targetTime.compareTo(
                    workTimeRange.getEndTime()) <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据上班时间、结束时间、多段休息时间段获取工作时间
     *
     * @param workStartTime
     * @param workEndTime
     * @param breakTimeRanges
     * @return
     */
    public static TreeSet<TimeRangeBo> calculateWorkTime(LocalTime workStartTime,
                                                         LocalTime workEndTime,
                                                         List<TimeRangeBo> breakTimeRanges) {
        TreeSet<TimeRangeBo> workTimeRanges = new TreeSet<>(Comparator.comparing(TimeRangeBo::getStartTime));

        LocalTime currentTime = workStartTime;
        for (TimeRangeBo breakTime : breakTimeRanges) {
            if (breakTime.getStartTime()
                    .isAfter(currentTime)) {
                workTimeRanges.add(new TimeRangeBo(currentTime, breakTime.getStartTime()));
            }
            currentTime = breakTime.getEndTime();
        }

        if (workEndTime.isAfter(currentTime)) {
            workTimeRanges.add(new TimeRangeBo(currentTime, workEndTime));
        }
        return workTimeRanges;
    }

    /**
     * 计算下一道工序开始时间。
     *
     * @param currentStartTime       当前工序的开始时间
     * @param processDurationMinutes 工序预计时长（分钟）
     * @param holidays               节假日集合，包含需要跳过的日期
     * @param workTimeRanges         多段工作时间范围列表
     * @return 下一道工序的开始时间
     */
    public static LocalDateTime calculateNextStartTime(LocalDateTime currentStartTime,
                                                       int processDurationMinutes,
                                                       Set<LocalDate> holidays,
                                                       TreeSet<TimeRangeBo> workTimeRanges) {
        LocalDateTime endTime = currentStartTime.plusMinutes(processDurationMinutes);

        // 跳过假期
        while (isHoliday(endTime.toLocalDate(), holidays)) {
            endTime = endTime.plus(1, ChronoUnit.DAYS);
        }
        return getNextWorkDateTime(endTime, workTimeRanges);
    }

    /**
     * 获取给定时间之后的下一个工作时间。
     *
     * @param dateTime       给定的时间，用于确定起始点。
     * @param workTimeRanges 工作时间范围的集合，按照起始时间排序。
     * @return 下一个工作时间的 LocalDateTime。
     */
    public static LocalDateTime getNextWorkDateTime(LocalDateTime dateTime,
                                                    TreeSet<TimeRangeBo> workTimeRanges) {
        while (true) {
            LocalTime time = dateTime.toLocalTime();
            for (TimeRangeBo workTimeRange : workTimeRanges) {
                if (time.isBefore(workTimeRange.getStartTime())) {
                    return dateTime.with(workTimeRange.getStartTime());
                } else if (time.isBefore(workTimeRange.getEndTime())) {
                    return dateTime;
                }
            }
            TimeRangeBo timeRange = workTimeRanges.stream()
                    .findFirst()
                    .orElse(null);
            if (Objects.isNull(timeRange)) {
                throw new BizException("工作时间段为空，请检查相关配置");
            }
            dateTime = dateTime.plus(1, ChronoUnit.DAYS)
                    .with(timeRange.getStartTime());
        }
    }

    /**
     * @Description 进位到下一个5分钟
     * @author yanjiawei
     * @Date 2023/8/28 11:09
     */
    public static LocalDateTime adjustToNextFiveMinutes(LocalDateTime dateTime) {
        int minute = dateTime.get(ChronoField.MINUTE_OF_HOUR);
        int remainder = minute % 5;
        int adjustedMinute = minute + (5 - remainder) % 5;

        if (adjustedMinute >= 60) {
            int extraHours = adjustedMinute / 60;
            adjustedMinute = adjustedMinute % 60;
            dateTime = dateTime.plusHours(extraHours)
                    .withMinute(adjustedMinute);
        } else {
            dateTime = dateTime.withMinute(adjustedMinute);
        }
        return dateTime;
    }


    /**
     * 将给定的 LocalDateTime 对象的时分秒部分设置为当天的结束时间（23:59:59.999999999）。
     * 如果输入的 LocalDateTime 为 null，则抛出 ParamIllegalException 异常，提示“初始化日期失败，日期为空！”。
     *
     * @param inputDateTime 要处理的 LocalDateTime 对象，不能为 null。
     * @return 处理后的 LocalDateTime 对象，时分秒部分被设置为当天的结束时间。
     * @throws ParamIllegalException 如果输入的 LocalDateTime 为 null。
     */
    public static LocalDateTime setToEndOfDay(LocalDateTime inputDateTime) {
        if (Objects.isNull(inputDateTime)) {
            throw new ParamIllegalException("初始化日期23:59:59失败，日期为空！");
        }
        LocalDateTime localDateTime = inputDateTime.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);
        return TimeUtil.convertZone(localDateTime, TimeZoneId.CN, TimeZoneId.UTC);
    }

    /**
     * 计算从指定时间到现在的时长（天数），结果向上取整。
     *
     * @param createTime 指定时间
     * @return 时长（天数）
     */
    public static long calculateDuration(LocalDateTime createTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createTime, now);
        long days = duration.toDays();

        // 如果有不满一天的时间，向上取整
        if (duration.toNanos() % ChronoUnit.DAYS.getDuration()
                .toNanos() > 0) {
            days++;
        }

        return days;
    }

    /**
     * 获取本月的第1天
     * eg.当前是2023-02-15 15:07:55
     * 则返回值是 2023-01-01T00:00
     *
     * @return
     */
    public static LocalDateTime getStartOfCurrentMonth() {
        final LocalDateTime now = TimeUtil.now(TimeZoneId.CN);
        return now.withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取指定月份的第一天的 LocalDateTime 对象
     *
     * @param month 月份（1 到 12）
     * @return 指定月份的第一天的 LocalDateTime 对象
     */
    public static LocalDateTime getStartOfMonth(int month) {
        // 获取当前日期时间
        final LocalDateTime now = LocalDateTime.now();
        // 获取指定月份的第一天
        return now.withMonth(month)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取指定月份的最后一天的 LocalDateTime 对象
     *
     * @param month 月份（1 到 12）
     * @return 指定月份的最后一天的 LocalDateTime 对象
     */
    public static LocalDateTime getEndOfMonth(int month) {
        // 获取当前日期时间
        final LocalDateTime now = LocalDateTime.now();

        // 获取指定月份的最后一天
        return now.withMonth(month)
                .withDayOfMonth(now.getMonth()
                        .length(now.toLocalDate()
                                .isLeapYear()))
                .withHour(23)
                .withMinute(59)
                .withSecond(59);
    }

    public static boolean isWithinTimeRange(LocalDateTime targetTime,
                                            LocalDateTime startTime,
                                            LocalDateTime endTime) {
        return targetTime.isAfter(startTime) && targetTime.isBefore(endTime);
    }

    /**
     * 获取n天前的时间字符串，yyyy-MM-dd
     *
     * @return
     */
    public static String getBeforeDateString(int n) {
        // 获取昨天的日期
        final LocalDate now = TimeUtil.now(TimeZoneId.CN)
                .toLocalDate();
        LocalDate yesterday = now.minusDays(n);

        // 格式化为指定的日期字符串格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return yesterday.format(formatter);
    }

    /**
     * 获取本月的时间字符串yyyy-MM
     *
     * @return
     */
    public static String getCurrentMonthString() {
        // 获取当前月份
        LocalDate currentDate = LocalDate.now();

        // 格式化为指定的日期字符串格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return currentDate.format(formatter);
    }

    /**
     * 获取指定的时间字符串yyyy-MM
     *
     * @param currentDateTime:
     * @return String
     * @author ChenWenLong
     * @date 2024/5/29 14:46
     */
    public static String getSpecifyMonthString(@NotNull LocalDateTime currentDateTime) {
        // 格式化为指定的日期字符串格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return currentDateTime.format(formatter);
    }

    /**
     * 获取月份格式
     *
     * @param currentDate
     * @return
     */
    public static String getMonthString(LocalDateTime currentDate) {

        // 格式化为指定的日期字符串格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return currentDate.format(formatter);
    }

    /**
     * 获取上个月最后一天的时间字符串,yyyy-MM-dd
     *
     * @return
     */
    public static String getLastDayOfPreviousMonth() {

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 获取上个月的日期
        LocalDate previousMonth = currentDate.minusMonths(1);

        // 获取上个月的最后一天
        LocalDate lastDayOfPreviousMonth = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());

        // 格式化日期为 "yyyy-MM-dd" 格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return lastDayOfPreviousMonth.format(formatter);
    }

    /**
     * 检查给定的日期字符串是否符合指定的日期格式。
     *
     * @param dateStr    要检查的日期字符串
     * @param dateFormat 日期格式字符串，例如 "yyyy-MM-dd"
     * @return 如果日期字符串符合指定的日期格式，则返回 true；否则返回 false
     */
    public static boolean isValidDateFormat(String dateStr,
                                            String dateFormat) {
        // 创建一个 DateTimeFormatter 对象来解析日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        try {
            // 尝试解析输入的字符串
            formatter.parse(dateStr);
            // 如果解析成功，返回true
            return true;
        } catch (DateTimeParseException e) {
            // 如果解析失败，捕获异常并返回false
            log.info("isValidDateFormat fail ", e);
            return false;
        }
    }


    /**
     * 获取n天后的日期
     *
     * @param n
     * @return
     */
    public static LocalDateTime getAfterDate(long n) {
        final LocalDateTime now = TimeUtil.now(TimeZoneId.CN);

        return now.plusDays(n).with(LocalTime.MIDNIGHT);
    }

    /**
     * 获取n月后的日期
     *
     * @param n
     * @return
     */
    public static LocalDateTime getAfterMonthDate(long n) {
        final LocalDateTime now = TimeUtil.now(TimeZoneId.CN);

        return now.plusMonths(n).with(LocalTime.MIDNIGHT);
    }

    /**
     * 根据入参字符串获取该字符串前一天的时间字符串
     *
     * @param dateStr
     * @return
     */
    public static String getYesterdayStrByDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        // 解析时间字符串为LocalDate对象
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

        // 减去一天得到前一天的日期
        LocalDate previousDate = date.minusDays(1);

        // 格式化前一天的日期为字符串
        return previousDate.format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * 根据日期字符串获取该日期对应的月度时间字符串
     *
     * @param dateStr
     * @return
     */
    public static String getMonthStrByDateStr(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        // 解析时间字符串为LocalDate对象
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

        // 获取年月部分的字符串
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    /**
     * 将指定日期的开始时间转换为 UTC 时间。
     *
     * @param yearMonthDay 指定日期
     * @return UTC 时间
     */
    public static LocalDateTime toDailyStartUtc(LocalDate yearMonthDay) {
        // 转换年月日日期为 UTC 时间
        return TimeUtil.convertZone(yearMonthDay.atStartOfDay(), TimeZoneId.CN, TimeZoneId.UTC);
    }

    /**
     * 将指定日期的结束时间转换为 UTC 时间。
     *
     * @param yearMonthDay 指定日期
     * @return UTC 时间
     */
    public static LocalDateTime toDailyEndUtc(LocalDate yearMonthDay) {
        // 将时间设置为当天最后一刻并转换为 UTC 时间
        return TimeUtil.convertZone(yearMonthDay.atTime(LocalTime.MAX), TimeZoneId.CN, TimeZoneId.UTC);
    }

    /**
     * 将指定年月的开始时间转换为 UTC 时间。
     *
     * @param yearMonth 指定年月
     * @return UTC 时间
     */
    public static LocalDateTime toMonthlyStartUtc(YearMonth yearMonth) {
        // 获取月份的第一天并转换为 UTC 时间
        return TimeUtil.convertZone(yearMonth.atDay(1).atStartOfDay(), TimeZoneId.CN, TimeZoneId.UTC);
    }

    /**
     * 将指定年月的结束时间转换为 UTC 时间。
     *
     * @param yearMonth 指定年月
     * @return UTC 时间
     */
    public static LocalDateTime toMonthlyEndUtc(YearMonth yearMonth) {
        // 获取月份的最后一天并设置为当天最后一刻，并转换为 UTC 时间
        return TimeUtil.convertZone(yearMonth.atEndOfMonth().atTime(23, 59, 59), TimeZoneId.CN, TimeZoneId.UTC);
    }

    /**
     * 获取该时间字符串n天后的时间字符串
     *
     * @param costTime
     * @param n
     * @return
     */
    public static String getAfterDateString(String costTime, int n) {
        LocalDate date = LocalDate.parse(costTime, DateTimeFormatter.ISO_LOCAL_DATE);

        // 获取前一天日期
        LocalDate previousDay = date.plusDays(n);

        // 格式化为字符串
        return previousDay.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 判断两个LocalDateTime都有值时判断不相等返回TRUE（空值为不相等）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean determineLocalDateTimeEqual(LocalDateTime startTime,
                                                      LocalDateTime endTime) {
        return startTime != null && endTime != null && !startTime.equals(endTime);
    }

    /**
     * 根据开始时间和结束时间返回202X年0X月或第几周
     *
     * @param start:
     * @param end:
     * @return String
     * @author ChenWenLong
     * @date 2024/5/24 17:54
     */
    public static String getTimeChangeName(@NotNull LocalDateTime start,
                                           @NotNull LocalDateTime end) {
        // 转换为 LocalDate
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        if (startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth()) {
            // 如果在同一个月内
            return startDate.getYear() + "年" + startDate.getMonthValue() + "月";
        } else {
            // 计算以 start 时间为基准的周数
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int startWeek = startDate.get(weekFields.weekOfWeekBasedYear());
            int startYear = startDate.getYear();
            // 如果跨周
            return startYear + "年第" + startWeek + "周";
        }
    }

    /**
     * 计算两个LocalDateTime之间的天数
     *
     * @param start:
     * @param end:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/5/24 18:46
     */
    public static Integer calculateDaysBetween(@NotNull LocalDateTime start,
                                               @NotNull LocalDateTime end) {
        // 计算两个 LocalDateTime 之间的天数，并包括结束日期
        return Math.toIntExact(ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate()) + 1);
    }

    /**
     * 将时间字符串转为时间
     *
     * @param timeString:
     * @return LocalDateTime
     * @author ChenWenLong
     * @date 2024/6/3 11:04
     */
    public static LocalDateTime strToLocalDateTime(@NotBlank String timeString) {
        if (StringUtils.isEmpty(timeString)) {
            return null;
        }
        // 将字符串转换为 LocalDate
        LocalDate date = LocalDate.parse(timeString, DateTimeFormatter.ISO_LOCAL_DATE);
        // 将 LocalDate 转换为 LocalDateTime，设置时间为当天的开始时间 00:00
        return date.atStartOfDay();
    }

    /**
     * 获取两个日期中的最小日期
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 最小的日期
     */
    public static LocalDate min(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) {
            throw new IllegalArgumentException("Both dates cannot be null");
        }
        if (date1 == null) {
            return date2;
        }
        if (date2 == null) {
            return date1;
        }
        return date1.isBefore(date2) ? date1 : date2;
    }

    /**
     * 获取两个日期时间中的最小日期时间
     *
     * @param dateTime1 第一个日期时间
     * @param dateTime2 第二个日期时间
     * @return 最小的日期时间
     */
    public static LocalDateTime min(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null && dateTime2 == null) {
            throw new IllegalArgumentException("Both dateTimes cannot be null");
        }
        if (dateTime1 == null) {
            return dateTime2;
        }
        if (dateTime2 == null) {
            return dateTime1;
        }
        return dateTime1.isBefore(dateTime2) ? dateTime1 : dateTime2;
    }

    /**
     * 获取两个日期中的最大日期
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 最大的日期
     */
    public static LocalDate max(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) {
            throw new IllegalArgumentException("Both dates cannot be null");
        }
        if (date1 == null) {
            return date2;
        }
        if (date2 == null) {
            return date1;
        }
        return date1.isAfter(date2) ? date1 : date2;
    }
}
