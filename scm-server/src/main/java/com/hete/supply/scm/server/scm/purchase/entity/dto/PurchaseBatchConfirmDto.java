package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/10 10:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseBatchConfirmDto extends PurchaseBatchIdVersionDto {
    @ApiModelProperty(value = "采购母单单号")
    @NotBlank(message = "采购母单单号不能为空")
    private String purchaseParentOrderNo;
}
