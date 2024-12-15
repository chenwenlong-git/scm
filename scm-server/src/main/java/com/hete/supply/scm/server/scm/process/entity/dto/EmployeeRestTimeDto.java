package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * @date 2023年09月15日 14:26
 */
@Data
public class EmployeeRestTimeDto {

    @NotNull(message = "停工开始时间不能为空")
    @ApiModelProperty(value = "停工开始时间")
    private LocalDateTime restStartTime;

    @NotNull(message = "停工结束时间不能为空")
    @ApiModelProperty(value = "停工结束时间")
    private LocalDateTime restEndTime;
}
