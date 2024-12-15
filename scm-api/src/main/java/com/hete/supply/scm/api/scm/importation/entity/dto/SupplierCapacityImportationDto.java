package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/8/5.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierCapacityImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "产能日期")
    private String capacityDate;

    @ApiModelProperty(value = "可用标准产能")
    private String normalAvailableCapacityStr;
}
