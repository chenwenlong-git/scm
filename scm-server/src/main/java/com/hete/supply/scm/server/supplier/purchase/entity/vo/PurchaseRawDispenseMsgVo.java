package com.hete.supply.scm.server.supplier.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/4/11 10:18
 */
@Data
@NoArgsConstructor
public class PurchaseRawDispenseMsgVo {
    @ApiModelProperty(value = "id")
    private Long supplierInventoryRecordId;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "分配信息")
    private List<PurchaseRawDispenseMsgItemVo> purchaseRawDispenseMsgItemList;
}
