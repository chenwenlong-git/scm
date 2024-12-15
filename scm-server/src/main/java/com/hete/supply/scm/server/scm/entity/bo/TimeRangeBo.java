package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author yanjiawei
 * @date 2023年08月21日 16:10
 */
@Data
@AllArgsConstructor
public class TimeRangeBo {
    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;
}
