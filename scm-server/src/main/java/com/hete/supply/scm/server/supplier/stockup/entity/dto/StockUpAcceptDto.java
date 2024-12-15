package com.hete.supply.scm.server.supplier.stockup.entity.dto;

import com.hete.supply.scm.server.scm.stockup.entity.dto.StockIdAndVersionDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/12 13:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StockUpAcceptDto extends StockIdAndVersionDto {
    @ApiModelProperty(value = "是否接单")
    private BooleanType acceptType;
}
