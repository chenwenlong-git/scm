package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 工序提成规则
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process_commission_rule")
@ApiModel(value = "ProcessCommissionRulePo对象", description = "工序提成规则")
public class ProcessCommissionRulePo extends BaseSupplyPo {


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
}
