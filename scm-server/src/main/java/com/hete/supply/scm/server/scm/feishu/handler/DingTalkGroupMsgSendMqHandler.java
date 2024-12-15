package com.hete.supply.scm.server.scm.feishu.handler;

import com.hete.supply.mc.api.msg.entity.dto.DingTalkGroupMsgDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/10/23 17:53
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DingTalkGroupMsgSendMqHandler implements AsyncSendMqHandler<DingTalkGroupMsgDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid DingTalkGroupMsgDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("钉钉群消息发送mq失败，request:{},error:{}", JacksonUtil.parse2Str(request), failCallbackBo.getBizErrorMsg());
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
