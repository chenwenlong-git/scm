package com.hete.supply.scm.server.scm.production.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class SkuAttributeInfoVo {
    @ApiModelProperty(value = "属性主键id")
    private Long attrId;

    @ApiModelProperty(value = "sku属性主键id")
    private Long skuAttrId;

    @JsonIgnore
    @ApiModelProperty(value = "属性状态")
    private AttributeStatus attributeStatus;

    @ApiModelProperty(value = "sku属性值列表")
    @JsonProperty("attrValueList")
    private List<SkuAttrValueVo> skuAttrValueVoList;
}
