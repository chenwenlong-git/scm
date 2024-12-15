package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/25 16:37
 */
@Data
@NoArgsConstructor
public class PurchaseModifySkuDto {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    @Min(value = 1, message = "采购数不能小于0")
    private Integer purchaseCnt;

    @NotBlank(message = "降档sku不能为空")
    @ApiModelProperty(value = "降档sku")
    private String newSku;

    @NotNull(message = "降档采购数不能为空")
    @ApiModelProperty(value = "降档采购数")
    private Integer newPurchaseCnt;
}
