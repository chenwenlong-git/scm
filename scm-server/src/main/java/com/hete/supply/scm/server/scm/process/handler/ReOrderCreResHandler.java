package com.hete.supply.scm.server.scm.process.handler;

import com.hete.supply.scm.server.scm.process.entity.dto.CreateRepairOrderResultMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2023/12/28.
 */
@Slf4j
@Component
public class ReOrderCreResHandler implements AsyncSendMqHandler<CreateRepairOrderResultMqDto> {

    private String beanName;

    @Override
    public void failed(@NotNull @Valid CreateRepairOrderResultMqDto request,
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
