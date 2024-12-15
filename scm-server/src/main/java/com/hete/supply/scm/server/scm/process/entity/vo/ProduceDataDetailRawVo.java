package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/10/17 13:45
 */
@Data
@NoArgsConstructor
public class ProduceDataDetailRawVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

}
