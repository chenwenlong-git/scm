package com.hete.supply.scm.server.supplier.purchase.enums;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author weiwenxin
 * @date 2022/11/17 11:05
 */
@Getter
@AllArgsConstructor
public enum PurchaseCtrlType implements IRemark {
    // 供应商采购操作类型
    SCHEDULING("确认排产", PurchaseOrderStatus::toWaitCommissioning),
    COMMISSIONING("确认投产", PurchaseOrderStatus::toCommission),
    ENTRY_PRETREATMENT("进入前处理", PurchaseOrderStatus::toPretreatment),
    COMPLETE_PRETREATMENT("完成前处理", PurchaseOrderStatus::toSewing),
    COMPLETE_SEWING("完成缝制", PurchaseOrderStatus::toAfterTreatment),
    COMPLETE_AFTERTREATMENT("完成后处理", PurchaseOrderStatus::toPostQc),
    COMPLETE_QC("完成质检", PurchaseOrderStatus::toWaitDeliver),
    ;

    private final String remark;

    private final Function<PurchaseOrderStatus, PurchaseOrderStatus> function;

    @Override
    public String getRemark() {
        return this.remark;
    }
}
