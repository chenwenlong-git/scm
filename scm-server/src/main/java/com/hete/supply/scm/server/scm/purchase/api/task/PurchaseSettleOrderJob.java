package com.hete.supply.scm.server.scm.purchase.api.task;

import com.hete.supply.scm.server.scm.settle.service.base.PurchaseSettleOrderBaseService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @Desc 结算统计
 * @date 2023/1/6 10:26
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PurchaseSettleOrderJob {
    private final PurchaseSettleOrderBaseService purchaseSettleOrderBaseService;


    @XxlJob(value = "purchaseSettleOrderTask")
    public void settleOrderTask() {
        log.info("开始统计采购结算任务");
        String param = XxlJobHelper.getJobParam();
        long start = System.currentTimeMillis();
        purchaseSettleOrderBaseService.countPurchaseSettleOrder(null, null, param);
        log.info("结束统计采购结算任务,耗时={}ms", System.currentTimeMillis() - start);
    }
}
