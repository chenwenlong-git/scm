package com.hete.supply.scm.server.scm.settle.api.task;

import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleSettleBaseService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ChenWenLong
 * @date 2023/8/7 10:45
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DevelopSampleSettleJob {
    private final DevelopSampleSettleBaseService developSampleSettleBaseService;


    @XxlJob(value = "developSampleSettleOrderTask")
    public void settleOrderTask() {
        log.info("开始统计开发样品单结算任务");
        String param = XxlJobHelper.getJobParam();
        long start = System.currentTimeMillis();
        developSampleSettleBaseService.countDevelopSampleSettleOrder(null, param);
        log.info("结束统计开发样品单结算任务,耗时={}ms", System.currentTimeMillis() - start);
    }
}
