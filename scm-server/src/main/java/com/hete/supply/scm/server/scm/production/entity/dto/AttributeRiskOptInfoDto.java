package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/9/19.
 */
@Data
public class AttributeRiskOptInfoDto {
    @NotNull(message = "属性选项主键id不能为空")
    @ApiModelProperty(value = "属性选项主键id")
    private Long attrOptionId;

    @NotBlank(message = "属性选项值不能为空")
    @ApiModelProperty(value = "属性选项值")
    private String attributeValue;

    @NotNull(message = "风险评分不能为空")
    @ApiModelProperty(value = "风险评分")
    @Digits(integer = 6, fraction = 2, message = "风险评分最多6位整数和2位小数")
    private BigDecimal score;
}
