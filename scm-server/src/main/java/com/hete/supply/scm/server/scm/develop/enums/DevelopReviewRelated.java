package com.hete.supply.scm.server.scm.develop.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: weiwenxin
 * @date: 2023-10-30 11:20:20
 */
@AllArgsConstructor
@Getter
public enum DevelopReviewRelated implements IRemark {

    // 审版关联单据类型
    PURCHASE("采购"),
    DEVELOP_SAMPLE("开发样品"),
    NORMAL("普通"),
    ;

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
