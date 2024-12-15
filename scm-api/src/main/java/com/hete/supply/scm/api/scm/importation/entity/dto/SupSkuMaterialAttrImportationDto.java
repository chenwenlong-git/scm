package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupSkuMaterialAttrImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "裆长尺寸")
    private String crotchLength;

    @ApiModelProperty(value = "裆长部位")
    private String crotchPosition;

    @ApiModelProperty(value = "深色克重")
    private String darkWeight;

    @ApiModelProperty(value = "浅色克重")
    private String lightWeight;

    @ApiModelProperty(value = "裆长配比")
    private String crotchLengthRatio;

    @ApiModelProperty(value = "原料克重")
    private String weight;
}
