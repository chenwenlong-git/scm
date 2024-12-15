package com.hete.supply.scm.server.supplier.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/14 17:29
 */
@Data
@NoArgsConstructor
public class SampleDeliverItemVo {

    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    // 下单人  采购数
}
