package com.hete.supply.scm.server.scm.stockup.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/1/9 20:11
 */
@Data
@NoArgsConstructor
public class StockUpCreateItemDto {
    @ApiModelProperty(value = "SKU")
    private String sku;

    @Positive(message = "下单数必须为正整数")
    @ApiModelProperty(value = "下单数")
    private Integer placeOrderCnt;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "要求回货时间")
    private LocalDateTime requestReturnGoodsDate;
}
