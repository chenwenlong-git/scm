package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.RawSupplier;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawDeliverPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 采购需求子单原料出库单关联 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-03-21
 */
@Component
@Validated
public class PurchaseChildOrderRawDeliverDao extends BaseDao<PurchaseChildOrderRawDeliverMapper, PurchaseChildOrderRawDeliverPo> {

    public List<PurchaseChildOrderRawDeliverPo> getListByChildOrderNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawDeliverPo>lambdaQuery()
                .eq(PurchaseChildOrderRawDeliverPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public List<PurchaseChildOrderRawDeliverPo> getListByChildOrderNo(String purchaseChildOrderNo, RawSupplier rawSupplier) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawDeliverPo>lambdaQuery()
                .eq(PurchaseChildOrderRawDeliverPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderRawDeliverPo::getRawSupplier, rawSupplier));
    }

    public List<PurchaseChildOrderRawDeliverPo> getListByChildOrderNoList(List<String> purchaseChildOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawDeliverPo>lambdaQuery()
                .in(PurchaseChildOrderRawDeliverPo::getPurchaseChildOrderNo, purchaseChildOrderNoList));
    }

    public List<PurchaseChildOrderRawDeliverPo> getListIdListOrNoList(List<String> deliveryOrderNoList, List<Long> supplierInventoryRecordIdList) {
        if (CollectionUtils.isNotEmpty(deliveryOrderNoList) && CollectionUtils.isNotEmpty(supplierInventoryRecordIdList)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawDeliverPo>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(deliveryOrderNoList),
                        PurchaseChildOrderRawDeliverPo::getPurchaseRawDeliverOrderNo, deliveryOrderNoList)
                .in(CollectionUtils.isNotEmpty(supplierInventoryRecordIdList),
                        PurchaseChildOrderRawDeliverPo::getSupplierInventoryRecordId, supplierInventoryRecordIdList));
    }

    public List<PurchaseChildOrderRawDeliverPo> getListByChildOrderNoAndSku(String purchaseChildOrderNo, String sku) {
        if (StringUtils.isBlank(purchaseChildOrderNo) || StringUtils.isBlank(sku)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderRawDeliverPo>lambdaQuery()
                .eq(PurchaseChildOrderRawDeliverPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderRawDeliverPo::getSku, sku));
    }

    public PurchaseChildOrderRawDeliverPo getOneByRawDeliverNo(String purchaseRawDeliverOrderNo) {
        if (StringUtils.isBlank(purchaseRawDeliverOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseChildOrderRawDeliverPo>lambdaQuery()
                .eq(PurchaseChildOrderRawDeliverPo::getPurchaseRawDeliverOrderNo, purchaseRawDeliverOrderNo)
                .last("limit 1"));
    }

    public void deleteByChildNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return;
        }
        baseMapper.deleteSkipCheck(Wrappers.<PurchaseChildOrderRawDeliverPo>lambdaUpdate()
                .eq(PurchaseChildOrderRawDeliverPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public void removeBatchByIds(List<Long> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
