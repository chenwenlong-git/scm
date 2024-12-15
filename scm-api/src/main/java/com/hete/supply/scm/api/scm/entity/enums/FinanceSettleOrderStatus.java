package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:17
 */
@AllArgsConstructor
@Getter
public enum FinanceSettleOrderStatus implements IRemark {

    // 结算单状态
    WAIT_SUPPLIER_SUBMIT("待工厂提交"),
    WAIT_FOLLOWUP_CONFIRM("待跟单确认"),
    IN_APPROVAL("审批中"),
    WAIT_SETTLEMENT("待结算"),
    PARTIAL_SETTLEMENT("部分结算"),
    SETTLE_COMPLETED("结算完成"),
    INVALIDATE("已作废"),
    FEISHU_APPROVAL_PROCESSING("飞书审批处理中");

    private final String remark;
}
