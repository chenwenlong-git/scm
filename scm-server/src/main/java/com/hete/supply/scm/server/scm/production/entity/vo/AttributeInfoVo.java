package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeIsRequired;
import com.hete.supply.scm.server.scm.production.enums.AttributeScope;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class AttributeInfoVo {
    @ApiModelProperty(value = "属性主键id")
    private Long attrId;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "一级属性分类id（属性类型）", notes = "")
    private Long firstAttrCategoryId;

    @ApiModelProperty(value = "一级属性分类名称（属性类型名称）", notes = "")
    private String firstAttrCategoryName;

    @ApiModelProperty(value = "二级属性分类id（次级属性类型）", notes = "")
    private Long secondAttrCategoryId;

    @ApiModelProperty(value = "二级属性分类名称（次级属性类型名称）", notes = "")
    private String secondAttrCategoryName;

    @ApiModelProperty(value = "录入类型")
    private AttributeInputType attributeInputType;

    @ApiModelProperty(value = "是否必填")
    private AttributeIsRequired attributeIsRequired;

    @ApiModelProperty(value = "状态")
    private AttributeStatus attributeStatus;

    @ApiModelProperty(value = "商品分类列表", notes = "商品二级分类信息")
    private List<SkuCategoryVo> skuCategoryList;

    @NotNull(message = "数据维度不能为空")
    @ApiModelProperty(value = "数据作用范围（数据维度）")
    private AttributeScope attributeScope;

    @ApiModelProperty(value = "属性选项列表")
    private List<AttributeOptionVo> attrOptionList;
}
