package com.hete.supply.scm.server.scm.feishu.handler;

import com.hete.supply.mc.api.msg.entity.dto.FeiShuGroupMsgDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/10/31 10:07
 */
@Slf4j
@Component
public class FeiShuGroupHandler implements AsyncSendMqHandler<FeiShuGroupMsgDto> {

    private String name;

    @Override
    public void failed(@NotNull @Valid FeiShuGroupMsgDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("发送群组消息失败：msg:{}", failCallbackBo.getBizErrorMsg());
    }

    @Override
    public @NotBlank String getBeanName() {
        return name;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

}
