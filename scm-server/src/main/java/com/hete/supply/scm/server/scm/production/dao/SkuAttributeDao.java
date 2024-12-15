package com.hete.supply.scm.server.scm.production.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.SkuAttributePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-18
 */
@Component
@Validated
@Slf4j
public class SkuAttributeDao extends BaseDao<SkuAttributeMapper, SkuAttributePo> {

    public List<SkuAttributePo> listBySkuAndAttrIds(String sku, List<Long> attrIds) {
        if (CollectionUtils.isEmpty(attrIds) || StrUtil.isBlank(sku)) {
            log.info("listBySkuAndAttrIds查询参数为空，返回空列表! sku=>{},attrIds=>{} ", sku, attrIds);
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SkuAttributePo> wrapper
                = Wrappers.<SkuAttributePo>lambdaQuery().eq(SkuAttributePo::getSku, sku).in(SkuAttributePo::getAttributeId, attrIds);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SkuAttributePo> listBySku(String sku) {
        if (StrUtil.isBlank(sku)) {
            log.info("商品属性查询参数为空，返回空列表! sku=>{} ", sku);
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SkuAttributePo> wrapper
                = Wrappers.<SkuAttributePo>lambdaQuery().eq(SkuAttributePo::getSku, sku);
        return baseMapper.selectList(wrapper);
    }

    public List<SkuAttributePo> listByAttrIds(List<Long> attrIds) {
        if (CollectionUtils.isEmpty(attrIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SkuAttributePo> wrapper
                = Wrappers.<SkuAttributePo>lambdaQuery().in(SkuAttributePo::getAttributeId, attrIds);
        return baseMapper.selectList(wrapper);
    }
}
