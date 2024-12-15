package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/3/18 10:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProduceDataItemSupplierImportationDto extends BaseImportationRowDto {


    @ApiModelProperty(value = "id")
    private String produceDataItemId;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "BOM名称")
    private String bomName;

    @ApiModelProperty(value = "供应商代码(多个按逗号隔开)")
    private String supplierCodeList;

    @ApiModelProperty(value = "处理方式")
    private String handleWay;

}
