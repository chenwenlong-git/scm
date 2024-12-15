package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/10/12.
 */
@Data
public class SkuRiskLogVo {

    @ApiModelProperty(value = "风险等级")
    private SkuRisk skuRisk;

    @ApiModelProperty(value = "总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "风险属性列表")
    private List<RiskLogItemVo> riskLogItemVoList;

    @Data
    public static class RiskLogItemVo {
        @ApiModelProperty(value = "属性id")
        private Long attrId;

        @ApiModelProperty(value = "属性名")
        private String attrName;

        @ApiModelProperty(value = "属性可选项id")
        private Long attrOptionId;

        @ApiModelProperty(value = "属性可选项名称")
        private String attributeValue;

        @ApiModelProperty(value = "风险评分")
        private BigDecimal score;

        @ApiModelProperty(value = "风险系数")
        private BigDecimal coefficient;
    }
}
