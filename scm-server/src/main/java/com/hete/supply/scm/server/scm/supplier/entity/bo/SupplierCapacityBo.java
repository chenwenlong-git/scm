package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author yanjiawei
 * Created on 2024/8/8.
 */
@Data
public class SupplierCapacityBo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "日期")
    private LocalDate capacityDate;
    @ApiModelProperty(value = "剩余产能")
    private BigDecimal normalAvailableCapacity;
}
