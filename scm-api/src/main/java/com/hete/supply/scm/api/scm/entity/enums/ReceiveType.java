package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/28 11:21
 */
@Getter
@AllArgsConstructor
public enum ReceiveType implements IRemark {
    // 同步类型
    BULK("大货入库"),
    TRANSFER("调拨入库"),
    PROCESS_MATERIAL("原料入库"),
    PROCESS_PRODUCT("加工成品入库"),
    SAMPLE("样品入库"),
    SALE_RETURN("销退入库"),
    PROFIT("盘盈入库"),
    RETURN("归还入库"),
    DEFECTIVE_PROCESS_PRODUCT("次品入库"),
    DOWN_RANK("降档入库"),
    OTHER("其他入库"),
    CHANGE_GOODS("换货入库"),
    INSIDE_CHECK("抽检入库"),
    FAST_SALE("闪售入库"),
    PREPARE_ORDER("备货入库"),
    ;


    private final String remark;
}
