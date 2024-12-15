package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 数据库默认值枚举
 *
 * @author yanjiawei
 * Created on 2023/8/23.
 */
@Getter
@AllArgsConstructor
public enum DefaultDatabaseTime implements IRemark {
    // 数据库时间默认值
    DEFAULT_TIME(LocalDateTime.of(1970, 1, 1, 0, 0, 0), "数据库默认值");

    private final LocalDateTime dateTime;
    private final String remark;
}
