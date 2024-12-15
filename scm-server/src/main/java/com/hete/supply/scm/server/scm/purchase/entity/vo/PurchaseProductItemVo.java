package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DiscountType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2022/11/15 20:31
 */
@Data
@NoArgsConstructor
public class PurchaseProductItemVo {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku变体属性")
    private String variantProperties;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "优惠类型")
    private DiscountType discountType;

    @ApiModelProperty(value = "扣减金额")
    private BigDecimal substractPrice;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;
}
