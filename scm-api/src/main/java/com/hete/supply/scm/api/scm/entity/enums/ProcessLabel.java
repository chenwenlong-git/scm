package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: RockyHuas
 * @date: 2023/03/13 09:02
 */
@AllArgsConstructor
@Getter
public enum ProcessLabel implements IRemark {

    // 工序标签
    PLUCK_HAIR("拔毛"),
    BRAIDED_HAIR("编发造型"),
    SEWING_HEAD("缝头套"),
    CLIPS("卡子/松紧带"),
    ICON_PERM("离子烫"),
    FLOATING("漂扣"),
    CLEAN("清洗"),
    DYEING("染色"),
    DOLL_HAIR("娃娃发"),
    BLOCK_LACE("修剪发块蕾丝"),
    MODELING("造型"),
    SPECIAL_PROCEDURE("特殊工序"),
    HOT_PERM("热烫");


    private final String desc;
    private static final Map<String, ProcessLabel> PROCESS_LABEL_MAP = new HashMap<>();

    static {
        for (ProcessLabel value : values()) {
            PROCESS_LABEL_MAP.put(value.getDesc(), value);
        }
    }


    public static ProcessLabel getByDesc(String desc) {

        return PROCESS_LABEL_MAP.get(desc);
    }

    @Override
    public String getRemark() {
        return this.desc;
    }
}
