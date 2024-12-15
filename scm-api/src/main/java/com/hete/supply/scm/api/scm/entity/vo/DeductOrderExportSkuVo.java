package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/07/05 13:44
 */
@Data
public class DeductOrderExportSkuVo {

    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @ApiModelProperty(value = "类型名称")
    private String deductTypeName;

    @ApiModelProperty(value = "扣款状态名称")
    private String deductStatusName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "扣款总金额")
    private BigDecimal deductPriceTotal;

    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "单据类型名称")
    private String deductOrderPurchaseTypeName;

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "扣款数量")
    private Integer deductNum;

    @ApiModelProperty(value = "扣款单价")
    private BigDecimal deductUnitPrice;

    @ApiModelProperty(value = "扣款原因")
    private String deductRemarks;

    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "退货关联采购单")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "退货sku批次码")
    private String returnSkuBatchCode;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

}
