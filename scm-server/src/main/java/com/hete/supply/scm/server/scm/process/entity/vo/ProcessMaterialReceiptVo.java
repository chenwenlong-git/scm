package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessMaterialReceiptStatus;
import com.hete.supply.scm.api.scm.entity.enums.MaterialReceiptType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author RockyHuas
 * @date 2022/11/11 14:04
 */
@Data
@NoArgsConstructor
public class ProcessMaterialReceiptVo {

    @ApiModelProperty(value = "主键id")
    private Long processMaterialReceiptId;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;

    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "原料收货类型")
    private MaterialReceiptType materialReceiptType;

    @ApiModelProperty(value = "出库单编号")
    private String deliveryNo;


    @ApiModelProperty(value = "状态，待收货（WAIT_RECEIVE），已收货（RECEIVED）")
    private ProcessMaterialReceiptStatus processMaterialReceiptStatus;


    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliveryTime;


    @ApiModelProperty(value = "收货人")
    private String receiptUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "发货仓库编码")
    private String deliveryWarehouseCode;


    @ApiModelProperty(value = "发货仓库名称")
    private String deliveryWarehouseName;


    @ApiModelProperty(value = "出库备注")
    private String deliveryNote;


    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;


    @ApiModelProperty(value = "下单人名称")
    private String placeOrderUsername;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "版本号")
    private Integer version;


}
