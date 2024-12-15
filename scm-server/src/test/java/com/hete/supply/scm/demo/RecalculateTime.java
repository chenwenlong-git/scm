package com.hete.supply.scm.demo;/**
 * @author yanjiawei
 * Created on 2023/8/31.
 */

import com.hete.supply.scm.server.scm.process.entity.bo.BreakTimeSegmentBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessPlanTimeBo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年08月31日 18:08
 */
public class RecalculateTime {
    public static void getProcessPlanDate() {

        List<BreakTimeSegmentBo> breakTimeSegments = new ArrayList<>();
        BreakTimeSegmentBo bo = new BreakTimeSegmentBo();
        bo.setStartTime(LocalTime.of(4, 0));
        bo.setEndTime(LocalTime.of(5, 30));
        breakTimeSegments.add(bo);

        List<ProcessPlanTimeBo> processPlanTimeBos = new ArrayList<>();

        ProcessPlanTimeBo processPlanTimeBo1 = new ProcessPlanTimeBo();
        processPlanTimeBo1.setExpectBeginDateTime(LocalDateTime.of(2023, 8, 31, 7, 50));
        processPlanTimeBo1.setExpectEndDateTime(LocalDateTime.of(2023, 9, 1, 6, 20));
        processPlanTimeBos.add(processPlanTimeBo1);

        ProcessPlanTimeBo processPlanTimeBo2 = new ProcessPlanTimeBo();
        processPlanTimeBo2.setExpectBeginDateTime(LocalDateTime.of(2023, 9, 1, 6, 20));
        processPlanTimeBo2.setExpectEndDateTime(LocalDateTime.of(2023, 9, 2, 3, 20));
        processPlanTimeBos.add(processPlanTimeBo2);

        ProcessPlanTimeBo processPlanTimeBo3 = new ProcessPlanTimeBo();
        processPlanTimeBo3.setExpectBeginDateTime(LocalDateTime.of(2023, 9, 2, 5, 50));
        processPlanTimeBo3.setExpectEndDateTime(LocalDateTime.of(2023, 9, 3, 5, 50));
        processPlanTimeBos.add(processPlanTimeBo3);

//        ProcessPlanTimeBo processPlanTimeBo = processPlanBaseService.recalculateTime("", LocalDateTime.of(2023, 9, 1, 1, 0),
//                LocalDateTime.of(2023, 9, 1, 10, 30), 480, ChronoUnit.MINUTES, LocalTime.of(1, 0), LocalTime.of(10, 30),
//                breakTimeSegments, Sets.newTreeSet(), processPlanTimeBos);
//        System.out.println(processPlanTimeBo);
    }
}



