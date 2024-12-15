package com.hete.supply.scm.server.scm.feishu.api.task;

import com.hete.supply.scm.server.scm.feishu.service.base.FeiShuBaseService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/10/29 15:49
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BfExcDataJob {
    private final FeiShuBaseService feiShuBaseService;

    @XxlJob(value = "bfQcExcDataJob")
    public void bfQcExcDataJob() {
        feiShuBaseService.bfQcExcDataJob();
    }

    @XxlJob(value = "bfReceiveExcDataJob")
    public void bfReceiveExcDataJob() {
        feiShuBaseService.bfReceiveExcDataJob();
    }

    @XxlJob(value = "bfPurchaseExcDataJob")
    public void bfPurchaseExcDataJob() {
        feiShuBaseService.bfPurchaseExcDataJob();
    }

}
