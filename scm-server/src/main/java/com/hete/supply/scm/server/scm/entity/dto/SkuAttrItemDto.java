package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/9/10 17:04
 */
@Data
@NoArgsConstructor
public class SkuAttrItemDto {
    @ApiModelProperty(value = "蕾丝面积属性值")
    private String laceAttrValue;

    @ApiModelProperty(value = "档长尺寸属性值")
    private String sizeAttrValue;

    @ApiModelProperty(value = "材料属性值")
    private String materialAttrValue;
}
