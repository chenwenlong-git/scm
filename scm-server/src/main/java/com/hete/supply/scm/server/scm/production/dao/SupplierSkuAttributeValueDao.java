package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hete.supply.scm.server.scm.production.entity.po.SupplierSkuAttributeValuePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应商商品属性值表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-20
 */
@Component
@Validated
public class SupplierSkuAttributeValueDao extends BaseDao<SupplierSkuAttributeValueMapper, SupplierSkuAttributeValuePo> {
    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SupplierSkuAttributeValuePo> listBySupSkuAttrIds(List<Long> supSkuAttrIds) {
        if (CollectionUtils.isEmpty(supSkuAttrIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SupplierSkuAttributeValuePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SupplierSkuAttributeValuePo::getSupplierSkuAttributeId, supSkuAttrIds);
        return baseMapper.selectList(queryWrapper);
    }
}
