package com.hete.supply.scm.server.supplier.entity.bo;

import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/27 15:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnOrderResultBo {
    @ApiModelProperty(value = "退货单")
    private PurchaseReturnOrderPo purchaseReturnOrderPo;

    @ApiModelProperty(value = "退货单明细")
    private List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList;
}
