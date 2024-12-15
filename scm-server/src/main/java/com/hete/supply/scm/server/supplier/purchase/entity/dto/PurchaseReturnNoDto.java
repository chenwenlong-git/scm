package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:14
 */
@Data
@NoArgsConstructor
public class PurchaseReturnNoDto {
    @NotBlank(message = "退货单号不能为空")
    @ApiModelProperty(value = "采购退货单号")
    private String returnOrderNo;

}
