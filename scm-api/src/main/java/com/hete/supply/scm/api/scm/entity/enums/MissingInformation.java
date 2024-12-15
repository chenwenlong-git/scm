package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缺失信息枚举
 *
 * @author yanjiawei
 * @date 2023/07/23 12:42
 */
@Getter
@AllArgsConstructor
public enum MissingInformation implements IRemark {
    // 原料在WMS没有对应库存信息，或者库存不足
    OUT_OF_STOCK("无库存"),
    // 加工单无工序信息
    NOT_EXIST_PROCESS("无工序信息"),
    // 加工单无原料信息
    NOT_EXIST_MATERIAL("无原料信息"),
    ;
    private final String remark;
}