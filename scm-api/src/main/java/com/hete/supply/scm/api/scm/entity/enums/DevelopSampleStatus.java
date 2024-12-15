package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:37
 */
@Getter
@AllArgsConstructor
public enum DevelopSampleStatus implements IRemark {
    //状态
    WAIT_SEND_SAMPLES("待寄样"),
    ALREADY_SEND_SAMPLES("已寄样"),
    DOCUMENTARY_RECEIPT("跟单签收"),
    ALREADY_GET_REVIEW("已交接审版"),
    ALREADY_GET_PRICING("已交接核价"),
    WAIT_HANDLE("跟单待处理"),
    RETURN_SAMPLES("已退样"),
    RETURN_SAMPLES_RECEIPT("退样已签收"),
    WAIT_RECEIVE("待收货"),
    ON_SHELVES("已上架"),
    CANCELED_SAMPLES("取消样品");

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

    public DevelopSampleStatus toAlreadyGetPricing() {
        if (ALREADY_GET_REVIEW != this) {
            throw new ParamIllegalException("样品单状态处于{}时才进行操作，请刷新页面后重试！", ALREADY_GET_REVIEW.getRemark());
        }
        return ALREADY_GET_PRICING;
    }

    public DevelopSampleStatus toWaitHandle() {
        if (ALREADY_GET_PRICING != this) {
            throw new ParamIllegalException("样品单状态处于{}时才进行操作，请刷新页面后重试！", ALREADY_GET_PRICING.getRemark());
        }
        return WAIT_HANDLE;
    }

    public void submitHandleVerify() {
        if (WAIT_HANDLE != this) {
            throw new ParamIllegalException("样品单状态处于{}时才进行操作，请刷新页面后重试！", WAIT_HANDLE.getRemark());
        }
    }

    public DevelopSampleStatus toReturnSamplesReceipt() {
        if (RETURN_SAMPLES != this) {
            throw new ParamIllegalException("样品单状态处于{}时才进行操作，请刷新页面后重试！", RETURN_SAMPLES.getRemark());
        }
        return RETURN_SAMPLES_RECEIPT;
    }

    public DevelopSampleStatus toDocumentaryReceipt() {
        if (ALREADY_SEND_SAMPLES != this) {
            throw new ParamIllegalException("样品单状态处于{}时才进行操作，请刷新页面后重试！", ALREADY_SEND_SAMPLES.getRemark());
        }
        return DOCUMENTARY_RECEIPT;
    }

    public DevelopSampleStatus toAlreadyGetReview() {
        if (DOCUMENTARY_RECEIPT != this) {
            throw new ParamIllegalException("样品单状态处于{}时才进行操作，请刷新页面后重试！", DOCUMENTARY_RECEIPT.getRemark());
        }
        return ALREADY_GET_REVIEW;
    }

    public DevelopSampleStatus toAlreadySendSamples() {
        if (WAIT_SEND_SAMPLES != this) {
            throw new ParamIllegalException("样品单状态处于{}时才进行操作，请刷新页面后重试！", WAIT_SEND_SAMPLES.getRemark());
        }
        return ALREADY_SEND_SAMPLES;
    }

    /**
     * 齐备信息验证是否修改状态
     *
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/9/25 16:33
     */
    public Boolean verifyCompleteInfo() {
        return ALREADY_GET_REVIEW == this || ALREADY_GET_PRICING == this || WAIT_HANDLE == this;
    }
}
