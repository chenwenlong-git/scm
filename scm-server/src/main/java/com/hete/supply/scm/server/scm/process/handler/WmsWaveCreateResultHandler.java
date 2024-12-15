package com.hete.supply.scm.server.scm.process.handler;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessWaveCreateResultMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 加工单波次创建结果 mq
 *
 * @Author: RockyHuas
 * @date: 2022/11/19 11:50
 */
@Slf4j
@Component
public class WmsWaveCreateResultHandler implements AsyncSendMqHandler<ProcessWaveCreateResultMqDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid ProcessWaveCreateResultMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
