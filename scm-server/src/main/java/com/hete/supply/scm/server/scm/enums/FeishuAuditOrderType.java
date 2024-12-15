package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/12/5 18:17
 */
@Getter
@AllArgsConstructor
public enum FeishuAuditOrderType implements IRemark {
    //飞书审批单单据类型
    SUPPLIER_PAYMENT_ACCOUNT("供应商收款账号"),
    IBFS_PREPAYMENT_APPROVE("预付款审批"),
    IFBS_RECO_ORDER_APPROVE("对账单审批"),
    IFBS_SETTLEMENT_APPROVE("结算单审批"),
    ADJUST_PRICE_APPROVE("调价审批"),

    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }


}
