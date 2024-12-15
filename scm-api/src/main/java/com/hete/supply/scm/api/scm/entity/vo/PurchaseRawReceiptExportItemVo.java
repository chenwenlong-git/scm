package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/6/6 10:58
 */
@Data
@NoArgsConstructor
public class PurchaseRawReceiptExportItemVo {


    @ApiModelProperty(value = "原料sku")
    private String rawSku;

    @ApiModelProperty(value = "原料sku产品名称")
    private String rawSkuEncode;

    @ApiModelProperty(value = "单位bom需求")
    private Integer bomDeliveryCnt;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;


}
