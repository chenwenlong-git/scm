package com.hete.supply.scm.server.scm.purchase.handler;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseRawBizType;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.enums.PurchaseSplitRaw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2024/3/22 00:47
 */
@Service
@RequiredArgsConstructor
public class SupplierRawStrategy implements PurchaseRawStrategy {
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;

    @Override
    public void dealSplitPurchaseRaw(List<PurchaseChildOrderRawPo> newPurchaseChildOrderRawPoList,
                                     Map<Long, PurchaseChildOrderRawPo> idRawPoMap,
                                     List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                                     PurchaseChildOrderPo purchaseChildOrderPo,
                                     PurchaseChildOrderPo newPurchaseChildOrderPo,
                                     Integer purchaseTotal) {
        final PurchaseChildOrderRawPo purchaseChildOrderRawPo = purchaseChildOrderRawPoList.get(0);
        // 供应商来源的只有DEMAND类型
        newPurchaseChildOrderRawPoList.forEach(newRawPo -> {
            final PurchaseChildOrderRawPo rawPo = idRawPoMap.get(newRawPo.getPurchaseChildOrderRawId());
            // 拆分订单分配数量、需求数量
            final int dispenseCnt = BigDecimal.valueOf(newPurchaseChildOrderPo.getPurchaseTotal())
                    .multiply(BigDecimal.valueOf(rawPo.getDispenseCnt()))
                    .divide(BigDecimal.valueOf(purchaseTotal), 0, RoundingMode.HALF_UP).intValue();
            final int supplyDeliveryCnt = BigDecimal.valueOf(newPurchaseChildOrderPo.getPurchaseTotal())
                    .multiply(BigDecimal.valueOf(rawPo.getDeliveryCnt()))
                    .divide(BigDecimal.valueOf(purchaseTotal), 0, RoundingMode.HALF_UP).intValue();

            // 如果是FORMULA类型，则保持deliver和dispense不变
            if (PurchaseRawBizType.FORMULA.equals(rawPo.getPurchaseRawBizType())) {
                newRawPo.setDeliveryCnt(rawPo.getDeliveryCnt());
                newRawPo.setDispenseCnt(rawPo.getDispenseCnt());
            } else {
                newRawPo.setDeliveryCnt(supplyDeliveryCnt);
                newRawPo.setDispenseCnt(dispenseCnt);
                // 原订单原料需求减少补交拆出去的部分
                rawPo.setDeliveryCnt(rawPo.getDeliveryCnt() - supplyDeliveryCnt);
                rawPo.setDispenseCnt(rawPo.getDispenseCnt() - dispenseCnt);
            }
            newRawPo.setActualConsumeCnt(0);
            newRawPo.setExtraCnt(0);
            newRawPo.setRawExtra(purchaseChildOrderRawPo.getRawExtra());
        });

        // 清空id
        newPurchaseChildOrderRawPoList.forEach(newRawPo -> newRawPo.setPurchaseChildOrderRawId(null));

        purchaseChildOrderRawDao.updateBatchByIdVersion(purchaseChildOrderRawPoList);
        purchaseChildOrderRawDao.insertBatch(newPurchaseChildOrderRawPoList);
    }

    @Override
    public PurchaseSplitRaw getHandlerType() {
        return PurchaseSplitRaw.BEFORE_DELIVER_SUPPLIER;
    }
}
