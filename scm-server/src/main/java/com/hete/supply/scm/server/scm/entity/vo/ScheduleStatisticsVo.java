package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanjiawei
 * @date 2023年08月08日 00:45
 */
@Data
@NoArgsConstructor
public class ScheduleStatisticsVo {
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "待加工订单数")
    private long pendingOrders;
    @ApiModelProperty(value = "加工中订单数")
    private long processingOrders;
    @ApiModelProperty(value = "已完成订单数")
    private long completedOrders;

}
