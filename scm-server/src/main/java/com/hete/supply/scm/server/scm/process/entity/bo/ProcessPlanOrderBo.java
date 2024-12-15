package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 需要排产加工单信息
 *
 * @author yanjiawei
 * @date 2023/07/27 23:39
 */
@Data
@NoArgsConstructor
public class ProcessPlanOrderBo {

    @ApiModelProperty(value = "主键id")
    private Long processOrderId;
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;
    @ApiModelProperty(value = "总工序复杂系数")
    private Integer totalComplexCoefficient;
    @ApiModelProperty(value = "产能指数")
    private Integer capacityNum;
    @ApiModelProperty(value = "优先级")
    private Integer priority;
    @ApiModelProperty(value = "订单类型")
    private String processOrderType;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "已成功排产")
    private boolean successPlan;
}