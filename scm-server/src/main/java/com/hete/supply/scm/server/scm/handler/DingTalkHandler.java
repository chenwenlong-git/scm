package com.hete.supply.scm.server.scm.handler;

import com.hete.supply.mc.api.msg.entity.dto.DingTalkOtoMsgDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 钉钉消息推送
 *
 * @Author: RockyHuas
 * @date: 2022/12/15 11:50
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DingTalkHandler implements AsyncSendMqHandler<DingTalkOtoMsgDto> {

    private String beanName;

    @Override
    public void failed(@NotNull @Valid DingTalkOtoMsgDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("钉钉消息发送失败！！！");
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
