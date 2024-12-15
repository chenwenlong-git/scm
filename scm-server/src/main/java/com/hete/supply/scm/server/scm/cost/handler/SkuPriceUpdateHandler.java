package com.hete.supply.scm.server.scm.cost.handler;

import com.hete.supply.scm.server.scm.cost.entity.dto.SkuPriceUpdateDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/10/23 14:09
 */
@Slf4j
@Component
public class SkuPriceUpdateHandler implements AsyncSendMqHandler<SkuPriceUpdateDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid SkuPriceUpdateDto request,
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
