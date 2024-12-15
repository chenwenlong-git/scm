package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/15 20:31
 */
@Data
@NoArgsConstructor
public class RawSampleItemVo {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderRawId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;
}
