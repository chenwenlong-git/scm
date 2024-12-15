package com.hete.supply.scm.server.scm.adjust.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.adjust.entity.dto.GoodsPriceGetListDto;
import com.hete.supply.scm.server.scm.adjust.entity.dto.GoodsPriceGetPurchaseListDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.GoodsPricePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceGetListVo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.GoodsPriceGetPurchaseListVo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品价格表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-18
 */
@Component
@Validated
public class GoodsPriceDao extends BaseDao<GoodsPriceMapper, GoodsPricePo> {

    public List<GoodsPricePo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPricePo>lambdaQuery()
                .in(GoodsPricePo::getSku, skuList));
    }

    public List<GoodsPricePo> getListBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPricePo>lambdaQuery()
                .eq(GoodsPricePo::getSku, sku));
    }


    public List<GoodsPricePo> getListByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPricePo>lambdaQuery()
                .in(GoodsPricePo::getGoodsPriceId, idList));
    }

    public List<GoodsPricePo> getListBySkuAndSupplierCode(String sku,
                                                          String supplierCode) {
        if (StringUtils.isBlank(sku) || StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPricePo>lambdaQuery()
                .eq(GoodsPricePo::getSku, sku)
                .eq(GoodsPricePo::getSupplierCode, supplierCode));
    }

    public List<GoodsPricePo> getListBySkuListAndSupplierCode(List<String> skuList,
                                                              String supplierCode) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPricePo>lambdaQuery()
                .in(GoodsPricePo::getSku, skuList)
                .eq(StringUtils.isNotBlank(supplierCode), GoodsPricePo::getSupplierCode, supplierCode));
    }


    public List<GoodsPriceGetListVo> getGoodsPriceList(GoodsPriceGetListDto dto) {
        if (CollectionUtils.isEmpty(dto.getGoodsPriceGetListItemList())) {
            return Collections.emptyList();
        }
        return baseMapper.getGoodsPriceList(dto);
    }

    public List<GoodsPriceGetPurchaseListVo> getGoodsPricePurchaseList(GoodsPriceGetPurchaseListDto dto) {
        if (CollectionUtils.isEmpty(dto.getGoodsPriceGetListPurchaseItemList())
                && CollectionUtils.isEmpty(dto.getSkuUniversalList())) {
            return Collections.emptyList();
        }
        return baseMapper.getGoodsPricePurchaseList(dto);
    }

    public List<GoodsPricePo> getListByChannelIdList(List<Long> channelIdList) {
        if (CollectionUtils.isEmpty(channelIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<GoodsPricePo>lambdaQuery()
                .in(GoodsPricePo::getChannelId, channelIdList));
    }

    public Long getTotalsCnt(String sku) {
        return baseMapper.selectCount(Wrappers.<GoodsPricePo>lambdaQuery()
                .eq(StringUtils.isNotBlank(sku), GoodsPricePo::getSku, sku));
    }

    public IPage<GoodsPricePo> getByPage(Page<GoodsPricePo> page, String sku) {
        return baseMapper.selectPage(page, Wrappers.<GoodsPricePo>lambdaQuery()
                .eq(StringUtils.isNotBlank(sku), GoodsPricePo::getSku, sku));
    }

}
