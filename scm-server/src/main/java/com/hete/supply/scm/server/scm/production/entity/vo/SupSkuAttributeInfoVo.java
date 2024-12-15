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
public class SupSkuAttributeInfoVo {
    @ApiModelProperty(value = "属性主键id")
    private Long attrId;

    @ApiModelProperty(value = "状态")
    @JsonIgnore
    private AttributeStatus attributeStatus;

    @ApiModelProperty(value = "供应商属性主键id")
    private Long supSkuAttrId;

    @ApiModelProperty(value = "供应商属性值列表")
    @JsonProperty("attrValueList")
    private List<SupSkuAttrValueVo> supSkuAttrValueVoList;
}
