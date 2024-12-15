package com.hete.supply.scm.server.scm.process.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Sets;
import com.hete.supply.scm.server.scm.process.entity.bo.CalculateCostBo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderItemBizService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import com.hete.trace.util.TraceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessCostHandler implements AsyncHandler<CalculateCostBo> {

    private String beanName;
    private final ProcessOrderItemBizService processOrderItemBizService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid CalculateCostBo request) {
        Set<String> failPonList = Sets.newHashSet();
        Set<String> successPonList = Sets.newHashSet();

        Set<String> processOrderNos = request.getProcessOrderNos();
        for (String processOrderNo : processOrderNos) {
            log.info("doCalculateBatchCodeCost spanId:{} processOrderNo:{} ", TraceUtil.getSpanId(), processOrderNo);
            boolean calculateSuccessful = processOrderItemBizService.doCalculateBatchCodeCost(processOrderNo);
            (calculateSuccessful ? successPonList : failPonList).add(processOrderNo);
        }

        log.info("计算批次码总成本结束！" +
                        "预计:{}条，加工单号:{} " +
                        "成功:{}条，加工单号:{} " +
                        "失败:{}条，加工单号:{} ",
                processOrderNos.size(), processOrderNos.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")),
                successPonList.size(), successPonList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")),
                failPonList.size(), failPonList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
        return true;
    }

    @Override
    public void failed(@NotNull @Valid CalculateCostBo request,
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
