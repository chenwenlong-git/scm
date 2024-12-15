package com.hete.supply.scm.server.supplier.handler;

import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncSendMqHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/6/7 11:44
 */
@Slf4j
@Component
public class ScmExportHandler implements AsyncSendMqHandler<FileOperateMessageDto> {

    private String beanName;

    @Override
    public void failed(@NotNull @Valid FileOperateMessageDto request, @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("发送采购原料导出消息失败，{}", request.getBizType());
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
