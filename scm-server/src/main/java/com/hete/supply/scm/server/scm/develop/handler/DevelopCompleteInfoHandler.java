package com.hete.supply.scm.server.scm.develop.handler;

import com.hete.supply.scm.api.scm.entity.dto.DevelopCompleteInfoMqDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 齐备信息推送给plm创建spu/sku
 *
 * @author ChenWenLong
 * @date 2023/8/12 16:50
 */
@Slf4j
@Component
public class DevelopCompleteInfoHandler implements AsyncSendMqHandler<DevelopCompleteInfoMqDto> {
    private String beanName;

    @Override
    public void failed(@NotNull @Valid DevelopCompleteInfoMqDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

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
