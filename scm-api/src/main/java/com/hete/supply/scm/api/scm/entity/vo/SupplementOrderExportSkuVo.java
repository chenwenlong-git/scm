package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/07/05 13:44
 */
@Data
public class SupplementOrderExportSkuVo {

    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

    @ApiModelProperty(value = "类型名称")
    private String supplementTypeName;

    @ApiModelProperty(value = "补款状态名称")
    private String supplementStatusName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "补款总金额")
    private BigDecimal supplementPriceTotal;

    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "单据类型名称")
    private String supplementOrderPurchaseTypeName;

    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "补款数量")
    private Integer supplementNum;

    @ApiModelProperty(value = "补款原因")
    private String supplementRemarks;

    @ApiModelProperty(value = "补款金额")
    private BigDecimal supplementPrice;

    @ApiModelProperty(value = "处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;


}
