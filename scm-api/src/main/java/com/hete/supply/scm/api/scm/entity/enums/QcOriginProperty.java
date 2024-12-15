package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Getter
@AllArgsConstructor
public enum QcOriginProperty implements IRemark {
    // 质检来源属性：质检来源下一级目录
    REPAIR("返修"),
    NORMAL("常规"),
    EXTRA("补单"),
    LIMITED("limited"),
    REWORKING("非limited返工"),
    LIMITED_REWORKING("limited返工"),
    OVERSEAS_REPAIR("海外返修"),
    WH("网红"),
    FIRST_ORDER("首单"),
    PRENATAL("产前样"),
    TT("TT"),
    PROC_REPAIR("加工返修"),
    SPECIAL("特殊"),
    ;

    private final String remark;

}
