package com.hete.supply.scm.server.scm.production.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商商品属性值表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_sku_attribute_value")
@ApiModel(value = "SupplierSkuAttributeValuePo对象", description = "供应商商品属性值表")
public class SupplierSkuAttributeValuePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_sku_attribute_value_id", type = IdType.ASSIGN_ID)
    private Long supplierSkuAttributeValueId;


    @ApiModelProperty(value = "供应商商品属性id")
    private Long supplierSkuAttributeId;


    @ApiModelProperty(value = "属性值来源id")
    private Long valueId;


    @ApiModelProperty(value = "属性值")
    private String value;
}
