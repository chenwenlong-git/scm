package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/8/17 17:07
 */
@Data
@NoArgsConstructor
public class DevelopPamphletOrderRawListVo {

    @ApiModelProperty(value = "id")
    private Long developPamphletOrderRawId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

}
