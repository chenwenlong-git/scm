package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库排序字段枚举
 *
 * @author yanjiawei
 * Created on 2023/9/11.
 */
@Getter
@AllArgsConstructor
public enum SortOrder implements IRemark {
    // 正序排序
    ASC("正序"),
    // 倒序排序
    DESC("倒序"),
    ;
    private final String remark;
}
