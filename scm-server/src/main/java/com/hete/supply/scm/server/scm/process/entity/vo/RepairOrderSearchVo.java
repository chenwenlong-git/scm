package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.plm.api.repair.entity.enums.ProcessType;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/8 09:50
 */
@Data
@NoArgsConstructor
public class RepairOrderSearchVo {

    @ApiModelProperty(value = "id")
    private Long repairOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "计划单号")
    private String planNo;

    @ApiModelProperty(value = "计划单号标题")
    private String planTitle;

    @ApiModelProperty(value = "计划单类型")
    private ProcessType planType;

    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;

    @ApiModelProperty(value = "返修单状态")
    private RepairOrderStatus repairOrderStatus;

    @ApiModelProperty(value = "单价")
    private BigDecimal salePrice;

    @ApiModelProperty(value = "加工总数")
    private Integer expectProcessNum;

    @ApiModelProperty(value = "需求平台编码")
    private String platform;

    @ApiModelProperty(value = "收货仓库编码")
    private String expectWarehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String expectWarehouseName;

    @ApiModelProperty(value = "期望完成时间")
    private LocalDateTime expectCompleteProcessTime;

    @ApiModelProperty(value = "下单人")
    private String planCreateUser;

    @ApiModelProperty(value = "下单人名称")
    private String planCreateUsername;

    @ApiModelProperty(value = "完成数量")
    private Integer actProcessedCompleteCntTotal;

    @ApiModelProperty(value = "返修人")
    private String completeRepairUserName;

    @ApiModelProperty(value = "返修完成时间")
    private LocalDateTime confirmCompleteTime;

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "质检正品数")
    private Integer passAmount;

    @ApiModelProperty(value = "质检次品数")
    private Integer notPassAmount;

    @ApiModelProperty(value = "原料收货数量")
    private Integer materialReceiptNumTotal;

    @ApiModelProperty(value = "原料收货单号")
    private String materialReceiveOrderNo;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptNum;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime planCreateTime;

    @ApiModelProperty(value = "返修单明细列表")
    private List<RepairOrderItemDetailVo> repairOrderItemDetailList;

}
