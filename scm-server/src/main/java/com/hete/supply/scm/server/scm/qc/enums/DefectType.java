package com.hete.supply.scm.server.scm.qc.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2023-10-25 14:06:23
 */
@Getter
@AllArgsConstructor
public enum DefectType implements IRemark {
    // 次品处理类型
    EXCEPTION("异常"),
    RETURN("退货单"),
    DEFECT("次品处理记录"),
    ;

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

}
