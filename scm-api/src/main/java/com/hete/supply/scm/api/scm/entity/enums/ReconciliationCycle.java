package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:17
 */
@AllArgsConstructor
@Getter
public enum ReconciliationCycle implements IRemark {

    // 对账周期(需要配置计算规则)
    MONTH("月结"),
    WEEK("周结"),
    ;

    private final String remark;

    /**
     * 获取对账周期的开始时间
     *
     * @param reconciliationCycle:
     * @param localDateTimeNow:
     * @return LocalDateTime
     * @author ChenWenLong
     * @date 2024/5/22 11:10
     */
    public static LocalDateTime getReconciliationStartTime(@NotNull ReconciliationCycle reconciliationCycle, LocalDateTime localDateTimeNow) {
        // 获取当前日期
        LocalDate currentDate = localDateTimeNow.toLocalDate();
        if (MONTH.equals(reconciliationCycle)) {
            // 获取当前月的第一天
            LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
            // 将其转换为LocalDateTime
            return firstDayOfMonth.atStartOfDay();
        }
        if (WEEK.equals(reconciliationCycle)) {
            // 获取当前周的第一天（周一）
            LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            // 将其转换为LocalDateTime
            return firstDayOfWeek.atStartOfDay();
        }
        throw new BizException("对账周期{}没有配置对应计算规则，请联系系统管理员！", reconciliationCycle.getRemark());
    }

    /**
     * 获取对账周期的结束时间
     *
     * @param reconciliationCycle:
     * @param localDateTimeNow:
     * @return LocalDateTime
     * @author ChenWenLong
     * @date 2024/5/22 11:10
     */
    public static LocalDateTime getReconciliationEndTime(@NotNull ReconciliationCycle reconciliationCycle, LocalDateTime localDateTimeNow) {
        // 获取当前日期
        LocalDate currentDate = localDateTimeNow.toLocalDate();
        if (MONTH.equals(reconciliationCycle)) {
            // 获取当前月的最后一天
            LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
            // 将其转换为LocalDateTime，并设置为当天的结束时间（23:59:59）
            return lastDayOfMonth.atTime(LocalTime.of(23, 59, 59));
        }
        if (WEEK.equals(reconciliationCycle)) {
            // 获取当前周的最后一天（周日）
            LocalDate lastDayOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            // 将其转换为LocalDateTime，并设置为当天的结束时间（23:59:59.999999999）
            return lastDayOfWeek.atTime(LocalTime.of(23, 59, 59));
        }
        throw new BizException("对账周期{}没有配置对应计算规则，请联系系统管理员！", reconciliationCycle.getRemark());
    }

    /**
     * 获取对账周期上次的开始时间
     *
     * @param reconciliationCycle:
     * @param localDateTimeNow:
     * @return LocalDateTime
     * @author ChenWenLong
     * @date 2024/5/22 11:10
     */
    public static LocalDateTime getReconciliationLastStartTime(@NotNull ReconciliationCycle reconciliationCycle, LocalDateTime localDateTimeNow) {
        // 获取当前日期
        LocalDate currentDate = localDateTimeNow.toLocalDate();
        if (MONTH.equals(reconciliationCycle)) {
            // 获取上个月的第一天
            LocalDate firstDayOfLastMonth = currentDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            // 将其转换为LocalDateTime
            return firstDayOfLastMonth.atStartOfDay();
        }
        if (WEEK.equals(reconciliationCycle)) {
            // 获取当前周的第一天（周一）
            LocalDate firstDayOfCurrentWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            // 获取上周的第一天（即当前周第一天的前一周的同一天）
            LocalDate firstDayOfLastWeek = firstDayOfCurrentWeek.minusWeeks(1);
            // 将其转换为LocalDateTime
            return firstDayOfLastWeek.atStartOfDay();
        }
        throw new BizException("对账周期{}没有配置对应计算规则，请联系系统管理员！", reconciliationCycle.getRemark());
    }

    /**
     * 获取对账周期上次的结束时间
     *
     * @param reconciliationCycle:
     * @return LocalDateTime
     * @author ChenWenLong
     * @date 2024/5/22 11:10
     */
    public static LocalDateTime getReconciliationLastEndTime(@NotNull ReconciliationCycle reconciliationCycle, LocalDateTime localDateTimeNow) {
        // 获取当前日期
        LocalDate currentDate = localDateTimeNow.toLocalDate();
        if (MONTH.equals(reconciliationCycle)) {
            // 获取上个月的最后一天
            LocalDate lastDayOfLastMonth = currentDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            // 将其转换为LocalDateTime，并设置为当天的结束时间（23:59:59）
            return lastDayOfLastMonth.atTime(LocalTime.of(23, 59, 59));
        }
        if (WEEK.equals(reconciliationCycle)) {
            // 获取当前周的最后一天（周日）
            LocalDate lastDayOfCurrentWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            // 获取上周的最后一天（即当前周最后一天的前一周的同一天）
            LocalDate lastDayOfLastWeek = lastDayOfCurrentWeek.minusWeeks(1);
            // 将其转换为LocalDateTime，并设置为当天的结束时间（23:59:59）
            return lastDayOfLastWeek.atTime(LocalTime.of(23, 59, 59));
        }
        throw new BizException("对账周期{}没有配置对应计算规则，请联系系统管理员！", reconciliationCycle.getRemark());
    }

    /**
     * 返回所有枚举常量
     *
     * @param :
     * @return List<ReconciliationCycle>
     * @author ChenWenLong
     * @date 2024/5/29 11:15
     */
    public static List<ReconciliationCycle> getAllValues() {
        return Arrays.asList(ReconciliationCycle.values());
    }

    /**
     * 判断不同枚举是否周期的第一天
     *
     * @param reconciliationCycle:
     * @param localDateTimeNow:
     * @return Boolean
     * @author ChenWenLong
     * @date 2024/5/29 11:29
     */
    public static Boolean whetherFirstDay(ReconciliationCycle reconciliationCycle, LocalDateTime localDateTimeNow) {
        LocalDate today = localDateTimeNow.toLocalDate();
        if (WEEK.equals(reconciliationCycle)) {
            // 判断今天是否是周的第一天（假设周一是周的第一天）
            return today.getDayOfWeek() == DayOfWeek.MONDAY;
        }
        if (MONTH.equals(reconciliationCycle)) {
            // 判断今天是否是月的第一天
            return today.getDayOfMonth() == 1;
        }
        return false;
    }

    /**
     * 判断是否周期或月的第一天
     *
     * @param localDateTimeNow:
     * @return Boolean
     * @author ChenWenLong
     * @date 2024/5/29 11:29
     */
    public static Boolean whetherCurrentFirstDay(LocalDateTime localDateTimeNow) {
        LocalDate today = localDateTimeNow.toLocalDate();
        // 判断今天是否是周或的第一天
        return (today.getDayOfWeek() == DayOfWeek.MONDAY) || (today.getDayOfMonth() == 1);

    }

}
