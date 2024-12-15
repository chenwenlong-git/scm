package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:37
 */
@Getter
@AllArgsConstructor
public enum DevelopPricingOrderStatus implements IRemark {
    //状态
    WAIT_SUBMIT_PRICE("待提交核价"),
    WAIT_PRICE("待核价"),
    ALREADY_PRICE("已核价"),
    CANCEL_PRICE("取消核价");

    private final String remark;

    public static List<DevelopPricingOrderStatus> developPricingMenuList() {
        return Arrays.asList(WAIT_SUBMIT_PRICE, WAIT_PRICE);
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    public static List<DevelopPricingOrderStatus> getValues() {
        return Arrays.asList(DevelopPricingOrderStatus.values());
    }

    public DevelopPricingOrderStatus toWaitPrice() {
        if (WAIT_SUBMIT_PRICE != this) {
            throw new ParamIllegalException("状态处于{}时才进行操作，请刷新页面后重试！", WAIT_SUBMIT_PRICE.getRemark());
        }
        return WAIT_PRICE;
    }

    public DevelopPricingOrderStatus toAlreadyPrice() {
        if (WAIT_PRICE != this) {
            throw new ParamIllegalException("状态处于{}时才进行操作，请刷新页面后重试！", WAIT_PRICE.getRemark());
        }
        return ALREADY_PRICE;
    }

    public static DevelopPricingOrderStatus isEnums(String status) {
        for (DevelopPricingOrderStatus value : DevelopPricingOrderStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
