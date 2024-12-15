package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/19 17:21
 */
@Data
@NoArgsConstructor
public class SupplierInventorySubBo {
    @NotNull(message = "备货库存不能为空")
    @ApiModelProperty(value = "备货库存")
    private Integer stockUpInventory;


    @NotNull(message = "消耗库存不能为空")
    @ApiModelProperty(value = "消耗库存")
    private Integer actualConsumeCnt;

}
