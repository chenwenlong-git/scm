package com.hete.supply.scm.server.scm.purchase.api.task;

import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseBizService;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseInitBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2023/12/18 11:44
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PurchaseInitJob {
    private final PurchaseBizService purchaseBizService;
    private final PurchaseInitBizService purchaseInitBizService;

    @XxlJob(value = "purchaseInitAvgPriceTask")
    public void purchaseInitAvgPriceTask() {
        purchaseBizService.purchaseInitAvgPriceTask();
    }

    @XxlJob(value = "purchaseRawChangeTask")
    public void purchaseRawChangeTask() {
        purchaseInitBizService.purchaseRawChangeTask();
    }
}
