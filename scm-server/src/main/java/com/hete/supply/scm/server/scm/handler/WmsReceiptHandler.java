package com.hete.supply.scm.server.scm.handler;

import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * wms创建收货单消费mq
 *
 * @Author: RockyHuas
 * @date: 2022/11/19 11:50
 */
@Slf4j
@Component
public class WmsReceiptHandler implements AsyncSendMqHandler<ReceiveOrderCreateMqDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid ReceiveOrderCreateMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
