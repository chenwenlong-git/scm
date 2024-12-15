package com.hete.supply.scm.server.supplier.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购退货单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class PurchaseReturnOrderItemDao extends BaseDao<PurchaseReturnOrderItemMapper, PurchaseReturnOrderItemPo> {

    /**
     * 通过单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/9 10:11
     */
    public List<PurchaseReturnOrderItemPo> getListByReturnOrderNo(String returnOrderNo) {
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .eq(PurchaseReturnOrderItemPo::getReturnOrderNo, returnOrderNo));
    }

    public List<PurchaseReturnOrderItemPo> getListByReturnBizNos(Collection<String> returnBizNos) {
        if (CollectionUtils.isEmpty(returnBizNos)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .in(PurchaseReturnOrderItemPo::getReturnBizNo, returnBizNos));
    }

    public List<PurchaseReturnOrderItemPo> getListByReturnNoList(List<String> returnNoList) {
        if (CollectionUtils.isEmpty(returnNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .in(PurchaseReturnOrderItemPo::getReturnOrderNo, returnNoList));
    }

    /**
     * 通过sku批次码查询
     *
     * @author ChenWenLong
     * @date 2023/5/30 14:03
     */
    public List<PurchaseReturnOrderItemPo> getListBySkuBatchCodeList(List<String> skuBatchCodeList) {
        if (CollectionUtils.isEmpty(skuBatchCodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .in(PurchaseReturnOrderItemPo::getSkuBatchCode, skuBatchCodeList));
    }

    /**
     * 通过 sku 查询
     *
     * @param skuList
     * @return
     */
    public List<PurchaseReturnOrderItemPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .in(PurchaseReturnOrderItemPo::getSku, skuList));
    }

    /**
     * 通过产品名称查询
     *
     * @param skuEncodeList
     * @return
     */
    public List<PurchaseReturnOrderItemPo> getListBySkuEncodeList(List<String> skuEncodeList) {
        if (CollectionUtils.isEmpty(skuEncodeList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .in(PurchaseReturnOrderItemPo::getSkuEncode, skuEncodeList));
    }

    /**
     * 通过多个 id 查询
     *
     * @param returnItemIdList
     * @return
     */
    public List<PurchaseReturnOrderItemPo> getListByIdList(List<Long> returnItemIdList) {
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .in(PurchaseReturnOrderItemPo::getPurchaseReturnOrderItemId, returnItemIdList));
    }

    /**
     * 根据来源单号获取退货单
     *
     * @param returnBizNo
     */
    public List<PurchaseReturnOrderItemPo> getListByReturnBizNo(String returnBizNo) {
        if (StringUtils.isBlank(returnBizNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .eq(PurchaseReturnOrderItemPo::getReturnBizNo, returnBizNo));
    }

    /**
     * 模糊 sku 查询
     *
     * @param sku:
     * @return List<PurchaseReturnOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 15:26
     */
    public List<PurchaseReturnOrderItemPo> getListLikeBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .like(PurchaseReturnOrderItemPo::getSku, sku));
    }

    /**
     * 模糊 批次码 查询
     *
     * @param skuBatchCode:
     * @return List<PurchaseReturnOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 15:28
     */
    public List<PurchaseReturnOrderItemPo> getListLikeSkuBatchCode(String skuBatchCode) {
        if (StringUtils.isBlank(skuBatchCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .like(PurchaseReturnOrderItemPo::getSkuBatchCode, skuBatchCode));
    }

    /**
     * 模糊查询产品名称
     *
     * @param skuEncode:
     * @return List<PurchaseReturnOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 15:30
     */
    public List<PurchaseReturnOrderItemPo> getListLikeSkuEncode(String skuEncode) {
        if (StringUtils.isBlank(skuEncode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseReturnOrderItemPo>lambdaQuery()
                .in(PurchaseReturnOrderItemPo::getSkuEncode, skuEncode));
    }
}
