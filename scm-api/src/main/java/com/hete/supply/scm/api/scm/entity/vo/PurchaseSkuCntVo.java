package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2023/2/28 11:02
 */
@Data
@NoArgsConstructor
public class PurchaseSkuCntVo {
    @ApiModelProperty("sku")
    private String skuCode;

    @ApiModelProperty("sku批次码")
    private String skuBatchCode;

    @ApiModelProperty("sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @ApiModelProperty(value = "实际退货数量")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "结算单价(收单规则计算)")
    private BigDecimal settleRecoOrderPrice;

    @ApiModelProperty(value = "结算总价(收单规则计算)")
    private BigDecimal settleRecoOrderPriceTotal;
}
