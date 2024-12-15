package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataSpecPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产信息产品规格书 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-11-01
 */
@Component
@Validated
public class ProduceDataSpecDao extends BaseDao<ProduceDataSpecMapper, ProduceDataSpecPo> {

    public List<ProduceDataSpecPo> getListBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataSpecPo>lambdaQuery()
                .eq(ProduceDataSpecPo::getSku, sku)
                .orderByDesc(ProduceDataSpecPo::getUpdateTime));
    }

    public List<ProduceDataSpecPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataSpecPo>lambdaQuery()
                .in(ProduceDataSpecPo::getSku, skuList)
                .orderByDesc(ProduceDataSpecPo::getUpdateTime));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ProduceDataSpecPo> getProduceDataSpecInit() {
        return baseMapper.getProduceDataSpecInit();
    }
}
