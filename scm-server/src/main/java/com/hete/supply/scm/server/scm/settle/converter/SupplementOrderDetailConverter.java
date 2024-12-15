package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportSkuVo;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2023/7/5 17:07
 */
@Slf4j
public class SupplementOrderDetailConverter {

    public static SupplementOrderExportSkuVo supplementOrderPoToVo(SupplementOrderPo po,
                                                                   Map<String, List<PurchaseSettleOrderItemPo>> purchaseSettleOrderItemPoMap) {
        SupplementOrderExportSkuVo vo = new SupplementOrderExportSkuVo();
        if (null == po) {
            return vo;
        }
        vo.setSupplementOrderNo(po.getSupplementOrderNo());
        vo.setSupplementTypeName(po.getSupplementType().getRemark());
        vo.setSupplementStatusName(po.getSupplementStatus().getRemark());
        vo.setSupplierCode(po.getSupplierCode());
        vo.setSupplementPriceTotal(po.getSupplementPrice());
        vo.setHandleUsername(po.getHandleUsername());
        if (purchaseSettleOrderItemPoMap.containsKey(po.getSupplementOrderNo())) {
            vo.setSettleOrderNo(purchaseSettleOrderItemPoMap.get(po.getSupplementOrderNo()).get(0).getPurchaseSettleOrderNo());
        }
        return vo;
    }


}
