package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemRawPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产信息详情原料表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Component
@Validated
public class ProduceDataItemRawDao extends BaseDao<ProduceDataItemRawMapper, ProduceDataItemRawPo> {

    public List<ProduceDataItemRawPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemRawPo>lambdaQuery()
                .in(ProduceDataItemRawPo::getSku, skuList));
    }

    public List<ProduceDataItemRawPo> getListBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemRawPo>lambdaQuery()
                .in(ProduceDataItemRawPo::getSpu, spuList));
    }

    public List<ProduceDataItemRawPo> getListBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemRawPo>lambdaQuery()
                .eq(ProduceDataItemRawPo::getSku, sku));
    }

    public void deleteByProduceDataItemIdList(List<Long> produceDataItemIdList) {
        if (CollectionUtils.isEmpty(produceDataItemIdList)) {
            return;
        }
        baseMapper.delete(Wrappers.<ProduceDataItemRawPo>lambdaUpdate()
                .in(ProduceDataItemRawPo::getProduceDataItemId, produceDataItemIdList));
    }

    public List<ProduceDataItemRawPo> getByProduceDataItemIdList(List<Long> produceDataItemIdList) {
        if (CollectionUtils.isEmpty(produceDataItemIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemRawPo>lambdaUpdate()
                .in(ProduceDataItemRawPo::getProduceDataItemId, produceDataItemIdList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return super.removeBatchByIds(list);
    }

    public List<ProduceDataItemRawPo> getByProduceDataItemId(Long produceDataItemId) {
        if (null == produceDataItemId) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemRawPo>lambdaUpdate()
                .eq(ProduceDataItemRawPo::getProduceDataItemId, produceDataItemId));
    }
}
