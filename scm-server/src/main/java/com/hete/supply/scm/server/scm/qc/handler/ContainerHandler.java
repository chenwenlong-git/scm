package com.hete.supply.scm.server.scm.qc.handler;

import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.ContainerUpdateStateDto;
import com.hete.supply.wms.api.basic.entity.vo.ContainerUpdateStateVo;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2023/11/9.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ContainerHandler implements AsyncHandler<ContainerUpdateStateDto> {

    private String beanName;
    private final WmsRemoteService wmsRemoteService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid ContainerUpdateStateDto request) {
        final String containerCode = request.getContainerCode();
        final String warehouseCode = request.getWarehouseCode();
        final WmsEnum.ContainerState state = request.getState();

        final ContainerUpdateStateVo containerUpdateResult = wmsRemoteService.updateContainerState(
                request);
        return wmsRemoteService.isContainerUpdateStateMatch(containerUpdateResult, containerCode, warehouseCode, state);
    }

    @Override
    public void failed(@NotNull @Valid ContainerUpdateStateDto request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("{}容器失败！仓库编号:{}，容器编号：{}", request.getState()
                .getRemark(), request.getWarehouseCode(), request.getContainerCode());
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
