package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/4/7 20:10
 */
@Data
@NoArgsConstructor
public class PurchaseRawDispenseMsgItemVo {
    @ApiModelProperty(value = "原料sku")
    private String sku;

    @ApiModelProperty(value = "分配数量")
    private Integer dispenseCnt;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;
}
