package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 通用时间返回实体
 *
 * @author yanjiawei
 * Created on 2023/9/14.
 */
public class TimeCommonVo {

    @ApiModelProperty(value = "当前日期")
    private final LocalDateTime currentDateTime = LocalDateTime.now();

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }
}
