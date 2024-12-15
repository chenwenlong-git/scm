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
 * 供应链属性可选值表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("attribute_option")
@ApiModel(value = "AttributeOptionPo对象", description = "供应链属性可选值表")
public class AttributeOptionPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "attribute_option_id", type = IdType.ASSIGN_ID)
    private Long attributeOptionId;


    @ApiModelProperty(value = "属性ID")
    private Long attributeId;


    @ApiModelProperty(value = "属性名称")
    private String attributeName;


    @ApiModelProperty(value = "属性值")
    private String attributeValue;
}
