package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/4/3 18:25
 */
@Data
@NoArgsConstructor
public class SmSkuVo {
    @ApiModelProperty(value = "辅料名称")
    private String subsidiaryMaterialName;

    @ApiModelProperty(value = "辅料sku")
    private String smSku;
}
