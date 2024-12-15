package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 工序时间计划
 *
 * @author yanjiawei
 * @date 2023/07/29 15:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPlanTimeBo {

    @ApiModelProperty(value = "预计开始时间")
    private LocalDateTime expectBeginDateTime;
    @ApiModelProperty(value = "预计结束时间")
    private LocalDateTime expectEndDateTime;
}