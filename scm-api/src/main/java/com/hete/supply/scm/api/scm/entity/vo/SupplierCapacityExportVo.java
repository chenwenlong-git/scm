package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 供应商每日剩余产能导出
 *
 * @author yanjiawei
 * Created on 2024/8/10.
 */
@Data
@NoArgsConstructor
public class SupplierCapacityExportVo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "日期")
    private LocalDate capacityDate;

    @ApiModelProperty(value = "日期")
    private String capacityDateStr;

    @ApiModelProperty(value = "剩余产能")
    private BigDecimal normalAvailableCapacity;
}
