package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/2 16:02
 */
@Getter
@AllArgsConstructor
public enum SampleOrderStatus implements IRemark {
    // 样品单状态
    WAIT_DISBURSEMENT("待接款", 1),
    WAIT_TYPESET("待打版", 2),
    WAIT_RECEIVE_ORDER("待接单", 3),
    RECEIVED_ORDER("已接单", 5),
    TYPESETTING("打版中", 6),
    WAIT_RECEIVED_SAMPLE("待收样", 7),
    WAIT_SAMPLE("待选样", 8),
    SELECTED("已选中", 9),
    SETTLE("已结算", 10),
    PROOFING_FAIL("打样失败", 11),
    REFUSED("已拒绝", 12),
    DELETED("作废", 13),
    ;

    private final String name;
    private final Integer sort;

    public static SampleOrderStatus getEarliestStatus(List<SampleOrderStatus> statusList) {
        if (statusList == null || statusList.isEmpty()) {
            throw new BizException("错误的状态列表，无法获取最早状态。");
        }
        SampleOrderStatus sampleOrderStatus = statusList.get(0);

        for (SampleOrderStatus status : statusList) {
            if (status.getSort() < sampleOrderStatus.getSort()) {
                sampleOrderStatus = status;
            }
        }

        return sampleOrderStatus;
    }

    public static List<SampleOrderStatus> getSupplierAllStatusList() {
        final List<SampleOrderStatus> statusList = Arrays.asList(SampleOrderStatus.values());
        return statusList.stream()
                .filter(status -> status.getSort() >= WAIT_RECEIVE_ORDER.getSort())
                .collect(Collectors.toList());
    }


    @Override
    public String getRemark() {
        return this.name;
    }


    public SampleOrderStatus toWaitTypeset() {
        if (WAIT_DISBURSEMENT != this) {
            throw new ParamIllegalException("当前样品需求单不处于【{}】，无法进行开款操作，请刷新后重试！",
                    WAIT_DISBURSEMENT.getRemark());
        }
        return WAIT_TYPESET;
    }

    public SampleOrderStatus toWaitReceiveOrder() {
        if (WAIT_TYPESET != this) {
            throw new ParamIllegalException("当前样品需求单不处于【{}】，无法进行下发打版操作，请刷新后重试！",
                    WAIT_TYPESET.getRemark());
        }
        return WAIT_RECEIVE_ORDER;
    }

    public SampleOrderStatus toReceivedOrder() {
        if (WAIT_RECEIVE_ORDER != this) {
            throw new ParamIllegalException("当前样品需求单不处于【{}】，无法进行接单操作，请刷新后重试！",
                    WAIT_RECEIVE_ORDER.getRemark());
        }
        return RECEIVED_ORDER;
    }

    public SampleOrderStatus toRefused() {
        if (WAIT_RECEIVE_ORDER != this) {
            throw new ParamIllegalException("当前样品需求单不处于【{}】，无法进行接单操作，请刷新后重试！",
                    WAIT_RECEIVE_ORDER.getRemark());
        }
        return REFUSED;
    }

    public SampleOrderStatus toSelected() {
        if (WAIT_SAMPLE != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】,打样操作失败，请刷新后重试！",
                    WAIT_SAMPLE.getRemark());
        }
        return SELECTED;
    }

    public SampleOrderStatus toProofingFail() {
        if (WAIT_SAMPLE != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】,打样操作失败，请刷新后重试！",
                    WAIT_SAMPLE.getRemark());
        }
        return PROOFING_FAIL;
    }

    public SampleOrderStatus toReSample() {
        if (PROOFING_FAIL != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】,打样操作失败，请刷新后重试！",
                    PROOFING_FAIL.getRemark());
        }
        return TYPESETTING;
    }


    public SampleOrderStatus toTypesetting() {
        if (RECEIVED_ORDER != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】,确认打版操作失败，请刷新后重试！",
                    RECEIVED_ORDER.getRemark());
        }
        return TYPESETTING;
    }

    public SampleOrderStatus toCancelDeliver() {
        if (WAIT_RECEIVED_SAMPLE != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】,取消发货操作失败，请刷新后重试！",
                    WAIT_RECEIVED_SAMPLE.name);
        }
        return TYPESETTING;
    }


    public SampleOrderStatus toWaitReceivedSample() {
        if (TYPESETTING != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】,确认发货操作失败，请刷新后重试！",
                    TYPESETTING.getRemark());
        }
        return WAIT_RECEIVED_SAMPLE;
    }

    public SampleOrderStatus toWaitSample() {
        if (WAIT_RECEIVED_SAMPLE != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】,确认收样操作失败，请刷新后重试！",
                    WAIT_RECEIVED_SAMPLE.getRemark());
        }
        return WAIT_SAMPLE;
    }

    public SampleOrderStatus toSettle() {
        if (RECEIVED_ORDER != this && TYPESETTING != this) {
            throw new ParamIllegalException("当前样品采购子单状态不在【{}】或【{}】，无法变更为【已结算】,操作失败，请刷新后重试！",
                    RECEIVED_ORDER.getRemark(), TYPESETTING.getRemark());
        }
        return SETTLE;
    }

    public static Boolean editChildCheck(SampleOrderStatus sampleOrderStatus) {
        return SampleOrderStatus.SELECTED.equals(sampleOrderStatus) || SampleOrderStatus.SETTLE.equals(sampleOrderStatus);
    }

    public static Boolean returnRawCheck(SampleOrderStatus sampleOrderStatus) {
        return SampleOrderStatus.RECEIVED_ORDER.equals(sampleOrderStatus) || SampleOrderStatus.TYPESETTING.equals(sampleOrderStatus) || SampleOrderStatus.DELETED.equals(sampleOrderStatus);
    }

    public void toProduceLabelCheck() {
        if (!(SELECTED == this || SETTLE == this)) {
            throw new ParamIllegalException("请选择状态在【{}】或【{}】的样品子单", SELECTED.getRemark(), SETTLE.getRemark());
        }
    }
}
