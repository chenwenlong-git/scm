package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * SKU生产资料信息
 *
 * @author ChenWenLong
 * @date 2023/10/12 14:39
 */
@Data
@NoArgsConstructor
public class SkuProduceDataVo {
    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "重量")
    private BigDecimal weight;


    @ApiModelProperty(value = "生产周期")
    private BigDecimal cycle;


}
