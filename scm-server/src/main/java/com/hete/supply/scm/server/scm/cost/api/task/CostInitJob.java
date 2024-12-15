package com.hete.supply.scm.server.scm.cost.api.task;

import com.hete.supply.scm.server.scm.cost.service.biz.CostInitBizService;
import com.hete.supply.scm.server.scm.cost.service.init.CostInitService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/2/29 16:52
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CostInitJob {
    private final CostInitBizService costInitBizService;
    private final CostInitService costInitService;

    /**
     * 每日初始化成本数据
     */
    @XxlJob(value = "costYestDataInitJob")
    public void costInitJob() {
        costInitBizService.costYestDataInitJob();
    }


    /**
     * 每月初始化成本数据
     */
    @XxlJob(value = "costMonthDataInitJob")
    public void costMonthDataInitJob() {
        costInitBizService.costMonthDataInitJob();
    }

    /**
     * 初始商品成本月初加权单价数据
     */
    @XxlJob(value = "costMonthWeightingPriceInitJob")
    public void costMonthWeightingPriceInitJob() {
        costInitService.costMonthWeightingPriceInitJob();
    }

}
