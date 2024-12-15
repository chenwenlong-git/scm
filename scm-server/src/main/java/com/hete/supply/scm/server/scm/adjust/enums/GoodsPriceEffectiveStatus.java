package com.hete.supply.scm.server.scm.adjust.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/7/23 14:46
 */
@Getter
@AllArgsConstructor
public enum GoodsPriceEffectiveStatus implements IRemark {
    // 生效状态：WAIT_EXAMINE("待审核"),WAIT_EFFECTIVE("待生效"),EFFECTIVE("当前生效"),INVALID("失效")
    WAIT_EXAMINE("待审核"),
    WAIT_EFFECTIVE("待生效"),
    EFFECTIVE("当前生效"),
    INVALID("失效"),
    ;

    private final String remark;

    public static List<GoodsPriceEffectiveStatus> getCloseEffectiveStatusList() {
        return List.of(WAIT_EFFECTIVE, EFFECTIVE);
    }
}
