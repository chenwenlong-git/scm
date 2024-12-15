package com.hete.supply.scm.server.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/2/13 20:32
 */
@Data
@NoArgsConstructor
public class OverseasWarehouseMsgItemVo {
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "海外仓条码")
    private String overseasWarehouseBarCode;
}
