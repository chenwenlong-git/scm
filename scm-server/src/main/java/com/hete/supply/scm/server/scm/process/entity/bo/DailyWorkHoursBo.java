package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author yanjiawei
 * @date 2023年08月18日 09:59
 */
@Data
@ApiModel(description = "每日工时计算信息")
public class DailyWorkHoursBo {
    @ApiModelProperty(value = "日期", example = "2023-08-15")
    private LocalDate processPlanDate;

    @ApiModelProperty(value = "耗时（分钟）", example = "1")
    private long durationMinutes;
}
