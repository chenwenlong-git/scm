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
 * 供应链属性分类表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("attribute_category")
@ApiModel(value = "AttributeCategoryPo对象", description = "供应链属性分类表")
public class AttributeCategoryPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "attribute_category_id", type = IdType.ASSIGN_ID)
    private Long attributeCategoryId;


    @ApiModelProperty(value = "属性类型名称")
    private String attributeCategoryName;


    @ApiModelProperty(value = "父级属性类型主键id")
    private Long parentAttributeCategoryId;
}
