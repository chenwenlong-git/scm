package com.hete.supply.scm.server.scm.ibfs.api.task;

import com.hete.supply.scm.server.scm.ibfs.service.biz.RecoOrderBizService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ChenWenLong
 * @date 2024/5/29 13:36
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FinanceRecoOrderJob {
    private final RecoOrderBizService recoOrderBizService;


    @XxlJob(value = "createFinanceRecoOrderTask")
    public void createFinanceRecoOrderTask() {
        log.info("开始创建对账单任务");
        String param = XxlJobHelper.getJobParam();
        long start = System.currentTimeMillis();
        recoOrderBizService.createFinanceRecoOrderTask(param);
        log.info("结束创建对账单任务,耗时={}ms", System.currentTimeMillis() - start);
    }

    @XxlJob(value = "collectFinanceRecoOrderTask")
    public void collectFinanceRecoOrderTask() {
        log.info("开始收单的对账单任务");
        String param = XxlJobHelper.getJobParam();
        long start = System.currentTimeMillis();
        recoOrderBizService.collectFinanceRecoOrderTask(param);
        log.info("结束收单的对账单任务,耗时={}ms", System.currentTimeMillis() - start);
    }

    @XxlJob(value = "delRecoOrderAmountEqZeroTask")
    public void delRecoOrderAmountEqZeroTask() {
        log.info("删除已收单的对账单金额为0的任务");
        String param = XxlJobHelper.getJobParam();
        long start = System.currentTimeMillis();
        recoOrderBizService.delRecoOrderAmountEqZeroTask(param);
        log.info("结束删除已收单的对账单金额为0的任务,耗时={}ms", System.currentTimeMillis() - start);
    }
}
