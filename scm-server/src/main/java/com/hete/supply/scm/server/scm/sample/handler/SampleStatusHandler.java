package com.hete.supply.scm.server.scm.sample.handler;

import com.hete.supply.scm.server.scm.sample.entity.dto.SampleStatusDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 样品采购单状态变更给plm推送mq
 *
 * @author weiwenxin
 * @date 2022/11/23 15:55
 */
@Slf4j
@Component
public class SampleStatusHandler implements AsyncSendMqHandler<SampleStatusDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid SampleStatusDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
