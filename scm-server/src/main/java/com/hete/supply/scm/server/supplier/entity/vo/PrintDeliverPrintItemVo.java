package com.hete.supply.scm.server.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/2/14 22:37
 */
@Data
@NoArgsConstructor
public class PrintDeliverPrintItemVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;
}
