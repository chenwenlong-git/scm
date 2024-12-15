package com.hete.supply.scm.server.supplier.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购发货单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class PurchaseDeliverOrderItemDao extends BaseDao<PurchaseDeliverOrderItemMapper, PurchaseDeliverOrderItemPo> {

    public List<PurchaseDeliverOrderItemPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderItemPo>lambdaQuery()
                .in(PurchaseDeliverOrderItemPo::getSku, skuList));
    }

    public List<PurchaseDeliverOrderItemPo> getListByDeliverOrderNo(String purchaseDeliverOrderNo) {
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderItemPo>lambdaQuery()
                .eq(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNo));
    }

    public List<PurchaseDeliverOrderItemPo> getListByDeliverOrderNoList(List<String> purchaseDeliverOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseDeliverOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderItemPo>lambdaQuery()
                .in(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNoList));
    }

    public Map<String, List<PurchaseDeliverOrderItemPo>> getMapByDeliverOrderNoList(List<String> purchaseDeliverOrderNoList) {
        final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = this.getListByDeliverOrderNoList(purchaseDeliverOrderNoList);
        return purchaseDeliverOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo));
    }

    public void removeByDeliverNoList(List<String> purchaseDeliverOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseDeliverOrderNoList)) {
            return;
        }
        baseMapper.delete(Wrappers.<PurchaseDeliverOrderItemPo>lambdaUpdate()
                .in(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNoList));
    }

    /**
     * 根据采购母单号获取发货总数
     *
     * @param purchaseParentOrderNo
     * @return
     */
    public int getDeliverCntByPurchaseParentNo(String purchaseParentOrderNo) {
        if (StringUtils.isBlank(purchaseParentOrderNo)) {
            return 0;
        }

        return baseMapper.getDeliverCntByPurchaseParentNo(purchaseParentOrderNo, Arrays.asList(DeliverOrderStatus.DELETED, DeliverOrderStatus.WAIT_DELIVER));
    }

    /**
     * 通过sku批次码查询
     *
     * @author ChenWenLong
     * @date 2023/5/30 14:03
     */
    public List<PurchaseDeliverOrderItemPo> getListBySkuBatchCodeList(List<String> skuBatchCodeList) {
        if (CollectionUtils.isEmpty(skuBatchCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderItemPo>lambdaQuery()
                .in(PurchaseDeliverOrderItemPo::getSkuBatchCode, skuBatchCodeList));
    }

    /**
     * 通过sku模糊搜索
     *
     * @param sku:
     * @return List<PurchaseDeliverOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 15:55
     */
    public List<PurchaseDeliverOrderItemPo> getListByLikeSku(String sku) {
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderItemPo>lambdaQuery()
                .like(PurchaseDeliverOrderItemPo::getSku, sku));
    }


    /**
     * 通过sku批次码模糊查询
     *
     * @param skuBatchCode:
     * @return List<PurchaseDeliverOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 16:12
     */
    public List<PurchaseDeliverOrderItemPo> getListByLikeSkuBatchCode(String skuBatchCode) {
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderItemPo>lambdaQuery()
                .like(PurchaseDeliverOrderItemPo::getSkuBatchCode, skuBatchCode));
    }

}
