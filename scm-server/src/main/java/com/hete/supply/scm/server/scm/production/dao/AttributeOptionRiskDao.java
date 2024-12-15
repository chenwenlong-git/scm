package com.hete.supply.scm.server.scm.production.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.AttributeOptionRiskPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应链属性可选项风险表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-26
 */
@Component
@Validated
public class AttributeOptionRiskDao extends BaseDao<AttributeOptionRiskMapper, AttributeOptionRiskPo> {

    public List<AttributeOptionRiskPo> listByAttrRiskIds(List<Long> attrRiskIds) {
        if (CollectionUtils.isEmpty(attrRiskIds)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<AttributeOptionRiskPo>lambdaQuery().in(AttributeOptionRiskPo::getAttributeRiskId, attrRiskIds));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
