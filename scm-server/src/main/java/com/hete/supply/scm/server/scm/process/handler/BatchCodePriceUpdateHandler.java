package com.hete.supply.scm.server.scm.process.handler;

import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Slf4j
@Component
public class BatchCodePriceUpdateHandler implements AsyncSendMqHandler<UpdateBatchCodePriceDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid UpdateBatchCodePriceDto request,
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
