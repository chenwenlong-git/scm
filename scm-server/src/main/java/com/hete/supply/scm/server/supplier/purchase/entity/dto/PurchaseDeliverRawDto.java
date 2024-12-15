package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/19 00:27
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverRawDto {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotNull(message = "额外消耗数不能为空")
    @ApiModelProperty(value = "额外消耗数")
    private Integer extraCnt;

    @NotNull(message = "原料提供方不能为空")
    @ApiModelProperty(value = "原料提供方")
    private RawSupplier rawSupplier;
}
