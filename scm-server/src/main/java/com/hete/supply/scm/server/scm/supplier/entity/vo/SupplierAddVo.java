package com.hete.supply.scm.server.scm.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/01/25 14:04
 */
@Data
@NoArgsConstructor
public class SupplierAddVo {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

}
