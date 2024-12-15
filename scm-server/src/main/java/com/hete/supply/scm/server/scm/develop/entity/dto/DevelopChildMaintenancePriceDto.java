package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopChildMaintenancePriceDto extends DevelopChildIdAndVersionDto {

    @NotNull(message = "样品价格不能为空")
    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @NotNull(message = "大货价格不能为空")
    @ApiModelProperty(value = "大货价格")
    private BigDecimal purchasePrice;
}
