package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * @date 2023年08月08日 00:38
 */
@Data
@NoArgsConstructor
public class ScheduleTabVo {
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "待加工订单")
    private long plannedCount;
    @ApiModelProperty(value = "待加工数")
    private long processedCount;
    @ApiModelProperty(value = "预计提成")
    private BigDecimal commission;
}
