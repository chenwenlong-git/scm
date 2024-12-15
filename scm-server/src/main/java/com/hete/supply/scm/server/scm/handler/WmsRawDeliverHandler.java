package com.hete.supply.scm.server.scm.handler;

import com.hete.supply.scm.server.scm.entity.dto.DeliveryOrderCreateMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 通知wms创建原料发货单
 *
 * @author weiwenxin
 * @date 2022/12/28 15:28
 */
@Slf4j
@Component
public class WmsRawDeliverHandler implements AsyncSendMqHandler<DeliveryOrderCreateMqDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid DeliveryOrderCreateMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
