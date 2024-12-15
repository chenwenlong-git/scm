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
public class ProduceDataAttrValueBySkuVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "属性信息列表")
    private List<ProduceDataAttrValueBySkuItemVo> produceDataAttrValueBySkuItemList;

    @Data
    public static class ProduceDataAttrValueBySkuItemVo {
        @ApiModelProperty(value = "属性ID")
        private Long attributeNameId;

        @ApiModelProperty(value = "属性名称")
        private String attrName;

        @ApiModelProperty(value = "属性value")
        private String attrValue;
    }

}
