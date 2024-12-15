package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.supplier.enums.CapacityType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 供应商产能规则表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_capacity_rule")
@ApiModel(value = "SupplierCapacityRulePo对象", description = "供应商产能规则表")
public class SupplierCapacityRulePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_capacity_rule_id", type = IdType.ASSIGN_ID)
    private Long supplierCapacityRuleId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "产能类型，常规(NORMAL)")
    private CapacityType capacityType;


    @ApiModelProperty(value = "产能")
    private BigDecimal capacity;


    @ApiModelProperty(value = "规则循环周期")
    private Integer period;


}
