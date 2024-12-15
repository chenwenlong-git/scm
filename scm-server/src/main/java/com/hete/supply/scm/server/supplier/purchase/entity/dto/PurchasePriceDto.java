package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildIdAndVersionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2023/1/28 16:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchasePriceDto extends SampleChildIdAndVersionDto {
    @NotNull(message = "打样单价不能为空")
    @ApiModelProperty(value = "打样单价")
    private BigDecimal proofingPrice;
}
