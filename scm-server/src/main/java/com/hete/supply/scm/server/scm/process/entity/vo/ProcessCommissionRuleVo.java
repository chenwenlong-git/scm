package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/14.
 */
@Data
public class ProcessCommissionRuleVo {

    @ApiModelProperty(value = "提成规则主键id")
    private Long processCommissionRuleId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "提成等级")
    private Integer commissionLevel;

    @ApiModelProperty(value = "数量起始值")
    private Integer startQuantity;

    @ApiModelProperty(value = "数量结束值")
    private Integer endQuantity;

    @ApiModelProperty(value = "提成系数百分比", example = "80.25%", notes = "前端加上百分号即可")
    private BigDecimal commissionCoefficient;

    @ApiModelProperty(value = "阶梯提成单价=基础单价*系数", example = "5.25")
    private BigDecimal commissionPrice;

    @ApiModelProperty(value = "单件阶梯提成总价=基础单价*系数+额外单价", example = "5.25")
    private BigDecimal unitStaircaseTotalCommissionPrice;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
