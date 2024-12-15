package com.hete.supply.scm.server.supplier.develop.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/8/21 10:38
 */
@Getter
@AllArgsConstructor
public enum DevelopPamphletExamine implements IRemark {
    //版单审核
    AGREE_PAMPHLET("开始打版"),
    REFUSE_PAMPHLET("审核拒绝"),
    ;

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }
}
