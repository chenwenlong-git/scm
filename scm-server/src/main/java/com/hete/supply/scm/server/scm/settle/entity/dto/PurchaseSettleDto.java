package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/27 16:46
 */
@Data
@NoArgsConstructor
public class PurchaseSettleDto {

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


}
