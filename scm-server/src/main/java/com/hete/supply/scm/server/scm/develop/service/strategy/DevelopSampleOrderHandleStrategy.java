package com.hete.supply.scm.server.scm.develop.service.strategy;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderSubmitHandleDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.support.core.handler.BaseEnumHandler;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/3/23 22:38
 */
public interface DevelopSampleOrderHandleStrategy extends BaseEnumHandler<DevelopSampleMethod> {

    /**
     * 前置操作检验入参
     *
     * @param dtoList:
     * @param developSampleOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/25 10:11
     */
    void submitHandleVerify(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                            List<DevelopSampleOrderPo> developSampleOrderPoList);

    /**
     * 相关业务单据逻辑处理
     *
     * @param dtoList:
     * @param developSampleOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/23 22:41
     */
    void developSampleOrderSubmitHandle(List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> dtoList,
                                        List<DevelopSampleOrderPo> developSampleOrderPoList);

    /**
     * 设置Handler类型
     */
    @NotNull
    @Override
    DevelopSampleMethod getHandlerType();
}
