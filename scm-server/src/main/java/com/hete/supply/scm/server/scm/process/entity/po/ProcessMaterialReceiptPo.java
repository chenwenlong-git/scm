package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessMaterialReceiptStatus;
import com.hete.supply.scm.api.scm.entity.enums.MaterialReceiptType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 加工原料收货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_material_receipt")
@ApiModel(value = "ProcessMaterialReceiptPo对象", description = "加工原料收货单")
public class ProcessMaterialReceiptPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_material_receipt_id", type = IdType.ASSIGN_ID)
    private Long processMaterialReceiptId;


    @ApiModelProperty(value = "关联的加工单")
    private String processOrderNo;

    @ApiModelProperty(value = "关联的返修单号")
    private String repairOrderNo;

    @ApiModelProperty(value = "原料收货类型")
    private MaterialReceiptType materialReceiptType;

    @ApiModelProperty(value = "出库单编号")
    private String deliveryNo;


    @ApiModelProperty(value = "状态，待收货（WAIT_RECEIVE），已收货（RECEIVED）")
    private ProcessMaterialReceiptStatus processMaterialReceiptStatus;


    @ApiModelProperty(value = "发货数量")
    private Integer deliveryNum;

    @ApiModelProperty(value = "发货人")
    private String deliveryUser;

    @ApiModelProperty(value = "发货人名称")
    private String deliveryUsername;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliveryTime;

    @ApiModelProperty(value = "发货人")
    private String receiptUser;

    @ApiModelProperty(value = "收货人名称")
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


    @ApiModelProperty(value = "下单人 id")
    private String placeOrderUser;


    @ApiModelProperty(value = "下单人名称")
    private String placeOrderUsername;

    @ApiModelProperty(value = "平台")
    private String platform;

}
