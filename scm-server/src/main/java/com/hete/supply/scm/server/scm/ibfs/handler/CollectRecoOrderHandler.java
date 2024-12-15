package com.hete.supply.scm.server.scm.ibfs.handler;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.ibfs.service.biz.RecoOrderBizService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/6/1 09:48
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CollectRecoOrderHandler implements AsyncHandler<FinanceRecoOrderPo> {
    private String beanName;

    private final RecoOrderBizService recoOrderBizService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid FinanceRecoOrderPo request) throws Exception {
        recoOrderBizService.collectRecoOrderTask(request);
        return true;
    }

    @Override
    public void failed(@NotNull @Valid FinanceRecoOrderPo request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("对账单收单失败！对账单单号:{},异常信息:{}", request.getFinanceRecoOrderNo(),
                JSON.toJSONString(failCallbackBo));
    }

    @Override
    public @NotBlank String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
