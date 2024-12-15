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
 * 供应商商品属性表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_sku_attribute")
@ApiModel(value = "SupplierSkuAttributePo对象", description = "供应商商品属性表")
public class SupplierSkuAttributePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_sku_attribute_id", type = IdType.ASSIGN_ID)
    private Long supplierSkuAttributeId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "sku编码")
    private String sku;


    @ApiModelProperty(value = "供应链属性主键id")
    private Long attributeId;


    @ApiModelProperty(value = "供应链属性名称")
    private String attributeName;
}
