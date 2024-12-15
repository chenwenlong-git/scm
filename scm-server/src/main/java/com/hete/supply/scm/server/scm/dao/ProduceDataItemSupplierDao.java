package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataItemGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemSupplierPo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataItemGetSuggestSupplierVo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产信息详情关联供应商 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-23
 */
@Component
@Validated
public class ProduceDataItemSupplierDao extends BaseDao<ProduceDataItemSupplierMapper, ProduceDataItemSupplierPo> {

    public List<ProduceDataItemSupplierPo> getByProduceDataItemIdList(List<Long> produceDataItemIdList) {
        if (CollectionUtils.isEmpty(produceDataItemIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemSupplierPo>lambdaUpdate()
                .in(ProduceDataItemSupplierPo::getProduceDataItemId, produceDataItemIdList));
    }

    public void deleteByProduceDataItemIdList(List<Long> produceDataItemIdList) {
        if (CollectionUtils.isEmpty(produceDataItemIdList)) {
            return;
        }
        baseMapper.delete(Wrappers.<ProduceDataItemSupplierPo>lambdaUpdate()
                .in(ProduceDataItemSupplierPo::getProduceDataItemId, produceDataItemIdList));
    }

    /**
     * 批量删除
     *
     * @param list:
     * @return boolean
     * @author ChenWenLong
     * @date 2024/3/18 11:27
     */
    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ProduceDataItemGetSuggestSupplierVo> getListBySuggestSupplierBoList(List<ProduceDataItemGetSuggestSupplierBo> boList) {
        if (CollectionUtils.isEmpty(boList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListBySuggestSupplierBoList(boList);
    }

    public List<ProduceDataItemSupplierPo> getBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemSupplierPo>lambdaUpdate()
                .in(ProduceDataItemSupplierPo::getSku, skuList));
    }
}
