package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.base.ProcessPlanBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * @Description 校验订单是否延期定时任务
 * @Date 2023/8/23 15:26
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessPlanDelayCheckJob {
    private final ProcessPlanBizService processPlanBizService;

    @XxlJob(value = "processPlanDelayCheckTask")
    public void execute() {
        processPlanBizService.processPlanDelayCheckTask();
    }

}
