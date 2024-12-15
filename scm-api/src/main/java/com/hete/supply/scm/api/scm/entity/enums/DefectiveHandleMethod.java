package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2023/05/30 09:02
 */
@AllArgsConstructor
@Getter
public enum DefectiveHandleMethod implements IRemark {

    /**
     * 次品报废
     */
    DEFECTIVE_SCRAP("次品报废"),

    /**
     * 次品返工
     */
    DEFECTIVE_REWORK("次品返工"),

    /**
     * 次品退供
     */
    DEFECTIVE_RETURN_SUPPLIER("加工次品"),

    /**
     * 归还原料
     */
    RETURN_MATERIAL("归还原料"),

    ;
    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
