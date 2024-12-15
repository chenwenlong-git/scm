package com.hete.supply.scm.server.scm.purchase.api.task;

import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseDataService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/3/28 09:46
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PurchaseDataJob {
    private final PurchaseDataService purchaseDataService;

    /**
     * 缺货数据推送
     */
    @XxlJob(value = "stockOutJob")
    public void stockOutJob() {
        purchaseDataService.pushSkuStockOutData();
        purchaseDataService.pushSkuStockOutReason();
        purchaseDataService.pushSkuStockOutOrderReason();
        purchaseDataService.pushSkuStockOutMonthReason();
    }


    /**
     * 获取采购统计数据推送到飞书
     */
    @XxlJob(value = "purchaseDataJob")
    public void purchaseDataJob() {
        purchaseDataService.pushPurchaseCntData();
    }

    /**
     * 采购需求单完成状态变更定时任务
     */
    @XxlJob(value = "purchaseParentCompleteJob")
    public void purchaseParentCompleteJob() {
        purchaseDataService.purchaseParentCompleteJob();
    }

    /**
     * 采购订单完结状态变更定时任务
     */
    @XxlJob(value = "purchaseChildFinishJob")
    public void purchaseChildFinishJob() {
        purchaseDataService.purchaseChildFinishJob();
    }

    @XxlJob(value = "purchaseDeliverDataJob")
    public void purchaseDeliverDataJob() {
        String param = XxlJobHelper.getJobParam();

        purchaseDataService.purchaseDeliverDataJob(param);
    }
}
