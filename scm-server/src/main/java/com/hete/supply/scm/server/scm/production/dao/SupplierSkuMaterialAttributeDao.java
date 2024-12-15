package com.hete.supply.scm.server.scm.production.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.SupplierSkuMaterialAttributePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 供应商商品原料属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Component
@Validated
public class SupplierSkuMaterialAttributeDao extends BaseDao<SupplierSkuMaterialAttributeMapper, SupplierSkuMaterialAttributePo> {
    public List<SupplierSkuMaterialAttributePo> listBySkuAndSupplierCode(String sku, String supplierCode) {
        if (StrUtil.isBlank(sku)) {
            return Collections.emptyList();
        }
        if (StrUtil.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SupplierSkuMaterialAttributePo> wrapper = Wrappers.<SupplierSkuMaterialAttributePo>lambdaQuery()
                .eq(SupplierSkuMaterialAttributePo::getSupplierCode, supplierCode)
                .eq(SupplierSkuMaterialAttributePo::getSku, sku);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public Set<String> getSupCodeListBySku(String sku) {
        if (StrUtil.isBlank(sku)) {
            return Collections.emptySet();
        }
        return baseMapper.getSupCodeListBySku(sku);
    }
}
