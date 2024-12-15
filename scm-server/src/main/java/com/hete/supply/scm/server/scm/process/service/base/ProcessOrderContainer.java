package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.constant.ScmConstant;
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
 * ProcessOrderContainer 类实现了 IContainer 接口，用于处理加工单相关的容器操作。
 *
 * @Author yanjiawei
 * @Date 2023/10/24
 */
public class ProcessOrderContainer implements IContainer {

    private final String containerCode;
    private final String warehouseCode = ScmConstant.PROCESS_WAREHOUSE_CODE;
    private final WmsRemoteService wmsRemoteService;

    private final ConsistencyService consistencyService;

    /**
     * 构造函数，接受容器编码作为参数，并初始化对象。
     *
     * @param containerCode 容器编码
     */
    public ProcessOrderContainer(String containerCode) {
        validateParameters(containerCode);
        this.containerCode = containerCode;
        this.wmsRemoteService = SpringUtil.getBean(WmsRemoteService.class);
        this.consistencyService = SpringUtil.getBean(ConsistencyService.class);
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

    private void validateParameters(String containerCode) {
        if (StrUtil.isBlank(containerCode)) {
            throw new BizException("容器编码为空！");
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
