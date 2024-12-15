package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.common.service.base.IContainer;
import com.hete.supply.scm.server.scm.qc.handler.ContainerHandler;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.ContainerListQueryDto;
import com.hete.supply.wms.api.basic.entity.dto.ContainerUpdateStateDto;
import com.hete.supply.wms.api.basic.entity.vo.Container4ScmListVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.consistency.core.service.ConsistencyService;

import java.util.List;

/**
 * QcContainer 是一个容器管理类，实现了 IContainer 接口。它用于管理质检过程中的容器状态，包括占用和释放容器。
 * 该类提供了方法用于尝试占用和释放容器，以及构造函数来初始化容器信息。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
public class QcContainer implements IContainer {

    private final String containerCode;
    private final String warehouseCode;
    private final ConsistencyService consistencyService;
    private final WmsRemoteService wmsRemoteService;

    /**
     * 构造一个 QcContainer 的实例。
     *
     * @param containerCode 容器编码
     * @param warehouseCode 仓库编码
     */
    public QcContainer(String containerCode,
                       String warehouseCode) {
        validateParameters(containerCode, warehouseCode);
        this.containerCode = containerCode;
        this.warehouseCode = warehouseCode;
        this.consistencyService = SpringUtil.getBean(ConsistencyService.class);
        this.wmsRemoteService = SpringUtil.getBean(WmsRemoteService.class);
    }

    /**
     * 尝试占用容器的方法。
     *
     * @return 如果容器占用成功，返回 true；否则返回 false。
     */
    @Override
    public boolean tryOccupyContainer() {
        final WmsEnum.ContainerState state = WmsEnum.ContainerState.OCCUPIED;
        final ContainerUpdateStateDto containerUpdateStateDto = new ContainerUpdateStateDto();
        containerUpdateStateDto.setContainerCode(containerCode);
        containerUpdateStateDto.setWarehouseCode(warehouseCode);
        containerUpdateStateDto.setState(state);

        validateContainer(containerCode, warehouseCode, WmsEnum.ContainerState.IDLE);
        consistencyService.execAsyncTask(ContainerHandler.class, containerUpdateStateDto);
        return true;
    }

    /**
     * 尝试释放容器的方法。
     *
     * @return 如果容器释放成功，返回 true；否则返回 false。
     */
    @Override
    public boolean tryReleaseContainer() {
        final WmsEnum.ContainerState state = WmsEnum.ContainerState.IDLE;
        final ContainerUpdateStateDto containerUpdateStateDto = new ContainerUpdateStateDto();
        containerUpdateStateDto.setContainerCode(containerCode);
        containerUpdateStateDto.setWarehouseCode(warehouseCode);
        containerUpdateStateDto.setState(state);

        validateContainer(containerCode, warehouseCode);
        consistencyService.execAsyncTask(ContainerHandler.class, containerUpdateStateDto);
        return true;
    }

    private void validateParameters(String containerCode,
                                    String warehouseCode) {
        if (StrUtil.isBlank(containerCode)) {
            throw new BizException("容器编码为空！");
        }
        if (StrUtil.isBlank(warehouseCode)) {
            throw new BizException("仓库编码为空！");
        }
    }

    private void validateContainer(String containerCode,
                                   String warehouseCode,
                                   WmsEnum.ContainerState state) {
        final ContainerListQueryDto containerListQueryDto = new ContainerListQueryDto();
        containerListQueryDto.setContainerCode(containerCode);
        containerListQueryDto.setWarehouseCode(warehouseCode);
        containerListQueryDto.setState(state);

        List<Container4ScmListVo> wmsContainerList = wmsRemoteService.getWmsContainerList(containerListQueryDto);
        if (CollectionUtils.isEmpty(wmsContainerList)) {
            throw new ParamIllegalException("SCM操作容器前置校验异常！容器信息不存在或被占用！仓库编码:{},容器编码:{}",
                    warehouseCode, containerCode);
        }
    }

    private void validateContainer(String containerCode,
                                   String warehouseCode) {
        final ContainerListQueryDto containerListQueryDto = new ContainerListQueryDto();
        containerListQueryDto.setContainerCode(containerCode);
        containerListQueryDto.setWarehouseCode(warehouseCode);

        List<Container4ScmListVo> wmsContainerList = wmsRemoteService.getWmsContainerList(containerListQueryDto);
        if (CollectionUtils.isEmpty(wmsContainerList)) {
            throw new ParamIllegalException("SCM操作容器前置校验异常！容器信息不存在！仓库编码:{},容器编码:{}",
                    warehouseCode, containerCode);
        }
    }

}

