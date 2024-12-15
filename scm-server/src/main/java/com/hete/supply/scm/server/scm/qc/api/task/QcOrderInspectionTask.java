package com.hete.supply.scm.server.scm.qc.api.task;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.server.scm.qc.service.base.QcOrderBaseService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 质检单巡检job
 *
 * @author yanjiawei
 * Created on 2023/10/18.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QcOrderInspectionTask {

    private final static int DEFAULT_DAY = 1;
    private final QcOrderBaseService qcOrderBaseService;

    @XxlJob(value = "qcOrderInspectionTask")
    public void performInspection() {
        final String dayParamStr = XxlJobHelper.getJobParam();
        qcOrderBaseService.performInspection(
                StrUtil.isNotBlank(dayParamStr) ? Integer.parseInt(dayParamStr) : DEFAULT_DAY);
    }
}
