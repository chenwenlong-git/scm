package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class UpdateAttributeRiskInfoDto {
    @ApiModelProperty(value = "属性风险主键id")
    private Long attrRiskId;

    @NotNull(message = "属性主键id不能为空")
    @ApiModelProperty(value = "属性主键id")
    private Long attrId;

    @NotBlank(message = "属性名称不能为空")
    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @NotNull(message = "风险系数不能为空")
    @ApiModelProperty(value = "风险系数")
    @Digits(integer = 6, fraction = 2, message = "风险系数最多6位整数和2位小数")
    private BigDecimal coefficient;

    @ApiModelProperty(value = "属性可选项风险列表")
    @NotEmpty(message = "属性可选项风险列表不能为空")
    @Valid
    private List<AttributeRiskOptInfoDto> attributeRiskOptInfoList;
}
