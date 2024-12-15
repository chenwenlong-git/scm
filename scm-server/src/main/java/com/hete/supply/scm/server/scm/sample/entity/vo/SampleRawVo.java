package com.hete.supply.scm.server.scm.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/3/31 09:55
 */
@Data
@NoArgsConstructor
public class SampleRawVo {
    @ApiModelProperty(value = "id")
    private Long sampleOrderRawId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;
}
