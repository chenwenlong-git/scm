package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/7/18 17:49
 */
@Data
@NoArgsConstructor
public class PurchaseSkuSupplierItemDto {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotNull(message = "期望上架时间不能为空")
    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @NotNull(message = "创建时间不能为空")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;
}
