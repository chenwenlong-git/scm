package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2022/12/2 20:30
 */
@AllArgsConstructor
@Getter
public enum Measurement implements IRemark {
    // 计量单位:VOLUME(卷),CENTIMETER(厘米),BAG(袋),BUCKET(桶),BRANCH(支),SHEET(张),HANDFUL(把),BOX(盒),PAIR(双),STRIP(条),BOTTLE(瓶),METRE(米),INDIVIDUAL(个),
    VOLUME("卷"),
    CENTIMETER("厘米"),
    BAG("袋"),
    BUCKET("桶"),
    BRANCH("支"),
    SHEET("张"),
    HANDFUL("把"),
    BOX("盒"),
    PAIR("双"),
    STRIP("条"),
    BOTTLE("瓶"),
    METRE("米"),
    INDIVIDUAL("个"),
    ;

    private final String remark;

    private static final Map<String, Measurement> MEASUREMENT_MAP = new HashMap<>();

    static {
        for (Measurement value : values()) {
            MEASUREMENT_MAP.put(value.getRemark(), value);
        }
    }

    public static Measurement getByRemark(String remark) {
        return MEASUREMENT_MAP.get(remark);
    }
}
