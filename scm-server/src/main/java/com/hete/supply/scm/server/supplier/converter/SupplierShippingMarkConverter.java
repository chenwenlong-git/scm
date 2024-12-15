package com.hete.supply.scm.server.supplier.converter;

import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkItemPo;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkPo;
import com.hete.supply.scm.server.supplier.entity.vo.ShippingMarkDetailVo;
import com.hete.supply.scm.server.supplier.entity.vo.ShippingMarkItemDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverItemVo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/2/15 18:03
 */
public class SupplierShippingMarkConverter {

    public static ShippingMarkDetailVo getShippingMarkDetailVoByPo(ShippingMarkPo shippingMarkPo) {
        final ShippingMarkDetailVo shippingMarkDetailVo = new ShippingMarkDetailVo();
        shippingMarkDetailVo.setShippingMarkId(shippingMarkPo.getShippingMarkId());
        shippingMarkDetailVo.setVersion(shippingMarkPo.getVersion());
        shippingMarkDetailVo.setShippingMarkNo(shippingMarkPo.getShippingMarkNo());
        shippingMarkDetailVo.setWarehouseCode(shippingMarkPo.getWarehouseCode());
        shippingMarkDetailVo.setWarehouseName(shippingMarkPo.getWarehouseName());
        shippingMarkDetailVo.setIsDirectSend(shippingMarkPo.getIsDirectSend());
        shippingMarkDetailVo.setTotalDeliver(shippingMarkPo.getTotalDeliver());
        shippingMarkDetailVo.setSupplierCode(shippingMarkPo.getSupplierCode());
        shippingMarkDetailVo.setSupplierName(shippingMarkPo.getSupplierName());
        shippingMarkDetailVo.setShippingMarkStatus(shippingMarkPo.getShippingMarkStatus());
        shippingMarkDetailVo.setShippingMarkBizType(shippingMarkPo.getShippingMarkBizType());
        shippingMarkDetailVo.setBoxCnt(shippingMarkPo.getBoxCnt());
        shippingMarkDetailVo.setTrackingNo(shippingMarkPo.getTrackingNo());

        return shippingMarkDetailVo;
    }

    public static List<ShippingMarkItemDetailVo> getShippingMarkItemDetailVoListByPoList(List<ShippingMarkItemPo> shippingMarkItemPoList,
                                                                                         Map<String, List<PurchaseDeliverOrderItemPo>> purchaseDeliverOrderNoMap,
                                                                                         String supplierCode,
                                                                                         Map<String, String> skuEncodeMap,
                                                                                         Map<String, SupplierProductComparePo> supplierProductCompareMap) {
        return Optional.ofNullable(shippingMarkItemPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(itemPo -> {
                    final ShippingMarkItemDetailVo shippingMarkItemDetailVo = new ShippingMarkItemDetailVo();
                    shippingMarkItemDetailVo.setDeliverOrderNo(itemPo.getDeliverOrderNo());
                    shippingMarkItemDetailVo.setBizChildOrderNo(itemPo.getBizChildOrderNo());
                    shippingMarkItemDetailVo.setShippingMarkNum(itemPo.getShippingMarkNum());
                    shippingMarkItemDetailVo.setShippingMarkNumBarCode(itemPo.getShippingMarkNo() + "-" + itemPo.getShippingMarkNum());
                    shippingMarkItemDetailVo.setDeliverCnt(itemPo.getDeliverCnt());
                    final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderNoMap.get(itemPo.getDeliverOrderNo());
                    final List<PurchaseDeliverItemVo> purchaseDeliverItemList = Optional.ofNullable(purchaseDeliverOrderItemPoList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(deliverItemPo -> {
                                final PurchaseDeliverItemVo purchaseDeliverItemVo = new PurchaseDeliverItemVo();
                                purchaseDeliverItemVo.setSku(deliverItemPo.getSku());
                                purchaseDeliverItemVo.setPurchaseCnt(deliverItemPo.getPurchaseCnt());
                                purchaseDeliverItemVo.setSkuEncode(skuEncodeMap.get(deliverItemPo.getSku()));
                                purchaseDeliverItemVo.setSkuBatchCode(deliverItemPo.getSkuBatchCode());
                                final SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(supplierCode + deliverItemPo.getSku());
                                if (null != supplierProductComparePo) {
                                    purchaseDeliverItemVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                                }
                                return purchaseDeliverItemVo;
                            }).collect(Collectors.toList());
                    shippingMarkItemDetailVo.setPurchaseDeliverItemList(purchaseDeliverItemList);
                    return shippingMarkItemDetailVo;
                }).collect(Collectors.toList());
    }

    public static List<ShippingMarkItemDetailVo> getPrintShippingMarkItemDetailVoListByPoList(List<ShippingMarkItemPo> shippingMarkItemPoList) {
        return Optional.ofNullable(shippingMarkItemPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(itemPo -> {
                    final ShippingMarkItemDetailVo shippingMarkItemDetailVo = new ShippingMarkItemDetailVo();
                    shippingMarkItemDetailVo.setDeliverOrderNo(itemPo.getDeliverOrderNo());
                    shippingMarkItemDetailVo.setBizChildOrderNo(itemPo.getBizChildOrderNo());
                    shippingMarkItemDetailVo.setShippingMarkNum(itemPo.getShippingMarkNum());
                    shippingMarkItemDetailVo.setShippingMarkNumBarCode(itemPo.getShippingMarkNo() + "-" + itemPo.getShippingMarkNum());
                    shippingMarkItemDetailVo.setDeliverCnt(itemPo.getDeliverCnt());
                    return shippingMarkItemDetailVo;
                }).collect(Collectors.toList());
    }
}
