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
 * 商品属性值表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sku_attribute_value")
@ApiModel(value = "SkuAttributeValuePo对象", description = "商品属性值表")
public class SkuAttributeValuePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sku_attribute_value_id", type = IdType.ASSIGN_ID)
    private Long skuAttributeValueId;


    @ApiModelProperty(value = "商品属性id")
    private Long skuAttributeId;


    @ApiModelProperty(value = "属性值来源id")
    private Long valueId;


    @ApiModelProperty(value = "属性值")
    private String value;
}
