package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Getter
@AllArgsConstructor
public enum QcState implements IRemark {
    // 质检状态
    WAIT_HAND_OVER("待交接"),
    TO_BE_QC("待质检"),
    QCING("质检中"),
    TO_BE_AUDIT("待审核"),
    DEFECT_HANDLING("次品处理中"),
    FINISHED("已完结");
    private final String remark;

    public QcState toBeQc() {
        if (WAIT_HAND_OVER != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return TO_BE_QC;
    }

    public QcState unPassedToBeQc() {
        if (TO_BE_AUDIT != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return TO_BE_QC;
    }

    public QcState resetQc() {
        if (QCING != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return TO_BE_QC;
    }

    public QcState toQcing() {
        if (TO_BE_QC != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return QCING;
    }

    public QcState toCompletedQc(QcResult qcResult) {
        if (QCING != this && TO_BE_QC != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (QcResult.PASSED.equals(qcResult)) {
            return FINISHED;
        }
        return TO_BE_AUDIT;
    }

    public QcState toDefectHandling() {
        if (TO_BE_AUDIT != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return DEFECT_HANDLING;
    }

    public QcState toFinished() {
        if (TO_BE_AUDIT != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return FINISHED;
    }

    public QcState defectToFinish() {
        if (DEFECT_HANDLING != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return FINISHED;
    }

    public QcState toFailCompromise() {
        if (FINISHED != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return DEFECT_HANDLING;
    }
}
