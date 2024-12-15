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
 * 商品属性表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sku_attribute")
@ApiModel(value = "SkuAttributePo对象", description = "商品属性表")
public class SkuAttributePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sku_attribute_id", type = IdType.ASSIGN_ID)
    private Long skuAttributeId;


    @ApiModelProperty(value = "sku编码")
    private String sku;


    @ApiModelProperty(value = "供应链属性主键id")
    private Long attributeId;


    @ApiModelProperty(value = "供应链属性名称")
    private String attributeName;
}
