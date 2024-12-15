package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/8/4 17:34
 */
@Getter
@AllArgsConstructor
public enum DevelopPamphletOrderStatus implements IRemark {
    // 版单结果
    TO_BE_CONFIRMED_PAMPHLET("待打版"),
    PAMPHLET_MAKING("打版中"),
    COMPLETED_PAMPHLET("完成打版"),
    CANCEL_PAMPHLET("取消打版"),
    NOT_SAMPLE_PAMPHLET("无需打样"),
    ;

    private final String remark;

    public static List<DevelopPamphletOrderStatus> getValues() {
        return Arrays.stream(DevelopPamphletOrderStatus.values())
                .filter(status -> status != DevelopPamphletOrderStatus.NOT_SAMPLE_PAMPHLET)
                .collect(Collectors.toList());
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    public DevelopPamphletOrderStatus cancel() {
        if (!(TO_BE_CONFIRMED_PAMPHLET == this || CANCEL_PAMPHLET == this)) {
            throw new ParamIllegalException("版单处于{}状态进行操作，请刷新页面后重试！", TO_BE_CONFIRMED_PAMPHLET.getRemark());
        }
        return CANCEL_PAMPHLET;
    }

    public DevelopPamphletOrderStatus toCompletedPamphlet() {
        if (PAMPHLET_MAKING != this) {
            throw new ParamIllegalException("版单处于{}状态进行操作，请刷新页面后重试！", PAMPHLET_MAKING.getRemark());
        }
        return COMPLETED_PAMPHLET;
    }

    public static List<DevelopPamphletOrderStatus> developPamphletMenuList() {
        return Arrays.asList(TO_BE_CONFIRMED_PAMPHLET, PAMPHLET_MAKING, CANCEL_PAMPHLET);
    }

    public void verifyToBeConfirmedPamphlet() {
        if (TO_BE_CONFIRMED_PAMPHLET != this) {
            throw new ParamIllegalException("版单状态处于{}时进行操作，请刷新页面后重试！", TO_BE_CONFIRMED_PAMPHLET.getRemark());
        }
    }

}
