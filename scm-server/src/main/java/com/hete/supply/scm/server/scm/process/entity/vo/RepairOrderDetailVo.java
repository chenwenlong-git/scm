package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/8 11:47
 */
@Data
@NoArgsConstructor
public class RepairOrderDetailVo {

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

    @ApiModelProperty(value = "需求平台编码")
    private String platform;

    @ApiModelProperty(value = "返修单状态")
    private RepairOrderStatus repairOrderStatus;

    @ApiModelProperty(value = "收货仓库编码")
    private String expectWarehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String expectWarehouseName;

    @ApiModelProperty(value = "期望完成时间")
    private LocalDateTime expectCompleteProcessTime;

    @ApiModelProperty(value = "完成数量")
    private Integer actProcessedCompleteCntTotal;

    @ApiModelProperty(value = "报废数量")
    private Integer actProcessScrapCntTotal;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptNum;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;


    @ApiModelProperty(value = "原料收货单号")
    private String materialReceiveOrderNo;

    @ApiModelProperty(value = "原料收货数")
    private Integer materialReceiptNumTotal;

    @ApiModelProperty(value = "原料明细列表")
    private List<RepairOrderItemMaterialVo> repairOrderItemMaterialList;

    @ApiModelProperty(value = "原料收货明细列表")
    private List<RepairOrderItemMaterialReceiptVo> repairOrderItemMaterialReceiptList;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "质检正品数")
    private Integer passAmount;

    @ApiModelProperty(value = "质检次品数")
    private Integer notPassAmount;

    @ApiModelProperty(value = "返修单结果列表")
    private List<RepairOrderResultDetailVo> repairOrderResultDetailList;

    @ApiModelProperty(value = "返修单明细列表")
    private List<RepairOrderItemDetailVo> repairOrderItemDetailList;

    @ApiModelProperty(value = "返修单明细和结果列表")
    private List<RepairOrderItemAndResultVo> repairOrderItemAndResultList;

    @ApiModelProperty(value = "原料信息列表", notes = "原料信息&原料收货信息&原料收货明细")
    private List<RepairMaterialCompositeVo> repairMaterialCompositeVos;
}
