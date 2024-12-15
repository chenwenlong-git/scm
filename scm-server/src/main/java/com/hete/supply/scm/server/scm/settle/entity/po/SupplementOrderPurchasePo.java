package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SupplementOrderPurchaseType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 补款单采购明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplement_order_purchase")
@ApiModel(value = "SupplementOrderPurchasePo对象", description = "补款单采购明细")
public class SupplementOrderPurchasePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplement_order_purchase_id", type = IdType.ASSIGN_ID)
    private Long supplementOrderPurchaseId;


    @ApiModelProperty(value = "补款单ID")
    private Long supplementOrderId;


    @ApiModelProperty(value = "关联单据号")
    private String businessNo;


    @ApiModelProperty(value = "关联单据类型")
    private SupplementOrderPurchaseType supplementOrderPurchaseType;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "SKU数量")
    private Integer skuNum;


    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "补款备注")
    private String supplementRemarks;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "结算单价")
    private BigDecimal settleUnitPrice;

}
