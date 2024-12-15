package com.hete.supply.scm.server.supplier.develop.entity.dto;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopChildIdAndVersionDto;
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
public class DevelopChildSupplierMaintenancePriceDto extends DevelopChildIdAndVersionDto {
    @ApiModelProperty(value = "版单id")
    private Long developPamphletOrderId;

    @ApiModelProperty(value = "版单version")
    private Integer developPamphletOrderVersion;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @NotNull(message = "供应商样品价格不能为空")
    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @NotNull(message = "供应商大货价格不能为空")
    @ApiModelProperty(value = "大货价格")
    private BigDecimal purchasePrice;
}
