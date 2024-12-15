package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 校验库存、原料、工序定时任务
 *
 * @author yanjiawei
 * @date 2023年08月03日 23:54
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MissingInfoCheckJob {

    private final ProcessOrderBizService processOrderBizService;

    @XxlJob(value = "missingInfoCheckTask")
    public void processOrderStockCheckTask() {
        processOrderBizService.missingInfoCheckTask();
    }
}
