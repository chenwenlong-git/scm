package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/11/4 10:01
 */
@NoArgsConstructor
@Data
public class ProduceAttrIdAndValueBo {
    @ApiModelProperty(value = "属性名id")
    private Long attrNameId;

    @ApiModelProperty(value = "属性值列表")
    private List<String> attrValueList;
}
