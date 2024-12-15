package com.hete.supply.scm.server.scm.process.handler;

import com.hete.supply.scm.server.scm.process.entity.bo.RefreshPromiseDateDelayedBo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/4/12.
 */
@Component
@RequiredArgsConstructor
public class RefreshProDateDelayHandler implements AsyncHandler<RefreshPromiseDateDelayedBo> {

    private String beanName;
    private final ProcessOrderBizService processOrderBizService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid RefreshPromiseDateDelayedBo request) {
        processOrderBizService.refreshPromiseDateDelayed(request);
        return true;
    }

    @Override
    public void failed(@NotNull @Valid RefreshPromiseDateDelayedBo request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
