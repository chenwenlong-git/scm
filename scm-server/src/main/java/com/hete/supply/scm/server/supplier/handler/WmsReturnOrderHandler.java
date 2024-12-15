package com.hete.supply.scm.server.supplier.handler;

import com.hete.supply.scm.server.supplier.entity.dto.ReturnOrderMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023-06-27 17:02:46
 */
@Slf4j
@Component
public class WmsReturnOrderHandler implements AsyncSendMqHandler<ReturnOrderMqDto> {

    private String beanName;

    @Override
    public void failed(@NotNull @Valid ReturnOrderMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {
    }

    @Override
    public @NotBlank String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
