package com.hete.supply.scm.server.scm.process.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/16.
 */
@Data
public class ProcessCommissionRuleBo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "process_commission_rule_id", type = IdType.ASSIGN_ID)
    private Long processCommissionRuleId;


    @ApiModelProperty(value = "工序编码")
    private String processCode;


    @ApiModelProperty(value = "提成等级")
    private Integer commissionLevel;


    @ApiModelProperty(value = "数量起始值")
    private Integer startQuantity;


    @ApiModelProperty(value = "数量结束值")
    private Integer endQuantity;

    @ApiModelProperty(value = "提成系数百分比")
    private BigDecimal commissionCoefficient;

    @ApiModelProperty(value = "提成单价", example = "5.25")
    private BigDecimal commissionPrice;

    @ApiModelProperty(value = "版本号", example = "1")
    private Integer version;
}
