package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/1/10 10:44
 */
@Data
@NoArgsConstructor
public class RepairOrderExportVo {

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "计划单号")
    private String planNo;

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "单价")
    private BigDecimal salePrice;

    @ApiModelProperty(value = "加工总数")
    private Integer expectProcessNum;

    @ApiModelProperty(value = "需求平台")
    private String platform;

    @ApiModelProperty(value = "收货仓库")
    private String expectWarehouseName;

    @ApiModelProperty(value = "期望上架时间")
    private String expectCompleteProcessTime;

    @ApiModelProperty(value = "下单人")
    private String planCreateUsername;

    @ApiModelProperty(value = "下单时间")
    private String planCreateTime;

    @ApiModelProperty(value = "完成数量")
    private Integer actProcessedCompleteCntTotal;

    @ApiModelProperty(value = "返修人")
    private String completeRepairUserName;

    @ApiModelProperty(value = "返修完成时间")
    private String confirmCompleteTime;

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "质检正品数")
    private Integer passAmount;

    @ApiModelProperty(value = "质检次品数")
    private Integer notPassAmount;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptNum;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;


}
