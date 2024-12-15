package com.hete.supply.scm.server.scm.settle.enums;

import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import com.hete.support.api.constant.IRemark;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplementOrderExamine implements IRemark {
    //补款单审核
    PRICE_CONFIRM_AGREE("价格确认通过"),
    PRICE_CONFIRM_REFUSE("价格确认拒绝"),
    ALREADY_CONFIRM("已提交"),
    CONFIRM_AGREE("确认通过"),
    CONFIRM_REFUSE("确认拒绝"),
    EXAMINE_AGREE("审核通过"),
    EXAMINE_REFUSE("审核拒绝");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    /**
     * 验证补款单的类型转换审批权限
     *
     * @param supplementStatus:
     * @param supplementType:
     * @return supplementOrderExamine:
     * @author ChenWenLong
     * @date 2024/7/11 16:31
     */
    public static SupplementOrderExamine converterType(SupplementStatus supplementStatus,
                                                       SupplementType supplementType,
                                                       BooleanType examine) {
        // 价差补款时待提交审核通过
        if (SupplementStatus.WAIT_SUBMIT.equals(supplementStatus)
                && SupplementType.PRICE.equals(supplementType)
                && BooleanType.TRUE.equals(examine)) {
            return ALREADY_CONFIRM;
            // 非价差补款时提交
        } else if (SupplementStatus.WAIT_SUBMIT.equals(supplementStatus)
                && !SupplementType.PRICE.equals(supplementType)
                && BooleanType.TRUE.equals(examine)) {
            return ALREADY_CONFIRM;
            // 价差补款时价格确认同意
        } else if (SupplementStatus.WAIT_PRICE.equals(supplementStatus)
                && SupplementType.PRICE.equals(supplementType)
                && BooleanType.TRUE.equals(examine)) {
            return PRICE_CONFIRM_AGREE;
            // 价差补款时价格确认拒绝
        } else if (SupplementStatus.WAIT_PRICE.equals(supplementStatus)
                && SupplementType.PRICE.equals(supplementType)
                && BooleanType.FALSE.equals(examine)) {
            return PRICE_CONFIRM_REFUSE;
            // 供应商确认同意
        } else if (SupplementStatus.WAIT_CONFIRM.equals(supplementStatus)
                && BooleanType.TRUE.equals(examine)) {
            return CONFIRM_AGREE;
            // 供应商确认拒绝
        } else if (SupplementStatus.WAIT_CONFIRM.equals(supplementStatus)
                && BooleanType.FALSE.equals(examine)) {
            return CONFIRM_REFUSE;
            // 审批同意
        } else if (SupplementStatus.WAIT_EXAMINE.equals(supplementStatus)
                && BooleanType.TRUE.equals(examine)) {
            return EXAMINE_AGREE;
            // 审批拒绝
        } else if (SupplementStatus.WAIT_EXAMINE.equals(supplementStatus)
                && BooleanType.FALSE.equals(examine)) {
            return EXAMINE_REFUSE;
        } else {
            throw new BizException("补款单的审批类型转换失败，请联系管理员！");
        }
    }
}
