package com.hete.supply.scm.server.supplier.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购原料收货单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-28
 */
@Component
@Validated
public class PurchaseRawReceiptOrderItemDao extends BaseDao<PurchaseRawReceiptOrderItemMapper, PurchaseRawReceiptOrderItemPo> {

    public List<PurchaseRawReceiptOrderItemPo> getListByNo(@NotBlank String purchaseRawReceiptOrderNo) {
        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderItemPo>lambdaQuery()
                .eq(PurchaseRawReceiptOrderItemPo::getPurchaseRawReceiptOrderNo, purchaseRawReceiptOrderNo));
    }

    public List<PurchaseRawReceiptOrderItemPo> getListByNoList(List<String> purchaseRawReceiptOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseRawReceiptOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderItemPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderItemPo::getPurchaseRawReceiptOrderNo, purchaseRawReceiptOrderNoList));
    }

    public List<PurchaseRawReceiptOrderItemPo> getListByIdList(List<Long> rawReceiptItemIdList) {
        if (CollectionUtils.isEmpty(rawReceiptItemIdList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderItemPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderItemPo::getPurchaseRawReceiptOrderItemId, rawReceiptItemIdList));
    }

    public List<PurchaseRawReceiptOrderItemPo> getListBySkuBatchCodeList(List<String> skuBatchCodeList) {
        if (CollectionUtils.isEmpty(skuBatchCodeList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderItemPo>lambdaQuery()
                .in(PurchaseRawReceiptOrderItemPo::getSkuBatchCode, skuBatchCodeList));
    }

    /**
     * 通过批次码模糊查询
     *
     * @param skuBatchCode:
     * @return List<PurchaseRawReceiptOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 15:40
     */
    public List<PurchaseRawReceiptOrderItemPo> getListByLikeSkuBatchCode(String skuBatchCode) {
        if (StringUtils.isBlank(skuBatchCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseRawReceiptOrderItemPo>lambdaQuery()
                .like(PurchaseRawReceiptOrderItemPo::getSkuBatchCode, skuBatchCode));
    }
}
