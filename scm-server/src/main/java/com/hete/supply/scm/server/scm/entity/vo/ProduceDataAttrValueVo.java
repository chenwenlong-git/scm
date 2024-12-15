package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/3/20 14:03
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrValueVo {

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;

    @ApiModelProperty(value = "SKU列表")
    private List<String> skuList;


}
