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
public enum AttributeIsRequired implements IRemark {
    //是
    YES("是"),
    //否
    NO("否"),
    ;

    private final String desc;

    public static AttributeIsRequired getByDesc(String desc) {
        return Arrays.stream(AttributeIsRequired.values())
                .filter(item -> item.getDesc().equals(desc))
                .findFirst().orElse(null);
    }

    @Override
    public String getRemark() {
        return this.desc;
    }
}
