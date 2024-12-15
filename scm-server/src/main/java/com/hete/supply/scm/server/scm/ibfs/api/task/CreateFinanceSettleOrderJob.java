package com.hete.supply.scm.server.scm.ibfs.api.task;

import com.hete.supply.scm.server.scm.ibfs.service.biz.FinanceSettleOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CreateFinanceSettleOrderJob {
    private final FinanceSettleOrderBizService financeSettleOrderBizService;

    @XxlJob(value = "createFinanceSettleOrderJob")
    public void createFinanceSettleOrderJob() {
        financeSettleOrderBizService.createFinanceSettleOrderJob();
    }
}
