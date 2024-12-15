package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/10/15.
 */
@Getter
@AllArgsConstructor
public enum CrotchLength implements IRemark {
    //供应商商品原料裆长尺寸枚举
    SHORT("短尺寸"),
    MIDDLE("中尺寸"),
    LONG("长尺寸"),

    LENGTH_2("2"),
    LENGTH_4("4"),
    LENGTH_6("6"),
    LENGTH_8("8"),
    LENGTH_10("10"),
    LENGTH_12("12"),
    LENGTH_14("14"),
    LENGTH_16("16"),
    LENGTH_18("18"),
    LENGTH_20("20"),
    LENGTH_22("22"),
    LENGTH_24("24"),
    LENGTH_26("26"),
    LENGTH_28("28"),
    LENGTH_30("30"),
    LENGTH_32("32"),
    LENGTH_34("34"),
    LENGTH_36("36"),
    LENGTH_38("38"),
    LENGTH_40("40"),
    ;
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }

    public static CrotchLength parse(String desc) {
        if (null == desc) {
            return null;
        }

        for (CrotchLength value : values()) {
            if (value.desc.equals(desc)) {
                return value;
            }
        }
        return null;
    }

}
