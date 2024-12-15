package com.hete.supply.scm.demo;/**
 * 1
 *
 * @author yanjiawei
 * Created on 2023/8/25.
 */

import com.hete.support.api.exception.BizException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * @author yanjiawei
 * @date 2023年08月25日 13:59
 */
public class NextProcessStartTimeCalculator {
    public static LocalDateTime calculateNextStartTime(LocalDateTime currentStartTime, int processDurationMinutes, List<LocalDate> holidays, List<TimeRange> workTimeRanges) {
        LocalDateTime endTime = currentStartTime.plusMinutes(processDurationMinutes);

        // Skip holidays
        while (isHoliday(endTime.toLocalDate(), holidays)) {
            endTime = endTime.plus(1, ChronoUnit.DAYS);
        }

        // Move to the next work day and find the nearest work start time
        LocalDateTime nextWorkDateTime = getNextWorkDateTime(endTime, workTimeRanges);

        return nextWorkDateTime;
    }

    private static boolean isHoliday(LocalDate date, List<LocalDate> holidays) {
        return holidays.contains(date);
    }

    private static LocalDateTime getNextWorkDateTime(LocalDateTime dateTime, List<TimeRange> workTimeRanges) {
        while (true) {
            LocalTime time = dateTime.toLocalTime();
            for (TimeRange workTimeRange : workTimeRanges) {
                if (time.isBefore(workTimeRange.getStartTime())) {
                    return dateTime.with(workTimeRange.getStartTime());
                } else if (time.isBefore(workTimeRange.getEndTime())) {
                    return dateTime; // Within work time
                }
            }
            TimeRange timeRange = workTimeRanges.stream().findFirst().orElse(null);
            if (Objects.isNull(timeRange)) {
                throw new BizException("工作时间段为空，请检查相关配置");
            }
            dateTime = dateTime.plus(1, ChronoUnit.DAYS).with(timeRange.getStartTime());
        }
    }

    public static class TimeRange {
        private LocalTime startTime;
        private LocalTime endTime;

        public TimeRange(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }
    }

    public static void main(String[] args) {
        // Example usage
        LocalDateTime currentStartTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 22));
        int processDurationMinutes = 90;
        List<LocalDate> holidays = List.of(LocalDate.of(2023, 8, 28));
        List<TimeRange> workTimeRanges = List.of(new TimeRange(LocalTime.of(1, 0), LocalTime.of(4, 0)),
                new TimeRange(LocalTime.of(5, 30), LocalTime.of(10, 30)));

        LocalDateTime nextStartTime = calculateNextStartTime(currentStartTime, processDurationMinutes, holidays, workTimeRanges);

        System.out.println("Next start time: " + nextStartTime);
    }
}
