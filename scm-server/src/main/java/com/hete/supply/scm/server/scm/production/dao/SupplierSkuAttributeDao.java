package com.hete.supply.scm.server.scm.production.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.SupplierSkuAttributePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 供应商商品属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Component
@Validated
@Slf4j
public class SupplierSkuAttributeDao extends BaseDao<SupplierSkuAttributeMapper, SupplierSkuAttributePo> {

    public List<SupplierSkuAttributePo> listBySupplierCodeAndSkuAndAttrIds(String sku, String supplierCode, List<Long> attrIds) {
        if (CollectionUtils.isEmpty(attrIds) || StrUtil.isBlank(sku) || StrUtil.isBlank(supplierCode)) {
            log.info("listBySupplierCodeAndSkuAndAttrIds查询参数为空，返回空列表! sku=>{},supplierCode=>{},attrIds=>{} ", sku, supplierCode, attrIds);
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SupplierSkuAttributePo> wrapper = Wrappers.<SupplierSkuAttributePo>lambdaQuery()
                .eq(SupplierSkuAttributePo::getSku, sku)
                .eq(SupplierSkuAttributePo::getSupplierCode, supplierCode)
                .in(SupplierSkuAttributePo::getAttributeId, attrIds);
        return baseMapper.selectList(wrapper);
    }

    public List<SupplierSkuAttributePo> listBySkuAndSupplierCode(String sku, String supplierCode) {
        if (StrUtil.isBlank(sku)) {
            log.info("listBySkuAndSupplierCode查询参数为空，返回空列表! sku=>{}", sku);
            return Collections.emptyList();
        }
        if (StrUtil.isBlank(supplierCode)) {
            log.info("listBySkuAndSupplierCode查询参数为空，返回空列表! supplierCode=>{} ", supplierCode);
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SupplierSkuAttributePo> wrapper = Wrappers.<SupplierSkuAttributePo>lambdaQuery()
                .eq(SupplierSkuAttributePo::getSku, sku)
                .eq(SupplierSkuAttributePo::getSupplierCode, supplierCode);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SupplierSkuAttributePo> listByAttrIds(List<Long> attrIds) {
        if (CollectionUtils.isEmpty(attrIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SupplierSkuAttributePo> wrapper
                = Wrappers.<SupplierSkuAttributePo>lambdaQuery().in(SupplierSkuAttributePo::getAttributeId, attrIds);
        return baseMapper.selectList(wrapper);
    }

    public Set<String> getSupCodeListBySku(String sku) {
        if (StrUtil.isBlank(sku)) {
            return Collections.emptySet();
        }
        return baseMapper.getSupCodeListBySku(sku);
    }
}
