package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderScanBizService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/2/29.
 */
@Component
@RequiredArgsConstructor
public class UpdateCommissionJob {
    private final ProcessOrderScanBizService processOrderScanBizService;

    @XxlJob(value = "updateCommissionJob")
    public void execute() {
        String scanCompleteTimeStr = XxlJobHelper.getJobParam();
        processOrderScanBizService.updateCommissionJob(scanCompleteTimeStr);
    }
}
