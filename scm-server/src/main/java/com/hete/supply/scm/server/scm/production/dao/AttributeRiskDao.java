package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.AttributeRiskPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 供应链属性风险表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-26
 */
@Component
@Validated
public class AttributeRiskDao extends BaseDao<AttributeRiskMapper, AttributeRiskPo> {

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<AttributeRiskPo> listAll(int maxLimit) {
        return baseMapper.listAll(maxLimit);
    }

    @Override
    public List<AttributeRiskPo> listByIds(Collection<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return super.listByIds(idList);
    }

    public List<AttributeRiskPo> listByAttrId(Long attrId) {
        if (Objects.isNull(attrId)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<AttributeRiskPo>lambdaQuery().eq(AttributeRiskPo::getAttributeId, attrId));
    }
}
