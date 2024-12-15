package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/22 21:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierInventoryImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "操作仓库名称")
    private String supplierWarehouse;

    @ApiModelProperty(value = "操作数量")
    private Integer ctrlCnt;
}
