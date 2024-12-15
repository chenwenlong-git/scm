package com.hete.supply.scm.server.scm.process.handler;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCancelEventDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 加工单取消推送WMS
 *
 * @author ChenWenLong
 * @date 2023/8/28 11:27
 */
@Slf4j
@Component
public class WmsProcessCancelHandler implements AsyncSendMqHandler<ProcessOrderCancelEventDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid ProcessOrderCancelEventDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
