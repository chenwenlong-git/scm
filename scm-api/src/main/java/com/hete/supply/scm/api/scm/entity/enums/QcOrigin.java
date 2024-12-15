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
public enum QcOrigin implements IRemark {
    // 质检来源
    PROCESS("加工采购"),
    PRODUCT("大货采购"),
    REPAIR_ORDER("返修质检"),
    PROCESS_ORDER("加工质检"),
    TRANSFER("调拨入库"),
    PROCESS_MATERIAL("原料入库"),
    SAMPLE("样品入库"),
    SALE_RETURN("销退入库"),
    PROFIT("盘盈入库"),
    RETURN("归还入库"),
    DEFECTIVE_PROCESS_PRODUCT("次品入库"),
    OTHER("其他入库"),
    CHANGE_GOODS("换货入库"),
    INSIDE_CHECK("抽检入库"),
    FAST_SALE("闪售入库"),
    PREPARE_ORDER("备货入库"),
    RESIDENT("驻场质检"),
    REWORK_RESIDENT("返工驻场质检"),
    OUTBOUND("出库质检"),

    ;

    private final String remark;
}
