package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/2/26 09:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CostImportDto extends BaseImportationRowDto {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotNull(message = "月初加权单价不能为空")
    @ApiModelProperty(value = "月初加权单价")
    private BigDecimal moWeightingPriceMin;
}
