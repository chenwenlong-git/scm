package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RepairOrderStockMatchingJob {
    private final RepairOrderBizService bizService;

    @XxlJob(value = "repairOrderStockMatchingJob")
    public void repairOrderStockMatching() {
        bizService.repairOrderStockMatchingJob();
    }
}
