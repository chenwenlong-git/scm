package com.hete.supply.scm.server.scm.production.api.task;

import com.hete.supply.scm.server.scm.production.service.biz.AttributeBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/9/27.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitAttrCategoryJob {
    private final AttributeBizService attributeBizService;

    @XxlJob(value = "initAttrCategoryJob")
    public void execute() {
        attributeBizService.initAttrCategory();
    }
}
