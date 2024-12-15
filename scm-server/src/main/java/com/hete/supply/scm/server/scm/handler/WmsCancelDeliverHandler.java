package com.hete.supply.scm.server.scm.handler;

import com.hete.supply.scm.server.scm.entity.dto.WmsCancelDeliverMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 通知wms采购单取消发货
 *
 * @author weiwenxin
 * @date 2023/1/3 09:59
 */
@Slf4j
@Component
public class WmsCancelDeliverHandler implements AsyncSendMqHandler<WmsCancelDeliverMqDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid WmsCancelDeliverMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
