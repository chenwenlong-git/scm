package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author rockyHuas
 * @date 2023/06/27 09:18
 */
@Getter
@AllArgsConstructor
public enum ReturnType implements IRemark {
    // 退货类型
    QC_NOT_PASSED_ALL("质检不合格(整单)"),
    QC_NOT_PASSED_PART("质检不合格(部分)"),
    PROCESS_DEFECT("加工次品"),
    INSIDE_CHECK("库内抽检"),
    RECEIVE_REJECT("收货拒收"),
    MATERIAL_DEFECT("原料次品");

    private final String remark;

    public static ReturnType defectHandlingTypeConvert(DefectHandlingType defectHandlingType) {
        if (DefectHandlingType.BULK_DEFECT.equals(defectHandlingType)) {
            return ReturnType.QC_NOT_PASSED_PART;
        }
        if (DefectHandlingType.PROCESS_DEFECT.equals(defectHandlingType)) {
            return ReturnType.PROCESS_DEFECT;
        }
        if (DefectHandlingType.INSIDE_DEFECT.equals(defectHandlingType)) {
            return ReturnType.INSIDE_CHECK;
        }
        if (DefectHandlingType.MATERIAL_DEFECT.equals(defectHandlingType)) {
            return ReturnType.MATERIAL_DEFECT;
        }

        return null;
    }

    public static List<ReturnType> getFinanceReturnTypeList() {
        return Arrays.asList(PROCESS_DEFECT, INSIDE_CHECK, MATERIAL_DEFECT);
    }
}
