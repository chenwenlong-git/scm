package com.hete.supply.scm.server.supplier.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 11:40
 */
@Data
@NoArgsConstructor
public class InventoryChangeDto {
    @NotEmpty(message = "库存变更项不能为空")
    @Valid
    @ApiModelProperty(value = "库存变更项")
    private List<InventoryChangeItemDto> inventoryChangeItemList;
}
