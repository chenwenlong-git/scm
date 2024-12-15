package com.hete.supply.scm.server.scm.process.api.task;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBaseService;
import com.xxl.job.core.context.XxlJobHelper;
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
public class CalRepairCostJob {
    private final RepairOrderBaseService repairOrderBaseService;

    @XxlJob(value = "calRepairCostJob")
    public void execute() {
        String rpOrderStr = XxlJobHelper.getJobParam();

        if (StrUtil.isNotBlank(rpOrderStr)) {
            repairOrderBaseService.calCost(rpOrderStr);
        }
    }
}
