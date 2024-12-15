package com.hete.supply.scm.server.supplier.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/19 14:24
 */
@Getter
@AllArgsConstructor
public enum CategoryLevel implements IRemark {
    // 品类级别:FIRST_CATEGORY(一级品类),SECOND_CATEGORY(二级品类),THIRD_CATEGORY(三级品类),
    FIRST_CATEGORY("一级品类"),
    SECOND_CATEGORY("二级品类"),
    THIRD_CATEGORY("三级品类"),
    ;

    private final String remark;

    public CategoryLevel toNextLevel() {
        if (FIRST_CATEGORY.equals(this)) {
            return SECOND_CATEGORY;
        } else if (SECOND_CATEGORY.equals(this)) {
            return THIRD_CATEGORY;
        }
        throw new BizException("错误的辅料类目级别，请重新添加辅料类目");
    }
}
