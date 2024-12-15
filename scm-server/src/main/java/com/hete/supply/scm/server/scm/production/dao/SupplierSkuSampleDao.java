package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.SupplierSkuSamplePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 供应商商品样品表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-29
 */
@Component
@Validated
public class SupplierSkuSampleDao extends BaseDao<SupplierSkuSampleMapper, SupplierSkuSamplePo> {

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SupplierSkuSamplePo> listBySkuAndSupplierCode(String sku, String supplierCode) {
        return baseMapper.selectList(
                Wrappers.<SupplierSkuSamplePo>lambdaQuery()
                        .eq(SupplierSkuSamplePo::getSupplierCode, supplierCode)
                        .eq(SupplierSkuSamplePo::getSku, sku)
        );
    }

    public Set<String> getSupCodeListBySku(String sku) {
        return baseMapper.getSupCodeListBySku(sku);
    }
}
