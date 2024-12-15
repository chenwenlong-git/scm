package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/3/25 15:54
 */
@Data
@NoArgsConstructor
public class PurchaseEndStatusVo {

    @ApiModelProperty(value = "采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购需求单")
    private PurchaseParentOrderPo purchaseParentOrderPo;
}
