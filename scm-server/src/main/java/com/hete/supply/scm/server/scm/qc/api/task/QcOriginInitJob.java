package com.hete.supply.scm.server.scm.qc.api.task;

import com.hete.supply.scm.server.scm.qc.service.biz.QcOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2024/3/6.
 */
@Component
@RequiredArgsConstructor
public class QcOriginInitJob {
    private final QcOrderBizService qcOrderBizService;

    @XxlJob(value = "qcOriginInitJob")
    public void execute() {
        qcOrderBizService.initQcOrigin();
    }
}
