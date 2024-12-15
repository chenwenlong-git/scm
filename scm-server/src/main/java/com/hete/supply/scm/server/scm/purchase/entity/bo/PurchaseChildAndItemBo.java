package com.hete.supply.scm.server.scm.purchase.entity.bo;

import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/4/10 10:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseChildAndItemBo {
    private List<PurchaseChildOrderPo> purchaseChildOrderPoList;

    private List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList;
}
