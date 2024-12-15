package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemProcessDescPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产信息详情描述 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Component
@Validated
public class ProduceDataItemProcessDescDao extends BaseDao<ProduceDataItemProcessDescMapper, ProduceDataItemProcessDescPo> {

    public List<ProduceDataItemProcessDescPo> getBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemProcessDescPo>lambdaQuery()
                .in(ProduceDataItemProcessDescPo::getSku, skuList));
    }

    public List<ProduceDataItemProcessDescPo> getBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemProcessDescPo>lambdaQuery()
                .in(ProduceDataItemProcessDescPo::getSpu, spuList));
    }

    public List<ProduceDataItemProcessDescPo> getListBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemProcessDescPo>lambdaQuery()
                .eq(ProduceDataItemProcessDescPo::getSku, sku));
    }

    public void deleteByProduceDataItemIdList(List<Long> produceDataItemIdList) {
        if (CollectionUtils.isEmpty(produceDataItemIdList)) {
            return;
        }
        baseMapper.delete(Wrappers.<ProduceDataItemProcessDescPo>lambdaUpdate()
                .in(ProduceDataItemProcessDescPo::getProduceDataItemId, produceDataItemIdList));
    }

    public List<ProduceDataItemProcessDescPo> getByProduceDataItemIdList(List<Long> produceDataItemIdList) {
        if (CollectionUtils.isEmpty(produceDataItemIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataItemProcessDescPo>lambdaUpdate()
                .in(ProduceDataItemProcessDescPo::getProduceDataItemId, produceDataItemIdList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return super.removeBatchByIds(list);
    }
}
