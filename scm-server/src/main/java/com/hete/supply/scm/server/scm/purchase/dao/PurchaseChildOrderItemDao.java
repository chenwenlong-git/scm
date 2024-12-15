package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseItemByReceiveTimeBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购需求子单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class PurchaseChildOrderItemDao extends BaseDao<PurchaseChildOrderItemMapper, PurchaseChildOrderItemPo> {

    /**
     * 通过子单号
     *
     * @author ChenWenLong
     * @date 2022/11/9 10:11
     */
    public List<PurchaseChildOrderItemPo> getListByChildNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .eq(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public List<PurchaseChildOrderItemPo> getListBySku(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .in(PurchaseChildOrderItemPo::getSku, skuList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        return super.removeBatchByIds(list);
    }

    public List<PurchaseChildOrderItemPo> getListByChildNoList(List<String> purchaseChildOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .in(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, purchaseChildOrderNoList));
    }

    public List<PurchaseChildOrderItemPo> getListByParent(String purchaseParentOrderNo) {
        if (StringUtils.isBlank(purchaseParentOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .eq(PurchaseChildOrderItemPo::getPurchaseParentOrderNo, purchaseParentOrderNo));
    }

    public void deleteByChildNoList(List<String> noList) {
        if (CollectionUtils.isEmpty(noList)) {
            return;
        }
        baseMapper.deleteSkipCheck(Wrappers.<PurchaseChildOrderItemPo>lambdaUpdate()
                .in(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, noList));
    }

    public PurchaseChildOrderItemPo getOneBySkuBatchCode(String skuBatchCode) {
        if (StringUtils.isBlank(skuBatchCode)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .eq(PurchaseChildOrderItemPo::getSkuBatchCode, skuBatchCode));
    }

    public List<PurchaseChildOrderItemPo> getListByChildNoListAndSkuList(List<String> purchaseChildNoList,
                                                                         List<String> skuList) {
        if (CollectionUtils.isEmpty(purchaseChildNoList) || CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }


        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .in(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, purchaseChildNoList)
                .in(PurchaseChildOrderItemPo::getSku, skuList));
    }

    public List<PurchaseChildOrderItemPo> getListByParentNoList(List<String> purchaseParentOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseParentOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .in(PurchaseChildOrderItemPo::getPurchaseParentOrderNo, purchaseParentOrderNoList));
    }

    public PurchaseChildOrderItemPo getOneByChildOrderNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .eq(PurchaseChildOrderItemPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .last("limit 1"));
    }

    public List<PurchaseChildOrderItemPo> getListByParentNoAndSku(String purchaseParentOrderNo, String sku) {
        if (StringUtils.isBlank(purchaseParentOrderNo)
                || StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .eq(PurchaseChildOrderItemPo::getPurchaseParentOrderNo, purchaseParentOrderNo)
                .eq(PurchaseChildOrderItemPo::getSku, sku));

    }

    public List<PurchaseChildOrderItemPo> getListBySkuBatchCode(List<String> skuBatchCodeList) {
        if (CollectionUtils.isEmpty(skuBatchCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .in(PurchaseChildOrderItemPo::getSkuBatchCode, skuBatchCodeList));
    }

    public List<PurchaseChildOrderItemPo> getListByLikeSkuBatchCode(String skuBatchCode) {
        if (StringUtils.isBlank(skuBatchCode)) {
            return null;
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .like(PurchaseChildOrderItemPo::getSkuBatchCode, skuBatchCode));
    }

    /**
     * 通过sku模糊查询
     *
     * @param sku:
     * @return List<PurchaseChildOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 18:11
     */
    public List<PurchaseChildOrderItemPo> getListByLikeSku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .like(PurchaseChildOrderItemPo::getSku, sku));
    }

    public Long getCnt() {
        return baseMapper.selectCount(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .ne(PurchaseChildOrderItemPo::getSkuBatchCode, ""));
    }

    public CommonPageResult.PageInfo<PurchaseChildOrderItemPo> getPage(Page<PurchaseChildOrderItemPo> page) {

        final IPage<PurchaseChildOrderItemPo> pageResult = baseMapper.getPage(page);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<PurchaseItemByReceiveTimeBo> getListByPurchaseItemAndReceiveTime(List<String> skuList,
                                                                                 List<PurchaseOrderStatus> purchaseOrderStatusExcludeList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListByPurchaseItemAndReceiveTime(skuList, purchaseOrderStatusExcludeList);
    }

    public List<PurchaseGetSuggestSupplierBo> getListBySkuListAndNotStatus(List<String> skuList,
                                                                           PurchaseOrderStatus notPurchaseOrderStatus,
                                                                           LocalDateTime planConfirmTimeStart,
                                                                           LocalDateTime planConfirmTimeEnd) {
        return baseMapper.getListBySkuListAndNotStatus(skuList, notPurchaseOrderStatus, planConfirmTimeStart, planConfirmTimeEnd);
    }

    public List<PurchaseChildOrderItemPo> listBySkuListAndTimeRange(List<String> skuList,
                                                                    LocalDateTime createTimeBegin,
                                                                    LocalDateTime createTimeEnd) {
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderItemPo>lambdaQuery()
                .in(PurchaseChildOrderItemPo::getSku, skuList)
                .ge(PurchaseChildOrderItemPo::getCreateTime, createTimeBegin)
                .le(PurchaseChildOrderItemPo::getCreateTime, createTimeEnd));
    }
}
