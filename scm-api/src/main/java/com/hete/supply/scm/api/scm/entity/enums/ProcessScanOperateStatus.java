package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工序扫码操作状态
 *
 * @author yanjiawei
 * Created on 2024/12/11.
 */
@Getter
@AllArgsConstructor
public enum ProcessScanOperateStatus implements IRemark {
    //开始加工
    BEGIN_PROCESSING("开始加工"),
    //完成加工
    COMPLETE_PROCESSING("完成加工"),
    ;

    private final String remark;
}
