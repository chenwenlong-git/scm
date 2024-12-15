package com.hete.supply.scm.server.scm.production.dao;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.AttributeCategoryPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 供应链属性分类表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Component
@Validated
public class AttributeCategoryDao extends BaseDao<AttributeCategoryMapper, AttributeCategoryPo> {

    public List<AttributeCategoryPo> listByParentAttrCategoryId(Long parentAttrCategoryId) {
        if (Objects.isNull(parentAttrCategoryId)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<AttributeCategoryPo> wrapper
                = Wrappers.<AttributeCategoryPo>lambdaQuery().eq(AttributeCategoryPo::getParentAttributeCategoryId, parentAttrCategoryId);
        return baseMapper.selectList(wrapper);
    }

    public List<AttributeCategoryPo> listChildAttrCategory() {
        return baseMapper.selectList(Wrappers.<AttributeCategoryPo>lambdaQuery().gt(AttributeCategoryPo::getParentAttributeCategoryId, 0));
    }

    public List<AttributeCategoryPo> listAll() {
        return baseMapper.selectList(Wrappers.<AttributeCategoryPo>lambdaQuery().eq(AttributeCategoryPo::getDelTimestamp, 0));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public AttributeCategoryPo getByName(String attributeCategoryName, long parentAttributeCategoryId) {
        return baseMapper.selectOne(
                Wrappers.<AttributeCategoryPo>lambdaQuery()
                        .eq(AttributeCategoryPo::getAttributeCategoryName, attributeCategoryName)
                        .gt(AttributeCategoryPo::getParentAttributeCategoryId, parentAttributeCategoryId)
        );
    }
}
