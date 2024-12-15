package com.hete.supply.scm.server.scm.supplier.entity.vo;

import com.hete.supply.scm.server.scm.supplier.enums.SupplierUrgentStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/07/18 14:04
 */
@Data
@NoArgsConstructor
public class SupplierUrgentVo {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "紧急状态")
    private SupplierUrgentStatus supplierUrgentStatus;

}
