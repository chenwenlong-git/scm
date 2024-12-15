package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
public class PurchaseProductItemDto {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @NotNull(message = "采购价不能为空")
    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    @NotNull(message = "扣减金额不能为空")
    @ApiModelProperty(value = "扣减金额")
    private BigDecimal substractPrice;

    @NotNull(message = "结算金额不能为空")
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;
}
