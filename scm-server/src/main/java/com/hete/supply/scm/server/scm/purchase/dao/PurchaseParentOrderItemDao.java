package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购需求母单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class PurchaseParentOrderItemDao extends BaseDao<PurchaseParentOrderItemMapper, PurchaseParentOrderItemPo> {

    public List<PurchaseParentOrderItemPo> getListByParentNo(@NotBlank String purchaseParentOrderNo) {

        return baseMapper.selectList(Wrappers.<PurchaseParentOrderItemPo>lambdaQuery()
                .eq(PurchaseParentOrderItemPo::getPurchaseParentOrderNo, purchaseParentOrderNo));
    }


    public void deleteByParentOrderNo(String purchaseParentOrderNo) {
        if (StringUtils.isBlank(purchaseParentOrderNo)) {
            return;
        }
        baseMapper.deleteSkipCheck(Wrappers.<PurchaseParentOrderItemPo>lambdaUpdate()
                .eq(PurchaseParentOrderItemPo::getPurchaseParentOrderNo, purchaseParentOrderNo));
    }

    public Map<String, List<PurchaseParentOrderItemPo>> getMapByNoList(Set<String> purchaseParentOrderNoSet) {
        if (CollectionUtils.isEmpty(purchaseParentOrderNoSet)) {
            return Collections.emptyMap();
        }

        return baseMapper.selectList(Wrappers.<PurchaseParentOrderItemPo>lambdaQuery()
                        .in(PurchaseParentOrderItemPo::getPurchaseParentOrderNo, purchaseParentOrderNoSet))
                .stream()
                .collect(Collectors.groupingBy(PurchaseParentOrderItemPo::getPurchaseParentOrderNo));
    }

    public List<PurchaseParentOrderItemPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseParentOrderItemPo>lambdaQuery()
                .in(PurchaseParentOrderItemPo::getSku, skuList));
    }


    public List<PurchaseParentOrderItemPo> getListByParentNoList(List<String> purchaseParentNoList) {
        if (CollectionUtils.isEmpty(purchaseParentNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseParentOrderItemPo>lambdaQuery()
                .in(PurchaseParentOrderItemPo::getPurchaseParentOrderNo, purchaseParentNoList));
    }

    public PurchaseParentOrderItemPo getOneByParentNoAndSku(String purchaseParentOrderNo, String sku) {
        if (StringUtils.isBlank(purchaseParentOrderNo)
                || StringUtils.isBlank(sku)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseParentOrderItemPo>lambdaQuery()
                .eq(PurchaseParentOrderItemPo::getPurchaseParentOrderNo, purchaseParentOrderNo)
                .eq(PurchaseParentOrderItemPo::getSku, sku));
    }

    public List<PurchaseParentOrderItemPo> getListBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseParentOrderItemPo>lambdaQuery()
                .like(PurchaseParentOrderItemPo::getSku, sku));
    }
}
