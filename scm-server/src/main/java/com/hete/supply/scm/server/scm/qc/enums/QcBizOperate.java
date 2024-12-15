package com.hete.supply.scm.server.scm.qc.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 质检业务操作
 *
 * @author yanjiawei
 * Created on 2023/10/30.
 */
@Getter
@AllArgsConstructor
public enum QcBizOperate implements IRemark {
    // 质检业务操作
    HANDOVER("完成交接"),
    START("开始质检"),
    SUBMIT("提交质检"),
    RESET("重置质检"),
    APPROVE_PASS("质检审核通过"),
    APPROVE_UN_PASSED("质检审核不通过"),
    COMPLETE("完成质检"),
    OTHER("非上述，其他质检操作");
    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }
}
