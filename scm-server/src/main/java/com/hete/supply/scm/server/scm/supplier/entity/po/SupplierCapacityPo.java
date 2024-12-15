package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商产能表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_capacity")
@ApiModel(value = "SupplierCapacityPo对象", description = "供应商产能表")
public class SupplierCapacityPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_capacity_id", type = IdType.ASSIGN_ID)
    private Long supplierCapacityId;


    @ApiModelProperty(value = "供应商产能规则id")
    private Long supplierCapacityRuleId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "产能日期")
    private LocalDate capacityDate;


    @ApiModelProperty(value = "总标准产能")
    private BigDecimal totalNormalCapacity;


    @ApiModelProperty(value = "可用标准产能")
    private BigDecimal normalAvailableCapacity;


}
