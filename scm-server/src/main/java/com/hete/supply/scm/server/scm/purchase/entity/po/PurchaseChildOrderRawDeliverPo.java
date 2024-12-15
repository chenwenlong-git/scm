package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购需求子单原料出库单关联
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_child_order_raw_deliver")
@ApiModel(value = "PurchaseChildOrderRawDeliverPo对象", description = "采购需求子单原料出库单关联")
public class PurchaseChildOrderRawDeliverPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_child_order_raw_deliver_id", type = IdType.ASSIGN_ID)
    private Long purchaseChildOrderRawDeliverId;


    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;


    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;


    @ApiModelProperty(value = "原料提供方:HETE(赫特),SUPPLIER(供应商),")
    private RawSupplier rawSupplier;


    @ApiModelProperty(value = "库存变更id")
    private Long supplierInventoryRecordId;


    @ApiModelProperty(value = "采购原料发货单号")
    private String purchaseRawDeliverOrderNo;

    @ApiModelProperty(value = "分配数量")
    private Integer dispenseCnt;

    @ApiModelProperty(value = "是否指定库位")
    private BooleanType particularLocation;

}
