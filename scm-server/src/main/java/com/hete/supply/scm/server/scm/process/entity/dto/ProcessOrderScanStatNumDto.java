package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@AllArgsConstructor
@ApiModel(value = "工序扫码本月提成参数", description = "工序扫码本月提成参数")
public class ProcessOrderScanStatNumDto {

    @ApiModelProperty(value = "扫码完成时间-开始时间")
    private LocalDateTime completeTimeStart;

    @ApiModelProperty(value = "扫码完成时间-结束时间")
    private LocalDateTime completeTimeEnd;

    @ApiModelProperty(value = "扫码完成人")
    private String completeUser;
}
