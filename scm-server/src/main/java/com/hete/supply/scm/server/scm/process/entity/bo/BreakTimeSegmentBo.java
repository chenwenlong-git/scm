package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author yanjiawei
 * @date 2023年08月17日 15:20
 */
@ApiModel(description = "休息时间段")
@Data
public class BreakTimeSegmentBo {
    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

}
