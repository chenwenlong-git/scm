package com.hete.supply.scm.server.scm.production.api.task;

import com.hete.supply.scm.server.scm.production.service.biz.AttributeBizService;
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
public class SkuTaskJob {
    private final AttributeBizService attrBizService;

    @XxlJob(value = "skuTaskJob")
    public void execute() {
        attrBizService.doCalSkuRiskJob();
    }
}
