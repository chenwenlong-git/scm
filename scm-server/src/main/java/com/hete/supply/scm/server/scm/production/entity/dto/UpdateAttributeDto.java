package com.hete.supply.scm.server.scm.production.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.production.enums.AttributeIsRequired;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
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
public class UpdateAttributeDto {

    @NotNull(message = "属性主键id不能为空")
    @ApiModelProperty(value = "属性主键id")
    private Long attrId;

    @NotNull(message = "版本号不能为空")
    @ApiModelProperty(value = "版本号")
    private Integer version;

    @NotBlank(message = "属性名称不能为空")
    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @NotNull(message = "属性分类id不能为空")
    @ApiModelProperty(value = "属性分类id", notes = "属性类型")
    private Long attrCategoryId;

    @ApiModelProperty(value = "属性分类名称", notes = "属性类型名称")
    private String attrCategoryName;

    @ApiModelProperty(value = "属性选项列表")
    private List<UpdateAttributeOptionDto> attrOptionList;

    @NotNull(message = "是否必填不能为空")
    @ApiModelProperty(value = "是否必填")
    private AttributeIsRequired attributeIsRequired;

    @NotNull(message = "属性状态不能为空")
    @ApiModelProperty(value = "属性状态")
    private AttributeStatus attributeStatus;

    @ApiModelProperty(value = "商品分类列表", notes = "商品二级分类信息")
    @NotEmpty(message = "商品分类列表不能为空")
    private List<AddAttributeDto.SkuCategoryDto> skuCategoryList;

    @Data
    public static class UpdateAttributeOptionDto {
        @NotBlank(message = "属性值不能为空")
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
        //属性名称最大10个字符
        ParamValidUtils.requireEquals(StrUtil.length(this.attrName) <= MAX_ATTR_NAME_LENGTH, true,
                StrUtil.format("属性名称最大{}个字符", MAX_ATTR_NAME_LENGTH));

        if (CollectionUtils.isNotEmpty(this.attrOptionList)) {
            //去除空格
            this.attrOptionList.forEach(item -> item.setAttributeValue(StrUtil.trim(item.getAttributeValue())));

            //长度校验
            boolean validLen = this.attrOptionList.stream().allMatch(item -> StrUtil.length(item.getAttributeValue()) <= 500);
            ParamValidUtils.requireEquals(validLen, true, "选项属性值不能超过500个字符");

            //属性选项列表名称不能重复
            long count = this.attrOptionList.stream().map(UpdateAttributeDto.UpdateAttributeOptionDto::getAttributeValue).distinct().count();
            long size = this.attrOptionList.size();
            ParamValidUtils.requireEquals(count, size, "属性选项列表名称不能重复");
        }
    }
}
