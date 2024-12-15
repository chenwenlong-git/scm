package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/9/19.
 */
@Data
public class AttributeRiskOptInfoVo {
    @ApiModelProperty(value = "属性选项主键id")
    private Long attrOptionId;

    @ApiModelProperty(value = "属性选项值")
    private String attributeValue;

    @ApiModelProperty(value = "风险评分")
    private BigDecimal score;
}
