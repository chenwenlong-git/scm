package com.hete.supply.scm.server.scm.adjust.handler;

import com.hete.supply.scm.server.scm.adjust.dao.AdjustPriceApproveItemDao;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApproveItemPo;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.support.api.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/6/20 17:45
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseAdjustPriceStrategy implements AdjustPriceStrategy {
    private final AdjustPriceApproveItemDao adjustPriceApproveItemDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;

    @Override
    public void agreeHandle(AdjustPriceApprovePo adjustPriceApprovePo) {
        final List<AdjustPriceApproveItemPo> adjustPriceApproveItemPoList = adjustPriceApproveItemDao.getListByNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        if (CollectionUtils.isEmpty(adjustPriceApproveItemPoList)) {
            throw new BizException("审批单:{}不存在明细信息，数据处理失败！", adjustPriceApprovePo.getAdjustPriceApproveNo());
        }
        final List<String> purchaseChildOrderNoList = adjustPriceApproveItemPoList.stream()
                .map(AdjustPriceApproveItemPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        final Map<String, BigDecimal> purchaseChildNoAdjustPriceMap = adjustPriceApproveItemPoList.stream()
                .collect(Collectors.toMap(AdjustPriceApproveItemPo::getPurchaseChildOrderNo,
                        AdjustPriceApproveItemPo::getAdjustPrice));
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
        purchaseChildOrderItemPoList.forEach(itemPo -> {
            final BigDecimal adjustPrice = purchaseChildNoAdjustPriceMap.get(itemPo.getPurchaseChildOrderNo());
            if (null == adjustPrice) {
                throw new BizException("查找不到采购单:{}的审批数据，数据异常", itemPo.getPurchaseChildOrderNo());
            }
            itemPo.setSettlePrice(adjustPrice);
            itemPo.setPurchasePrice(itemPo.getSubstractPrice().add(adjustPrice));
        });
        purchaseChildOrderItemDao.updateBatchByIdVersion(purchaseChildOrderItemPoList);

        // 清空采购单上关联的审批单号
        purchaseChildOrderPoList.forEach(po -> po.setAdjustPriceApproveNo(StringUtils.EMPTY));
        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);
    }

    @Override
    public void refuseHandle(AdjustPriceApprovePo adjustPriceApprovePo) {
        final List<AdjustPriceApproveItemPo> adjustPriceApproveItemPoList = adjustPriceApproveItemDao.getListByNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        if (CollectionUtils.isEmpty(adjustPriceApproveItemPoList)) {
            throw new BizException("审批单:{}不存在明细信息，数据处理失败！", adjustPriceApprovePo.getAdjustPriceApproveNo());
        }
        final List<String> purchaseChildOrderNoList = adjustPriceApproveItemPoList.stream()
                .map(AdjustPriceApproveItemPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);

        // 清空采购单上关联的审批单号
        purchaseChildOrderPoList.forEach(po -> po.setAdjustPriceApproveNo(StringUtils.EMPTY));
        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);
    }

    @Override
    public void failHandle(AdjustPriceApprovePo adjustPriceApprovePo) {
        final List<AdjustPriceApproveItemPo> adjustPriceApproveItemPoList = adjustPriceApproveItemDao.getListByNo(adjustPriceApprovePo.getAdjustPriceApproveNo());
        if (CollectionUtils.isEmpty(adjustPriceApproveItemPoList)) {
            throw new BizException("审批单:{}不存在明细信息，数据处理失败！", adjustPriceApprovePo.getAdjustPriceApproveNo());
        }
        final List<String> purchaseChildOrderNoList = adjustPriceApproveItemPoList.stream()
                .map(AdjustPriceApproveItemPo::getPurchaseChildOrderNo)
                .collect(Collectors.toList());
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);

        // 清空采购单上关联的审批单号
        purchaseChildOrderPoList.forEach(po -> po.setAdjustPriceApproveNo(StringUtils.EMPTY));
        purchaseChildOrderDao.updateBatchByIdVersion(purchaseChildOrderPoList);
    }

    @Override
    public ApproveType getHandlerType() {
        return ApproveType.PURCHASE_ADJUST;
    }
}
