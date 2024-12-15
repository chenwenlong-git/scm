package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/12/14 10:47
 */
@Getter
@AllArgsConstructor
public enum FeishuWorkflowType implements IRemark {
    // 飞书审批单编号
    SUPPLIER_PAYMENT_ACCOUNT("scmSupplierPaymentAccount", "供应商收款账户生效审批"),
    SUPPLIER_PAYMENT_ACCOUNT_FAIL("scmSupplierPaymentAccountFail", "供应商收款账户失效审批"),
    IBFS_PREPAYMENT_APPROVE("ibfsPrepaymentApprove", "财务-预付款单发起审批"),
    IBFS_RECO_ORDER_APPROVE("ibfsRecoOrderApprove", "财务-对账款单发起审批"),
    IBFS_SETTLEMENT_APPROVE("ibfsSettlementApprove", "财务-结算单发起审批"),
    ADJUST_PRICE_APPROVE("adjustPriceApprove", "调价审批"),
    ;
    private final String value;
    private final String remark;

    public static FeishuWorkflowType contains(String value) {
        for (FeishuWorkflowType workflowType : values()) {
            if (workflowType.getValue().equals(value)) {
                return workflowType;
            }
        }
        return null;
    }

}
