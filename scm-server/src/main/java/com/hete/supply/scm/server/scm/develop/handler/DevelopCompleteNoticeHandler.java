package com.hete.supply.scm.server.scm.develop.handler;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleCompleteNoticeMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 齐备信息完成时推送给plm
 *
 * @author ChenWenLong
 * @date 2023/8/12 16:50
 */
@Slf4j
@Component
public class DevelopCompleteNoticeHandler implements AsyncSendMqHandler<DevelopSampleCompleteNoticeMqDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid DevelopSampleCompleteNoticeMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
