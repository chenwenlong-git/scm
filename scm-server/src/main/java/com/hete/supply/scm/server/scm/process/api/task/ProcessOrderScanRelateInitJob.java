package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderScanBaseService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/2/27.
 */
@Component
@RequiredArgsConstructor
public class ProcessOrderScanRelateInitJob {
    private final ProcessOrderScanBaseService processOrderScanBaseService;

    @XxlJob(value = "processOrderScanRelateInitJob")
    public void processOrderScanRelateInitJob() {
        processOrderScanBaseService.processOrderScanRelateInitJob();
    }
}
