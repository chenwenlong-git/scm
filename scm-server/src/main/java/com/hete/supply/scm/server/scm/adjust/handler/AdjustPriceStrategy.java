package com.hete.supply.scm.server.scm.adjust.handler;

import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import com.hete.support.core.handler.BaseEnumHandler;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/6/20 17:27
 */
public interface AdjustPriceStrategy extends BaseEnumHandler<ApproveType> {


    /**
     * 同意处理
     *
     * @param adjustPriceApprovePo
     */
    void agreeHandle(AdjustPriceApprovePo adjustPriceApprovePo);

    /**
     * 拒绝处理
     *
     * @param adjustPriceApprovePo
     */
    void refuseHandle(AdjustPriceApprovePo adjustPriceApprovePo);

    /**
     * 发起失败处理
     *
     * @param adjustPriceApprovePo
     */
    void failHandle(AdjustPriceApprovePo adjustPriceApprovePo);

    /**
     * 设置Handler类型
     */
    @NotNull
    @Override
    ApproveType getHandlerType();
}
