package com.hete.supply.scm.server.supplier.purchase.service.base;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseModifyOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseModifyOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseModifyOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseModifyOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseModifyVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSimpleVo;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/21 20:10
 */
@Service
@RequiredArgsConstructor
@Validated
public class PurchaseReturnBaseService {
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseModifyOrderDao purchaseModifyOrderDao;
    private final PurchaseModifyOrderItemDao purchaseModifyOrderItemDao;

    public List<PurchaseReturnSimpleVo> getPurchaseReturnByPurchaseChildNo(List<String> childOrderNoList) {
        final List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByPurchaseChildNoList(childOrderNoList);
        if (CollectionUtils.isEmpty(purchaseReturnOrderPoList)) {
            return Collections.emptyList();
        }

        return purchaseReturnOrderPoList.stream()
                .map(po -> {
                    final PurchaseReturnSimpleVo purchaseReturnSimpleVo = new PurchaseReturnSimpleVo();
                    purchaseReturnSimpleVo.setPurchaseReturnOrderNo(po.getReturnOrderNo());
                    purchaseReturnSimpleVo.setReturnOrderStatus(po.getReturnOrderStatus());
                    purchaseReturnSimpleVo.setCreateTime(po.getCreateTime());
                    purchaseReturnSimpleVo.setReturnCnt(po.getRealityReturnCnt());
                    purchaseReturnSimpleVo.setLogistics(po.getLogistics());
                    purchaseReturnSimpleVo.setTrackingNo(po.getTrackingNo());
                    purchaseReturnSimpleVo.setPurchaseChildOrderNo(po.getPurchaseChildOrderNo());
                    return purchaseReturnSimpleVo;

                }).collect(Collectors.toList());
    }

    public List<PurchaseModifyVo> getPurchaseModifyByPurchaseChildNo(List<String> childOrderNoList) {
        List<PurchaseModifyOrderPo> purchaseModifyOrderPoList = purchaseModifyOrderDao.getByChildOrderNoList(childOrderNoList);
        final List<String> downReturnNoList = purchaseModifyOrderPoList.stream()
                .map(PurchaseModifyOrderPo::getDownReturnOrderNo)
                .collect(Collectors.toList());
        List<PurchaseModifyOrderItemPo> purchaseModifyOrderItemPoList = purchaseModifyOrderItemDao.getListByNoList(downReturnNoList);


        return PurchaseConverter.modifyPoToVo(purchaseModifyOrderPoList, purchaseModifyOrderItemPoList);
    }

    public Map<String, Integer> getReturnCntByReturnBizNoList(List<String> returnBizNoList) {
        final List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnBizNoList(returnBizNoList);
        final List<String> returnOrderNoList = purchaseReturnOrderPoList.stream()
                .map(PurchaseReturnOrderPo::getReturnOrderNo)
                .collect(Collectors.toList());

        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnNoList(returnOrderNoList);

        return purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getSku,
                        Collectors.summingInt(PurchaseReturnOrderItemPo::getExpectedReturnCnt)));

    }

    public Map<String, Integer> getChildOrderReturnCntByReturnBizNoMap(Map<String, String> deliverChildNoMap,
                                                                       Map<String, List<String>> deliverNoReturnNoMap) {
        final Map<String, List<String>> purchaseChildNoReturnNoMap = deliverNoReturnNoMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> deliverChildNoMap.getOrDefault(entry.getKey(), entry.getKey()),
                        Map.Entry::getValue, (item1, item2) -> item1));

        final List<String> returnOrderNoList = deliverNoReturnNoMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = purchaseReturnOrderItemDao.getListByReturnNoList(returnOrderNoList);

        final Map<String, Integer> returnOrderNoCntMap = purchaseReturnOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getReturnOrderNo,
                        Collectors.summingInt(PurchaseReturnOrderItemPo::getExpectedReturnCnt)));

        Map<String, String> returnNoPurchaseChildNoMap = purchaseChildNoReturnNoMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(value -> new AbstractMap.SimpleEntry<>(entry.getKey(), value)))
                .collect(Collectors.toMap(
                        Map.Entry::getValue,
                        Map.Entry::getKey,
                        (a, b) -> {
                            throw new IllegalStateException();
                        },
                        LinkedHashMap::new
                ));

        return returnOrderNoCntMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> returnNoPurchaseChildNoMap.getOrDefault(entry.getKey(), entry.getKey()),
                        Map.Entry::getValue,
                        Integer::sum));
    }
}
