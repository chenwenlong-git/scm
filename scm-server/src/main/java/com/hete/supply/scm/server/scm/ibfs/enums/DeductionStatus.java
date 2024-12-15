package com.hete.supply.scm.server.scm.ibfs.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:37
 */
@Getter
@AllArgsConstructor
public enum DeductionStatus implements IRemark {
    // 抵扣状态:UNASSOCIATED(未关联对账单),ASSOCIATED(已关联对账单),
    UNASSOCIATED("未关联对账单"),
    ASSOCIATED("已关联对账单"),

    ;
    private final String remark;
}
