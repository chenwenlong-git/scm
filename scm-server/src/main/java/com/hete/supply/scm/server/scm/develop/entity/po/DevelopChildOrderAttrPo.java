package com.hete.supply.scm.server.scm.develop.entity.po;

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
 * 开发子单属性值表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_child_order_attr")
@ApiModel(value = "DevelopChildOrderAttrPo对象", description = "开发子单属性值表")
public class DevelopChildOrderAttrPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_child_order_property_id", type = IdType.ASSIGN_ID)
    private Long developChildOrderPropertyId;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "属性key")
    private String attrName;


    @ApiModelProperty(value = "属性value")
    private String attrValue;

    @ApiModelProperty(value = "关联属性ID")
    private Long attributeNameId;

}
