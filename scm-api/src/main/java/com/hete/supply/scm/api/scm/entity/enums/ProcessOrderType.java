package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 15:02
 */
@AllArgsConstructor
@Getter
public enum ProcessOrderType implements IRemark {
    // 加工单类型
    NORMAL("常规"),
    EXTRA("补单"),
    LIMITED("limited"),
    REWORKING("非limited返工"),
    LIMITED_REWORKING("limited返工"),
    OVERSEAS_REPAIR("海外返修"),
    WH("网红"),
    REPAIR("返修"),
    ;

    private final String desc;

    private static final Map<String, ProcessOrderType> PROCESS_ORDER_TYPE_MAP = new HashMap<>();


    static {
        for (ProcessOrderType value : values()) {
            PROCESS_ORDER_TYPE_MAP.put(value.getDesc(), value);
        }
    }


    public static ProcessOrderType getByDesc(String desc) {

        return PROCESS_ORDER_TYPE_MAP.get(desc);
    }

    @Override
    public String getRemark() {
        return this.desc;
    }
}
