package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/19 11:31
 */
@Data
@NoArgsConstructor
public class SmProductVo {
    @ApiModelProperty(value = "id")
    private Long smProductId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "数量")
    private Integer count;
}
