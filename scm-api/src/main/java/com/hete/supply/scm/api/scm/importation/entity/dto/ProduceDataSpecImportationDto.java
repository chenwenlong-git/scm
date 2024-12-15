package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/3/8 14:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProduceDataSpecImportationDto extends BaseImportationRowDto {

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "规格书链接")
    private String productLink;

    @ApiModelProperty(value = "供应商代码(多个按逗号隔开)")
    private String supplierCodeList;

}
