package com.hete.supply.scm.server.scm.develop.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopOrderPriceBatchQueryBo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopOrderPricePo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 开发单相关单据大货价格 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-07-29
 */
@Component
@Validated
public class DevelopOrderPriceDao extends BaseDao<DevelopOrderPriceMapper, DevelopOrderPricePo> {

    public List<DevelopOrderPricePo> getListByNoAndType(String developOrderNo,
                                                        DevelopOrderPriceType developOrderPriceType) {
        if (StringUtils.isBlank(developOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopOrderPricePo>lambdaQuery()
                .eq(DevelopOrderPricePo::getDevelopOrderNo, developOrderNo)
                .eq(null != developOrderPriceType, DevelopOrderPricePo::getDevelopOrderPriceType, developOrderPriceType));

    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return super.removeBatchByIds(list);
    }

    public List<DevelopOrderPricePo> getListByNoAndTypeBatch(List<DevelopOrderPriceBatchQueryBo> boList) {
        if (CollectionUtils.isEmpty(boList)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DevelopOrderPricePo> queryWrapper = new LambdaQueryWrapper<>();
        for (DevelopOrderPriceBatchQueryBo bo : boList) {
            queryWrapper.or().eq(DevelopOrderPricePo::getDevelopOrderNo, bo.getDevelopOrderNo())
                    .eq(DevelopOrderPricePo::getDevelopOrderPriceType, bo.getDevelopOrderPriceType());
        }

        return baseMapper.selectList(queryWrapper);
    }

    public List<DevelopOrderPricePo> getListByNoListAndTypeList(List<String> developOrderNoList,
                                                                List<DevelopOrderPriceType> developOrderPriceTypeList) {
        if (CollectionUtils.isEmpty(developOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<DevelopOrderPricePo>lambdaQuery()
                .in(DevelopOrderPricePo::getDevelopOrderNo, developOrderNoList)
                .in(CollectionUtils.isNotEmpty(developOrderPriceTypeList), DevelopOrderPricePo::getDevelopOrderPriceType, developOrderPriceTypeList));

    }
}
