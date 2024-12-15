package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/8/12 17:10
 */
@Getter
@AllArgsConstructor
public enum DevelopCancelResult implements IRemark {
    // 开发母单取消结果:
    PAMPHLET("成功"),
    REVIEW("失败");

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }


}
