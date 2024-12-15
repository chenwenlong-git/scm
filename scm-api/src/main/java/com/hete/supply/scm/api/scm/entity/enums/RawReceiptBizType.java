package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/17 11:05
 */
@Getter
@AllArgsConstructor
public enum RawReceiptBizType implements IRemark {
    // 原料收货类型
    PURCHASE("采购原料"),
    @Deprecated // 旧样品单已弃用，不再使用
    SAMPLE("样品原料"),
    DEVELOP("样品原料"),
    ;

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

    /**
     * 返回样品相关的SAMPLE和DEVELOP，兼容新旧枚举的列表检索
     */
    public static List<RawReceiptBizType> getRelatedTypes() {
        return Arrays.asList(SAMPLE, DEVELOP);
    }
}
