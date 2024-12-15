package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.CostCoefficientsBizService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/2/20.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateCostCoefficientsJob {
    private final CostCoefficientsBizService costCoefficientsBizService;

    @XxlJob(value = "updateCostCoefficientsJob")
    public void execute() {
        String curCnTimeStrWithTestStr = XxlJobHelper.getJobParam();
        String globUpdateCoefficientKey = costCoefficientsBizService.getGlobUpdateCoefficientKey();
        costCoefficientsBizService.updateCoefficient(globUpdateCoefficientKey, curCnTimeStrWithTestStr);
    }
}
