package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * @date 2023年09月15日 14:26
 */
@Data
public class EmployeeRestTimeBo {

    @ApiModelProperty(value = "员工编号")
    private String employeeNo;

    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @ApiModelProperty(value = "停工开始时间")
    private LocalDateTime restStartTime;

    @ApiModelProperty(value = "停工结束时间")
    private LocalDateTime restEndTime;

}
