package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * @Description 刷新答交时间是否延期
 * @Date 2024/4/11 11:39
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshPromiseDateDelayedJob {
    private final ProcessOrderBizService processOrderBizService;

    /**
     * 刷新答交时间
     */
    @XxlJob(value = "refreshPromiseDateDelayedJob")
    public void execute() {
        log.info("开始执行刷新 promise_date_delayed 任务...");

        // 调用服务方法刷新 promise_date_delayed 字段
        processOrderBizService.refreshPromiseDateDelayed();

        log.info("刷新 promise_date_delayed 任务执行完成.");
    }
}
