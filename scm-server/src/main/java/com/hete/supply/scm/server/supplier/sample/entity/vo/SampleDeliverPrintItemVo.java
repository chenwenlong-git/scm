package com.hete.supply.scm.server.supplier.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/2/15 14:02
 */
@Data
@NoArgsConstructor
public class SampleDeliverPrintItemVo {
    @ApiModelProperty(value = "样品发货单号")
    private String sampleDeliverOrderNo;


    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

}
