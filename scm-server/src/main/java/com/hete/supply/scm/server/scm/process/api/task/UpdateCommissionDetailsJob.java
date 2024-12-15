package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderScanBizService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 注：一次性job,当2024年2月在master分支看到这个，代表可删除
 *
 * @author yanjiawei
 * Created on 2023/12/16.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateCommissionDetailsJob {
    private final ProcessOrderScanBizService processOrderScanBizService;

    @XxlJob(value = "updateCommissionDetailsJob")
    public void execute() {
        String scanCompleteTimeStr = XxlJobHelper.getJobParam();
        processOrderScanBizService.updateCommissionDetailsJob(scanCompleteTimeStr);
    }
}
