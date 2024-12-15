package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DeductOrderPurchaseType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 扣款单品质扣款明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("deduct_order_quality")
@ApiModel(value = "DeductOrderQualityPo对象", description = "扣款单品质扣款明细")
public class DeductOrderQualityPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "deduct_order_quality_id", type = IdType.ASSIGN_ID)
    private Long deductOrderQualityId;


    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;


    @ApiModelProperty(value = "关联单据号")
    private String businessNo;


    @ApiModelProperty(value = "关联单据类型：大货采购单(PRODUCT_PURCHASE)、加工采购单(PROCESS_PURCHASE)、采购退货单(PURCHASE_RETURN)、样品退货单SAMPLE_RETURN、样品采购单(SAMPLE)")
    private DeductOrderPurchaseType deductOrderPurchaseType;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "SKU数量")
    private Integer skuNum;


    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "扣款备注")
    private String deductRemarks;


}
