package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class AttributeRiskInfoVo {
    @ApiModelProperty(value = "属性风险主键id")
    private Long attrRiskId;

    @ApiModelProperty(value = "属性主键id")
    private Long attrId;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "风险系数")
    private BigDecimal coefficient;

    @ApiModelProperty(value = "属性可选项风险列表")
    private List<AttributeRiskOptInfoVo> attributeRiskOptInfoList;
}
