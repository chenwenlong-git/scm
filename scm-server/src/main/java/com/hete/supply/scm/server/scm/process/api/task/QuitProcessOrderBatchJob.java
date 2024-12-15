package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuitProcessOrderBatchJob {
    private final ProcessOrderBizService processOrderBizService;

    @XxlJob(value = "quitProcessOrderBatchJob")
    public void quitProcessOrderBatchJob() {
        processOrderBizService.quitProcessOrderBatchJob();
    }
}
