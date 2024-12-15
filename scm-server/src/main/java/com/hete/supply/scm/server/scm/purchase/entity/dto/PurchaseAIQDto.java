package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.wms.api.interna.entity.dto.AvailableInventoryQueryDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/10/15 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseAIQDto extends AvailableInventoryQueryDto {
    @ApiModelProperty(value = "供应商")
    private String supplierCode;

    @ApiModelProperty(value = "库位")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "批次码")
    private String batchCode;
}
