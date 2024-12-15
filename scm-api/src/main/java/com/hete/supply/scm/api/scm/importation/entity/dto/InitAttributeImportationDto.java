package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class InitAttributeImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "次级属性类型")
    private String attributeCategoryName;

    @ApiModelProperty(value = "属性名称")
    private String attributeName;

    @ApiModelProperty(value = "属性值")
    private String attributeOptValues;

    @ApiModelProperty(value = "录入类型")
    private String inputType;

    @ApiModelProperty(value = "是否必填")
    private String isRequired;

    @ApiModelProperty(value = "数据维度")
    private String scope;

    @ApiModelProperty(value = "关联类目")
    private String relateCategoryNames;
}
