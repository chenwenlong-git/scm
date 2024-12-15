package com.hete.supply.scm.server.scm.production.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class SupSkuAttributeInfoDto {
    @NotNull(message = "属性主键id不能为空")
    @ApiModelProperty(value = "属性主键id")
    private Long attrId;

    @NotBlank(message = "属性名称不能为空")
    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "供应商属性主键id")
    private Long supSkuAttrId;

    @ApiModelProperty(value = "供应商属性值列表")
    @JsonProperty("attrValueList")
    private List<SupSkuAttrValueDto> supSkuAttrValueDtoList;

    @Data
    public static class SupSkuAttrValueDto {
        @ApiModelProperty(value = "供应商商品属性值主键id")
        private Long supSkuAttrValueId;

        @ApiModelProperty(value = "供应商商品属性属性值关联id")
        private Long valueId;

        @ApiModelProperty(value = "供应商商品属性属性值")
        private String value;
    }
}
