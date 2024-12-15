package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@RequiredArgsConstructor
@Getter
public enum QcType implements IRemark {
    // 质检类型
    ALL_CHECK("全检"),
    SAMPLE_CHECK("抽检"),
    NOT_CHECK("免检");
    private final String remark;
}
