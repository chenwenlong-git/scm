package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SkuCycleImportationDto extends BaseImportationRowDto {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "生产周期")
    private String cycle;

    @ApiModelProperty(value = "单件产能")
    private String singleCapacity;

}
