package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportSkuVo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2023/7/5 17:07
 */
@Slf4j
public class DeductOrderDetailConverter {

    public static DeductOrderExportSkuVo deductOrderPoToVo(DeductOrderPo po,
                                                           Map<String, List<PurchaseSettleOrderItemPo>> purchaseSettleOrderItemPoMap) {
        DeductOrderExportSkuVo vo = new DeductOrderExportSkuVo();
        if (null == po) {
            return vo;
        }
        vo.setDeductOrderNo(po.getDeductOrderNo());
        vo.setDeductTypeName(po.getDeductType().getRemark());
        vo.setDeductStatusName(po.getDeductStatus().getRemark());
        vo.setSupplierCode(po.getSupplierCode());
        vo.setDeductPriceTotal(po.getDeductPrice());
        vo.setHandleUsername(po.getHandleUsername());
        if (purchaseSettleOrderItemPoMap.containsKey(po.getDeductOrderNo())) {
            vo.setSettleOrderNo(purchaseSettleOrderItemPoMap.get(po.getDeductOrderNo()).get(0).getPurchaseSettleOrderNo());
        }
        return vo;
    }


}
