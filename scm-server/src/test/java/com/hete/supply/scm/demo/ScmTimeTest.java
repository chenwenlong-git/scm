package com.hete.supply.scm.demo;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yanjiawei
 * @date 2023年08月16日 10:29
 */
public class ScmTimeTest {

    @Test
    public void testNextBegin() {

    }

    @Test
    public void test() {
        // 上班时间、下班时间、午休时间段
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 30);
        LocalTime lunchStartTime = LocalTime.of(12, 0);
        LocalTime lunchEndTime = LocalTime.of(13, 30);

        // 员工计划情况，包括跨天情况
        LocalDateTime planStartTime = LocalDateTime.of(2023, 8, 18, 15, 0);
        LocalDateTime planEndTime = LocalDateTime.of(2023, 8, 21, 10, 30);

        List<LocalDate> holidays = List.of(LocalDate.of(2023, 8, 20));

        // 计算每一天的工作时长
//        List<WorkDurationResult> workDurationResults
//                = calculateWorkDuration(planStartTime, planEndTime, startTime, endTime, lunchStartTime, lunchEndTime, holidays);
//        for (WorkDurationResult workDurationResult : workDurationResults) {
//            System.out.println(workDurationResult.getDate());
//            System.out.println(workDurationResult.getDuration().toMinutes());
//        }
        calculateDailyWorkDuration(planStartTime, planEndTime, startTime, endTime, lunchStartTime, lunchEndTime, holidays);

    }

    public static void calculateDailyWorkDuration(LocalDateTime planStart, LocalDateTime planEnd, LocalTime workStart, LocalTime workEnd, LocalTime lunchStart, LocalTime lunchEnd, List<LocalDate> holidays) {
        LocalDate currentDate = planStart.toLocalDate();
        LocalDateTime currentDateTime = planStart;

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
                    if (workDayStart.toLocalTime().isBefore(lunchEnd) && workDayEnd.toLocalTime().isAfter(lunchStart)) {
                        totalWorkDuration = totalWorkDuration.minus(Duration.between(lunchStart, lunchEnd));
                        System.out.println(currentDate + ": 工作时长 " + totalWorkDuration.toMinutes() + " 分钟");
                    } else {
                        System.out.println(currentDate + ": 工作时长 " + totalWorkDuration.toMinutes() + " 分钟");
                    }
                }
            }
            currentDateTime = currentDateTime.plusDays(1);
            currentDate = currentDateTime.toLocalDate();
        }
    }

    public static List<WorkDurationResult> calculateWorkDuration(LocalDateTime expectedStartTime, LocalDateTime expectedEndTime, LocalTime workStartTime, LocalTime workEndTime, LocalTime breakStartTime, LocalTime breakEndTime, List<LocalDate> holidays) {

        List<WorkDurationResult> result = new ArrayList<>();
        LocalDateTime currentDateTime = expectedStartTime;

        while (!currentDateTime.isAfter(expectedEndTime)) {
            if (!isHoliday(currentDateTime.toLocalDate(), holidays)) {
                LocalTime dayStartTime = currentDateTime.toLocalTime().isBefore(workStartTime) ? workStartTime : currentDateTime.toLocalTime();

                LocalTime dayEndTime = currentDateTime.toLocalTime().isAfter(workEndTime) ? workEndTime : currentDateTime.toLocalTime();

                Duration dayWorkDuration = calculateDayWorkDuration(dayStartTime, dayEndTime, breakStartTime, breakEndTime);
                result.add(new WorkDurationResult(currentDateTime.toLocalDate(), dayWorkDuration));
            }

            currentDateTime = currentDateTime.plusDays(1);
        }

        return result;
    }

    private static boolean isHoliday(LocalDate date, List<LocalDate> holidays) {
        return holidays.contains(date);
    }

    private static Duration calculateDayWorkDuration(LocalTime dayStartTime, LocalTime dayEndTime, LocalTime breakStartTime, LocalTime breakEndTime) {

        Duration workDuration = Duration.between(dayStartTime, dayEndTime);
        if (dayStartTime.isBefore(breakEndTime) && dayEndTime.isAfter(breakStartTime)) {
            Duration breakDuration = Duration.between(breakStartTime, breakEndTime);
            workDuration = workDuration.minus(breakDuration);
        }

        return workDuration;
    }

    /**
     * 根据开始时间和耗时得到结束时间（跳过节假日和休息时间段）
     *
     * @param startTime
     * @param durationHours
     * @param durationMinutes
     * @return
     */
    public static LocalDateTime calculateEndTime(LocalDateTime startTime, int durationHours, int durationMinutes, Set<LocalDate> holidays, LocalTime workStartTime, LocalTime workEndTime1, LocalTime workStartTime2, LocalTime workEndTime2) {
        LocalDateTime endTime = startTime;
        int remainingHours = durationHours;
        int remainingMinutes = durationMinutes;

        while (remainingHours > 0 || remainingMinutes > 0) {
            endTime = endTime.plusMinutes(1);

            // 跳过非工作时间
            if (!isWorkingTime(endTime.toLocalTime(), workStartTime, workEndTime1, workStartTime2, workEndTime2)) {
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

        return endTime;
    }

    private static boolean isWorkingTime(LocalTime time, LocalTime workStartTime, LocalTime workEndTime1, LocalTime workStartTime2, LocalTime workEndTime2) {
//        LocalTime workStartTime = LocalTime.of(9, 0); // 上班时间起始
//        LocalTime workEndTime1 = LocalTime.of(11, 59); // 下班时间结束（午餐）
//        LocalTime workStartTime2 = LocalTime.of(13, 0); // 上班时间起始（午餐结束）
//        LocalTime workEndTime2 = LocalTime.of(18, 29); // 下班时间结束
        return !(time.isBefore(workStartTime) || (time.isAfter(workEndTime1) && time.isBefore(workStartTime2)) || time.isAfter(workEndTime2));
    }

    private static boolean isHoliday(LocalDate date, Set<LocalDate> holidays) {
        return holidays.contains(date);
    }

    public static class WorkDurationResult {
        private LocalDate date;
        private Duration duration;

        public WorkDurationResult(LocalDate date, Duration duration) {
            this.date = date;
            this.duration = duration;
        }

        public LocalDate getDate() {
            return date;
        }

        public Duration getDuration() {
            return duration;
        }
    }
}
