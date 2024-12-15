package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商停工时间表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_rest")
@ApiModel(value = "SupplierRestPo对象", description = "供应商停工时间表")
public class SupplierRestPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_rest_id", type = IdType.ASSIGN_ID)
    private Long supplierRestId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "停工日期")
    private LocalDate restDate;
}
