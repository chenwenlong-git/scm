package com.hete.supply.scm.server.scm.develop.handler;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopStatusDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 开发子单状态变更给plm推送mq
 *
 * @author ChenWenLong
 * @date 2023/8/12 16:50
 */
@Slf4j
@Component
public class DevelopStatusHandler implements AsyncSendMqHandler<DevelopStatusDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid DevelopStatusDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
