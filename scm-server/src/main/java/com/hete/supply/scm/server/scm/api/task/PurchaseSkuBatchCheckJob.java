package com.hete.supply.scm.server.scm.api.task;

import com.hete.supply.scm.server.scm.service.biz.PurchasePatrolBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2023/12/7 09:26
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PurchaseSkuBatchCheckJob {
    private final PurchasePatrolBizService purchasePatrolBizService;

    /**
     * 校验sku批次码与供应商关系
     */
    @XxlJob(value = "purchaseCheckSupplierCodeTask")
    public void purchaseCheckSupplierCodeTask() {
        purchasePatrolBizService.purchaseCheckSupplierCodeTask();
    }

}
