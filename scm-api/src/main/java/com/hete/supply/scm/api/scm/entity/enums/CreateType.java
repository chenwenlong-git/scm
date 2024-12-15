package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 魏文鑫
 * @date 2023-09-25 21:28:18
 */
@Getter
@AllArgsConstructor
public enum CreateType implements IRemark {
    //创建类型:CREATE(首次创建),SUPERPOSITION(叠加),
    CREATE("首次创建"),
    SUPERPOSITION("叠加"),

    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
