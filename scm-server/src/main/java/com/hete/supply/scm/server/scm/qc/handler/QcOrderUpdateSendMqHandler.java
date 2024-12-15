package com.hete.supply.scm.server.scm.qc.handler;

import com.hete.supply.scm.server.scm.qc.entity.dto.QcOrderChangeDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import com.hete.support.core.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 质检单信息变更handler
 *
 * @author yanjiawei
 * Created on 2023/10/12.
 */
@Slf4j
@Component
public class QcOrderUpdateSendMqHandler implements AsyncSendMqHandler<QcOrderChangeDto> {

    private String beanName;

    @Override
    public void failed(@NotNull @Valid QcOrderChangeDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("质检单信息变更推送WMS失败！request:{},error:{}", JacksonUtil.parse2Str(request), failCallbackBo.getBizErrorMsg());
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
