package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.GoodsPriceMaintain;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SkuCapBo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * sku关联的业务信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-28
 */
@Component
@Validated
public class SkuInfoDao extends BaseDao<SkuInfoMapper, SkuInfoPo> {

    public List<SkuInfoPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SkuInfoPo>lambdaQuery()
                .in(SkuInfoPo::getSku, skuList));
    }

    public SkuInfoPo getBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<SkuInfoPo>lambdaQuery()
                .eq(SkuInfoPo::getSku, sku));
    }

    public List<SkuInfoPo> getListByGoodsPriceMaintain(GoodsPriceMaintain goodsPriceMaintain) {
        if (goodsPriceMaintain == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SkuInfoPo>lambdaQuery()
                .in(SkuInfoPo::getGoodsPriceMaintain, goodsPriceMaintain));
    }

    public Map<String, SkuInfoPo> getMapBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<SkuInfoPo>lambdaQuery()
                        .in(SkuInfoPo::getSku, skuList))
                .stream()
                .collect(Collectors.toMap(SkuInfoPo::getSku, Function.identity()));
    }

    public List<SkuCapBo> listByCapBo(Collection<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.listByCapBo(skuList);
    }
}
