package com.hete.supply.scm.server.scm.purchase.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseRawBizType;
import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseChildRawDeliverConverter;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDeliverDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawDeliverPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.enums.PurchaseSplitRaw;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryRecordDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/3/22 00:48
 */
@Service
@RequiredArgsConstructor
public class CompanyRawStrategy implements PurchaseRawStrategy {
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final PurchaseChildOrderRawDeliverDao purchaseChildOrderRawDeliverDao;
    private final SupplierInventoryRecordDao supplierInventoryRecordDao;

    @Override
    public void dealSplitPurchaseRaw(List<PurchaseChildOrderRawPo> newPurchaseChildOrderRawPoList,
                                     Map<Long, PurchaseChildOrderRawPo> idRawPoMap,
                                     List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                                     PurchaseChildOrderPo purchaseChildOrderPo,
                                     PurchaseChildOrderPo newPurchaseChildOrderPo,
                                     Integer purchaseTotal) {
        // 其他供应商/我司的只有DEMAND类型需要处理
        newPurchaseChildOrderRawPoList.forEach(newRawPo -> {
            final PurchaseChildOrderRawPo rawPo = idRawPoMap.get(newRawPo.getPurchaseChildOrderRawId());
            // 拆分订单需求数量
            final int supplyDeliveryCnt = BigDecimal.valueOf(newPurchaseChildOrderPo.getPurchaseTotal())
                    .multiply(BigDecimal.valueOf(rawPo.getDispenseCnt()))
                    .divide(BigDecimal.valueOf(purchaseTotal), 0, RoundingMode.HALF_UP)
                    .intValue();
            newRawPo.setDispenseCnt(supplyDeliveryCnt);
            newRawPo.setRawExtra(RawExtra.NO_NEED_EXTRA);
            //  如果是FORMULA类型,继承单间用量
            if (PurchaseRawBizType.FORMULA.equals(rawPo.getPurchaseRawBizType())) {
                newRawPo.setDeliveryCnt(rawPo.getDeliveryCnt());
            } else {
                newRawPo.setDeliveryCnt(0);
            }

            // 原订单原料需求减少补交拆出去的部分
            rawPo.setDispenseCnt(rawPo.getDispenseCnt() - supplyDeliveryCnt);
        });
        // 清空id
        newPurchaseChildOrderRawPoList.forEach(newRawPo -> newRawPo.setPurchaseChildOrderRawId(null));

        purchaseChildOrderRawDao.updateBatchByIdVersion(purchaseChildOrderRawPoList);
        purchaseChildOrderRawDao.insertBatch(newPurchaseChildOrderRawPoList);


        // 继承原料出库信息
        final List<PurchaseChildOrderRawDeliverPo> newPurchasePoRawDeliverList = purchaseChildOrderRawDeliverDao.getListByChildOrderNo(newPurchaseChildOrderPo.getPurchaseChildOrderNo(), RawSupplier.HETE);
        if (CollectionUtils.isEmpty(newPurchasePoRawDeliverList)) {
            final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList
                    = purchaseChildOrderRawDeliverDao.getListByChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());

            final List<Long> recordIdList = purchaseChildOrderRawDeliverPoList.stream()
                    .filter(po -> RawSupplier.SUPPLIER.equals(po.getRawSupplier()))
                    .map(PurchaseChildOrderRawDeliverPo::getSupplierInventoryRecordId)
                    .collect(Collectors.toList());
            final List<SupplierInventoryRecordPo> supplierInventoryRecordPoList = supplierInventoryRecordDao.getListByIdList(recordIdList);
            final Map<Long, SupplierInventoryCtrlType> idCtrlTypeMap = supplierInventoryRecordPoList.stream()
                    .collect(Collectors.toMap(SupplierInventoryRecordPo::getSupplierInventoryRecordId,
                            SupplierInventoryRecordPo::getSupplierInventoryCtrlType));

            final List<PurchaseChildOrderRawDeliverPo> newPurchaseChildOrderRawDeliverPoList = new ArrayList<>();
            for (PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo : purchaseChildOrderRawDeliverPoList) {
                // 若关联记录的类型是释放或预占，则不需要更新或继承
                if (purchaseChildOrderRawDeliverPo.getSupplierInventoryRecordId() != 0) {
                    final SupplierInventoryCtrlType supplierInventoryCtrlType = idCtrlTypeMap.get(purchaseChildOrderRawDeliverPo.getSupplierInventoryRecordId());
                    if (SupplierInventoryCtrlType.CAMP_ON.equals(supplierInventoryCtrlType) || SupplierInventoryCtrlType.RELEASE.equals(supplierInventoryCtrlType)) {
                        continue;
                    }
                }

                final PurchaseChildOrderRawDeliverPo newPo = PurchaseChildRawDeliverConverter.INSTANCE.convert(purchaseChildOrderRawDeliverPo);
                newPo.setPurchaseChildOrderRawDeliverId(null);
                newPo.setPurchaseParentOrderNo(newPurchaseChildOrderPo.getPurchaseParentOrderNo());
                newPo.setPurchaseChildOrderNo(newPurchaseChildOrderPo.getPurchaseChildOrderNo());

                // 设置分配数
                final int supplyDeliveryCnt = BigDecimal.valueOf(newPurchaseChildOrderPo.getPurchaseTotal())
                        .multiply(BigDecimal.valueOf(purchaseChildOrderRawDeliverPo.getDispenseCnt()))
                        .divide(BigDecimal.valueOf(purchaseTotal), 0, RoundingMode.HALF_UP)
                        .intValue();
                purchaseChildOrderRawDeliverPo.setDispenseCnt(purchaseChildOrderRawDeliverPo.getDispenseCnt() - supplyDeliveryCnt);
                newPo.setDispenseCnt(supplyDeliveryCnt);
                newPurchaseChildOrderRawDeliverPoList.add(newPo);
            }
            purchaseChildOrderRawDeliverDao.updateBatchByIdVersion(purchaseChildOrderRawDeliverPoList);
            purchaseChildOrderRawDeliverDao.insertBatch(newPurchaseChildOrderRawDeliverPoList);
        }
    }

    @Override
    public PurchaseSplitRaw getHandlerType() {
        return PurchaseSplitRaw.BEFORE_DELIVER_COMPANY_OR_OTHER;
    }
}
