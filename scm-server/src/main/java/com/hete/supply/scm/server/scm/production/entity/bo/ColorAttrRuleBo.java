package com.hete.supply.scm.server.scm.production.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 颜色色系对照对象
 *
 * @author yanjiawei
 * Created on 2024/11/27.
 */
@Data
public class ColorAttrRuleBo {
    @ApiModelProperty(value = "颜色属性值")
    private String colorAttrValue;

    @ApiModelProperty(value = "色系属性值")
    private String colorSysAttrValue;

    @ApiModelProperty(value = "优先级", notes = "数字越小优先级越大")
    private Integer priority;
}
