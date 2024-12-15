package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplementOrderPurchaseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class PurchaseDropDownDto {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotNull(message = "单据类型不能为空")
    @ApiModelProperty(value = "单据类型")
    private SupplementOrderPurchaseType supplementOrderPurchaseType;

    @NotBlank(message = "单据号不能为空")
    @ApiModelProperty(value = "单据号")
    private String businessNo;

}