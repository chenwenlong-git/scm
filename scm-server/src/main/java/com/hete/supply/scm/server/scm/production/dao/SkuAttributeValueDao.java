package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.production.entity.po.SkuAttributeValuePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品属性值表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-18
 */
@Component
@Validated
public class SkuAttributeValueDao extends BaseDao<SkuAttributeValueMapper, SkuAttributeValuePo> {

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SkuAttributeValuePo> listBySkuAttrIds(List<Long> skuAttrIds) {
        if (CollectionUtils.isEmpty(skuAttrIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SkuAttributeValuePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuAttributeValuePo::getSkuAttributeId, skuAttrIds);
        return baseMapper.selectList(queryWrapper);
    }
}
