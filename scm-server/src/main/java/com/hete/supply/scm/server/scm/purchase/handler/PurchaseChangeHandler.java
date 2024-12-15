package com.hete.supply.scm.server.scm.purchase.handler;

import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/8/9 09:57
 */
@Slf4j
@Component
public class PurchaseChangeHandler implements AsyncSendMqHandler<PurchaseChildNoMqDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid PurchaseChildNoMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
