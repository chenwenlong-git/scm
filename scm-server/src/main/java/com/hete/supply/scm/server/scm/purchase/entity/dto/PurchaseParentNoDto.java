package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */
@Data
@NoArgsConstructor
public class PurchaseParentNoDto {
    @NotBlank(message = "采购母单单号不能为空")
    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;
}
