package com.hete.supply.scm.server.scm.process.enums;

import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/12/2 20:16
 */
@AllArgsConstructor
@Getter
public enum ProcessOrderVersion implements IRemark {
    // 加工单版本字段
    UPDATE_NAME("修改人", LogVersionValueType.STRING),
    UPDATE_TIME("修改时间", LogVersionValueType.DATE),
    SPU("赫特SPU", LogVersionValueType.STRING),
    WAREHOUSE_NAME("仓库名称", LogVersionValueType.STRING),
    DELIVER_DATE("业务约定日期", LogVersionValueType.DATE),
    PLAT_FORM("平台", LogVersionValueType.STRING),
    ORDER_NOTE("加工单备注", LogVersionValueType.STRING),
    PRODUCT_INFO("生产信息", LogVersionValueType.LIST),
    ;

    /**
     * 描述
     */
    private final String desc;
    private final LogVersionValueType logVersionValueType;

    @Override
    public String getRemark() {
        return this.desc;
    }

}
