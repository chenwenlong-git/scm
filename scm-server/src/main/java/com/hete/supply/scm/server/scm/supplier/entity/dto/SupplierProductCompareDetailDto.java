package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/3/28 15:21
 */
@Data
@NoArgsConstructor
public class SupplierProductCompareDetailDto {

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

}
