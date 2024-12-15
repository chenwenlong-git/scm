package com.hete.supply.scm.server.scm.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/14 19:54
 */
@Data
@NoArgsConstructor
public class SampleReceiptOrderItemVo {
    @ApiModelProperty(value = "id")
    private Long sampleReceiptOrderItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;
}
