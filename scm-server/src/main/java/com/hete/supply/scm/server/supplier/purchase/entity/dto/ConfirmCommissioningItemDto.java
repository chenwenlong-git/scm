package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/9 19:16
 */
@Data
@NoArgsConstructor
public class ConfirmCommissioningItemDto {
    @NotBlank(message = "原料sku不能为空")
    @ApiModelProperty(value = "原料sku")
    private String sku;

    @NotNull(message = "实际消耗数量")
    @ApiModelProperty(value = "实际消耗数量")
    private Integer actualConsumeCnt;

    @NotNull(message = "原料来源不能为空")
    @ApiModelProperty(value = "原料来源")
    private RawSupplier rawSupplier;

}
