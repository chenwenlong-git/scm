package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.SkuRiskPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品风险表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-29
 */
@Component
@Validated
public class SkuRiskDao extends BaseDao<SkuRiskMapper, SkuRiskPo> {
    public List<Long> listAllIds() {
        return this.baseMapper.selectAllIds();
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SkuRiskPo> listBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SkuRiskPo>lambdaQuery().in(SkuRiskPo::getSku, skuList));
    }
}
