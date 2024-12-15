package com.hete.supply.scm.server.supplier.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/19 16:09
 */
@Data
@NoArgsConstructor
public class InventorySubDto {
    @ApiModelProperty(value = "库存扣减明细")
    @NotEmpty(message = "库存扣减明细不能为空")
    @Valid
    private List<InventorySubItemDto> inventorySubItemList;
}
