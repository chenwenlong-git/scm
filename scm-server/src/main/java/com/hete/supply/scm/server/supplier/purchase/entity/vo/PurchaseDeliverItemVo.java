package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/3 11:56
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverItemVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

}
