package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/31 07:39
 */
@Data
@NoArgsConstructor
@ApiModel(value = "加工单排产池详情返回实体", description = "加工单排产池详情返回实体")
public class ProcessOrderPlanInfoVo {

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;
    @ApiModelProperty(value = "订单类型")
    private ProcessOrderType processOrderType;
    @ApiModelProperty(value = "约定日期/期望上架时间")
    private LocalDateTime deliverDate;
    @ApiModelProperty(value = "商品sku")
    private String sku;
    @ApiModelProperty(value = "下单数")
    private Integer totalProcessNum;
    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;
    @ApiModelProperty(value = "排产日期")
    private LocalDateTime processPlanTime;
    @ApiModelProperty(value = "排产计划")
    @JsonProperty("processProcedureEmployeePlans")
    private List<ProcessProcedureEmployeePlanVo> processProcedureEmployeePlanVos;
}