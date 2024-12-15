package com.hete.supply.scm.server.scm.production.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeIsRequired;
import com.hete.supply.scm.server.scm.production.enums.AttributeScope;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class AddAttributeDto {
    @NotBlank(message = "属性名称不能为空")
    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @NotNull(message = "属性分类id不能为空")
    @ApiModelProperty(value = "属性分类id", notes = "属性类型")
    private Long attrCategoryId;

    @ApiModelProperty(value = "属性分类名称", notes = "属性类型名称")
    private String attrCategoryName;

    @NotNull(message = "录入类型不能为空")
    @ApiModelProperty(value = "录入类型")
    private AttributeInputType attributeInputType;

    @NotNull(message = "是否必填不能为空")
    @ApiModelProperty(value = "是否必填")
    private AttributeIsRequired attributeIsRequired;

    @ApiModelProperty(value = "商品分类列表", notes = "商品二级分类信息")
    @NotEmpty(message = "商品分类列表不能为空")
    private List<SkuCategoryDto> skuCategoryList;

    @NotNull(message = "数据维度不能为空")
    @ApiModelProperty(value = "数据作用范围（数据维度）")
    private AttributeScope attributeScope;

    @ApiModelProperty(value = "属性选项列表")
    private List<AddAttributeOptionDto> attrOptionList;

    @Data
    public static class AddAttributeOptionDto {
        @ApiModelProperty(value = "选项属性值")
        @NotBlank(message = "选项属性值不能为空")
        private String attributeValue;
    }

    @Data
    public static class SkuCategoryDto {
        @ApiModelProperty(value = "plm商品分类名称", notes = "商品二级分类名称")
        private String skuCategoryName;

        @ApiModelProperty(value = "商品分类ID", notes = "商品二级分类ID")
        @NotNull(message = "商品分类ID不能为空")
        private Long skuCategoryId;
    }

    public void validate() {
        //定义属性名称最大字符常量
        final int MAX_ATTR_NAME_LENGTH = 10;

        List<AttributeInputType> validateAttributeInputType = List.of(AttributeInputType.SINGLE_SELECT, AttributeInputType.MULTIPLE_SELECT);
        if (validateAttributeInputType.contains(this.attributeInputType)) {
            //录入类型属于单选下拉/多选下拉，属性选项列表不能为空
            ParamValidUtils.requireNotEmpty(this.attrOptionList, StrUtil.format("录入类型为{}，属性选项列表不能为空", this.attributeInputType));

            //去除空格
            this.attrOptionList.forEach(item -> item.setAttributeValue(StrUtil.trim(item.getAttributeValue())));

            //长度校验
            boolean validLen = this.attrOptionList.stream().allMatch(item -> StrUtil.length(item.getAttributeValue()) <= 500);
            ParamValidUtils.requireEquals(validLen, true, "选项属性值不能超过500个字符");

            //属性选项列表名称不能重复
            long count = this.attrOptionList.stream().map(AddAttributeOptionDto::getAttributeValue).distinct().count();
            long size = this.attrOptionList.size();
            ParamValidUtils.requireEquals(count, size, "属性选项列表名称不能重复");
        }

        //属性名称最大10个字符
        ParamValidUtils.requireEquals(StrUtil.length(this.attrName) <= MAX_ATTR_NAME_LENGTH, true,
                StrUtil.format("属性名称最大{}个字符", MAX_ATTR_NAME_LENGTH));
    }
}
