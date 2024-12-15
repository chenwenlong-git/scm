package com.hete.supply.scm.server.scm.purchase.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/11/19 09:50
 */
@Data
@NoArgsConstructor
public class ReceiptSignOffBo {
    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "采购订单")
    private PurchaseChildOrderPo purchaseChildOrderPo;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "收货状态")
    private ReceiptOrderStatus receiptOrderStatus;
}
