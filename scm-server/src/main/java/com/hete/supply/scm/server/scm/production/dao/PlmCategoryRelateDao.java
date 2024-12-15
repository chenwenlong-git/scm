package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.PlmCategoryRelatePo;
import com.hete.supply.scm.server.scm.production.enums.PlmCategoryRelateBizType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品分类与业务关联表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-24
 */
@Component
@Validated
public class PlmCategoryRelateDao extends BaseDao<PlmCategoryRelateMapper, PlmCategoryRelatePo> {

    public List<PlmCategoryRelatePo> listByBizIdsAndBizType(List<Long> bizIds, PlmCategoryRelateBizType bizType) {
        if (CollectionUtils.isEmpty(bizIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PlmCategoryRelatePo> wrapper = Wrappers.<PlmCategoryRelatePo>lambdaQuery()
                .in(PlmCategoryRelatePo::getBizId, bizIds)
                .eq(PlmCategoryRelatePo::getBizType, bizType);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }


    public List<PlmCategoryRelatePo> listByCategoryIds(List<Long> plmCategoryIds, PlmCategoryRelateBizType bizType) {
        if (CollectionUtils.isEmpty(plmCategoryIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PlmCategoryRelatePo> wrapper
                = Wrappers.<PlmCategoryRelatePo>lambdaQuery().in(PlmCategoryRelatePo::getCategoryId, plmCategoryIds)
                .eq(PlmCategoryRelatePo::getBizType, bizType);
        return baseMapper.selectList(wrapper);
    }
}
