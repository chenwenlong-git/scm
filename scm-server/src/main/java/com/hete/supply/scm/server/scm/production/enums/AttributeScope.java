package com.hete.supply.scm.server.scm.production.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@AllArgsConstructor
@Getter
public enum AttributeScope implements IRemark {
    //供应商
    SUPPLIER("供应商"),
    //商品
    GOODS("商品"),
    ;

    private final String desc;

    public static AttributeScope getByDesc(String desc) {
        return Arrays.stream(AttributeScope.values())
                .filter(item -> item.getDesc().equals(desc))
                .findFirst().orElse(null);
    }

    @Override
    public String getRemark() {
        return this.desc;
    }
}
