package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/1/13 16:44
 */
@Data
@NoArgsConstructor
public class SmSimpleVo {
    @ApiModelProperty(value = "辅料名称")
    private String subsidiaryMaterialName;

    @ApiModelProperty(value = "辅料sku")
    private String smSku;

}
