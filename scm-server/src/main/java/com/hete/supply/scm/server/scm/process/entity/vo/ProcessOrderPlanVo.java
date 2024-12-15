package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/31 07:39
 */
@Data
@NoArgsConstructor
@ApiModel(value = "加工单排产池返回实体", description = "加工单排产池返回实体")
public class ProcessOrderPlanVo {

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;
    @ApiModelProperty(value = "加工数")
    private Integer totalProcessNum;
    @ApiModelProperty(value = "订单类型")
    private ProcessOrderType processOrderType;
    @ApiModelProperty(value = "约定日期/期望上架时间")
    private LocalDateTime deliverDate;
    @ApiModelProperty(value = "是否超额")
    private OverPlan overPlan;
    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;
    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;
    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "最早工序开始时间")
    private LocalDateTime processPlanEarliestExpectBeginTime;
    @ApiModelProperty(value = "最晚工序完成时间")
    private LocalDateTime processPlanLatestExpectEndTime;
    @ApiModelProperty(value = "工序完成时间")
    private LocalDateTime processCompletionTime;
    @ApiModelProperty(value = "排产时间")
    private LocalDateTime processPlanTime;
    @ApiModelProperty(value = "排产时间是否超过24H")
    private Boolean processPlanTimeOverTwentyFour;
    @ApiModelProperty(value = "产能指数")
    private Integer capacityNum;
    @ApiModelProperty(value = "是否延误")
    private ProcessPlanDelay processPlanDelay;
    @ApiModelProperty(value = "业务时间（创建时间/加工开始时间/加工完成时间）")
    private LocalDateTime businessTime;
}