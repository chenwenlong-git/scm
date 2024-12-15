package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/19 17:22
 */
@Data
@NoArgsConstructor
public class SupplierInventoryResultBo {
    @ApiModelProperty(value = "扣减备货库存的数值")
    private Integer stockUpDecrement;

    @ApiModelProperty(value = "扣减自备库存的数值")
    private Integer selfProvideDecrement;
}
