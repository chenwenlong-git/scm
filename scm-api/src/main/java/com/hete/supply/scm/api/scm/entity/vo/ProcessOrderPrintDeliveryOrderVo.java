package com.hete.supply.scm.api.scm.entity.vo;

/**
 * @author ChenWenLong
 * @date 2024/1/4 11:25
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/12/25 10:57
 */
@Data
@NoArgsConstructor
public class ProcessOrderPrintDeliveryOrderVo {
    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "计划发货数")
    private Integer deliverCnt;

}