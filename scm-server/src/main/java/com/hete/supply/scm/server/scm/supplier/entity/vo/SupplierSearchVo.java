package com.hete.supply.scm.server.scm.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/9/8 14:32
 */
@Data
@NoArgsConstructor
public class SupplierSearchVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


}
