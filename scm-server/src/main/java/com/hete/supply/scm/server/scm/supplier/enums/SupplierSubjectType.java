package com.hete.supply.scm.server.scm.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/5/15 13:52
 */
@Getter
@AllArgsConstructor
public enum SupplierSubjectType implements IRemark {
    // 主体类型
    PERSONAL("个人"),
    COMPANY("企业"),
    ;

    private final String remark;


    /**
     * 通过字符串转换枚举
     *
     * @param value:
     * @return SupplierSubjectType
     * @author ChenWenLong
     * @date 2024/5/15 13:56
     */
    public static SupplierSubjectType stringConvert(String value) {
        for (SupplierSubjectType type : SupplierSubjectType.values()) {
            if (type.getRemark().equals(value)) {
                return type;
            }
        }
        return null;
    }

}
