package com.hete.supply.scm.server.scm.production.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeIsRequired;
import com.hete.supply.scm.server.scm.production.enums.AttributeScope;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应链属性表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("attribute")
@ApiModel(value = "AttributePo对象", description = "供应链属性表")
public class AttributePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "attribute_id", type = IdType.ASSIGN_ID)
    private Long attributeId;


    @ApiModelProperty(value = "属性名称")
    private String attributeName;


    @ApiModelProperty(value = "属性分类ID")
    private Long attributeCategoryId;


    @ApiModelProperty(value = "属性分类名称")
    private String attributeCategoryName;


    @ApiModelProperty(value = "录入类型")
    private AttributeInputType inputType;


    @ApiModelProperty(value = "是否必填")
    private AttributeIsRequired isRequired;


    @ApiModelProperty(value = "状态")
    private AttributeStatus status;

    @ApiModelProperty(value = "数据作用范围（数据维度）")
    private AttributeScope scope;
}
