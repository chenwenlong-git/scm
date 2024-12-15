package com.hete.supply.scm.server.scm.qc.handler;

import com.google.common.collect.Sets;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcOriginUpdateBo;
import com.hete.supply.scm.server.scm.qc.service.base.QcOrderBaseService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import com.hete.trace.util.TraceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QcOriginUpdateHandler implements AsyncHandler<QcOriginUpdateBo> {

    private String beanName;
    private final QcOrderBaseService qcOrderBaseService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid QcOriginUpdateBo request) {
        Set<String> failQcList = Sets.newHashSet();
        Set<String> successQcList = Sets.newHashSet();

        List<String> qcOrderNos = request.getQcOrderNos();
        for (String qcOrderNo : qcOrderNos) {
            log.info("updateQcOrigin spanId:{} qcOrderNo:{} ", TraceUtil.getSpanId(), qcOrderNo);
            boolean initQcOriginSuccessful = qcOrderBaseService.initProcAndRepairQcOrigin(qcOrderNo);
            (initQcOriginSuccessful ? successQcList : failQcList).add(qcOrderNo);
        }

        log.info("初始化质检单类型&标识结束！" +
                        "预计:{}条，质检单号:{} " +
                        "成功:{}条，质检单号:{} " +
                        "失败:{}条，质检单号:{} ",
                qcOrderNos.size(), qcOrderNos.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")),
                successQcList.size(), successQcList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")),
                failQcList.size(), failQcList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
        return true;
    }

    @Override
    public void failed(@NotNull @Valid QcOriginUpdateBo request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
    }

    @Override
    public @NotBlank String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
