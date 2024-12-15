package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupSkuCraftAttrImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "缠管")
    private String tubeWrapping;

    @NotNull(message = "根数不能为空")
    @ApiModelProperty(value = "根数")
    private String rootsCnt;

    @NotNull(message = "层数不能为空")
    @ApiModelProperty(value = "层数")
    private String layersCnt;

    @ApiModelProperty(value = "特殊处理")
    private String specialHandling;
}
