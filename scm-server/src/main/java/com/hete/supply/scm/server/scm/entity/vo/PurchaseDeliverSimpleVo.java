package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/11/14 09:55
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverSimpleVo {
    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "发货单状态")
    private DeliverOrderStatus deliverOrderStatus;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;

    @ApiModelProperty(value = "收货单号")
    private String purchaseReceiptOrderNo;

}
