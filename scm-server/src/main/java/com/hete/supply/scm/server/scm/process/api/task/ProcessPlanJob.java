package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.base.ProcessPlanBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * @date 2023年07月27日 13:49
 */
@Component
@RequiredArgsConstructor
public class ProcessPlanJob {
    private final ProcessPlanBizService processPlanBizService;

    @XxlJob(value = "processPlanTask")
    public void processPlanTask() {
        processPlanBizService.executeProductionPlanTask();
    }
}
