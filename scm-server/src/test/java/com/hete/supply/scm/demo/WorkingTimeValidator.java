package com.hete.supply.scm.demo;

import java.time.LocalTime;
import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年08月21日 16:00
 */
public class WorkingTimeValidator {
    public static boolean isWorkingTime(LocalTime targetTime, List<WorkTimeRange> workTimeRanges) {
        for (WorkTimeRange range : workTimeRanges) {
            if (targetTime.isAfter(range.getStartTime()) && targetTime.isBefore(range.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        LocalTime targetTime = LocalTime.of(12, 0);
        List<WorkTimeRange> workTimeRanges = List.of(
                new WorkTimeRange(LocalTime.of(9, 0), LocalTime.of(11, 59)),
                new WorkTimeRange(LocalTime.of(13, 30), LocalTime.of(18, 29))
        );

        boolean isWorking = isWorkingTime(targetTime, workTimeRanges);
        System.out.println("Is Working Time: " + isWorking);
    }
}

class WorkTimeRange {
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkTimeRange(LocalTime startTime, LocalTime endTime) {
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
