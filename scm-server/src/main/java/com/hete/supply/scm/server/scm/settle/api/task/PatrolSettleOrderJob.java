package com.hete.supply.scm.server.scm.settle.api.task;

import com.hete.supply.scm.server.scm.settle.service.base.ProcessSettleOrderBaseService;
import com.hete.supply.scm.server.scm.settle.service.base.PurchaseSettleOrderBaseService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 巡检结算单
 *
 * @author ChenWenLong
 * @date 2023/6/7 16:38
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PatrolSettleOrderJob {
    private final PurchaseSettleOrderBaseService purchaseSettleOrderBaseService;
    private final ProcessSettleOrderBaseService processSettleOrderBaseService;

    @XxlJob(value = "patrolSettleOrderTask")
    public void patrolSettleOrderTask() {
        log.info("开始巡检结算单任务");
        String param = XxlJobHelper.getJobParam();
        long start = System.currentTimeMillis();
        purchaseSettleOrderBaseService.patrolSettleOrder(param);
        processSettleOrderBaseService.patrolSettleOrder(param);
        log.info("结束巡检结算单任务,耗时={}ms", System.currentTimeMillis() - start);
    }
}
