package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 供应商产能日志表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_capacity_log")
@ApiModel(value = "SupplierCapacityLogPo对象", description = "供应商产能日志表")
public class SupplierCapacityLogPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_capacity_log_id", type = IdType.ASSIGN_ID)
    private Long supplierCapacityLogId;


    @ApiModelProperty(value = "供应商产能id")
    private Long supplierCapacityId;


    @ApiModelProperty(value = "来源类型")
    private String bizType;


    @ApiModelProperty(value = "来源单号")
    private String bizNo;

    @ApiModelProperty(value = "操作产能")
    private BigDecimal operateCapacity;
}
