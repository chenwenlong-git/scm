package com.hete.supply.scm.server.scm.production.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.production.entity.po.AttributeOptionPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 供应链属性可选值表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Component
@Validated
public class AttributeOptionDao extends BaseDao<AttributeOptionMapper, AttributeOptionPo> {

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<AttributeOptionPo> listByAttrId(Long attrId) {
        if (Objects.isNull(attrId)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AttributeOptionPo> wrapper
                = Wrappers.<AttributeOptionPo>lambdaQuery().eq(AttributeOptionPo::getAttributeId, attrId);
        return baseMapper.selectList(wrapper);
    }

    public List<AttributeOptionPo> listByAttrIds(List<Long> attrIds) {
        if (CollectionUtils.isEmpty(attrIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AttributeOptionPo> wrapper
                = Wrappers.<AttributeOptionPo>lambdaQuery().in(AttributeOptionPo::getAttributeId, attrIds);
        return baseMapper.selectList(wrapper);
    }

    public List<Long> getIdsByAttrVal(String attributeValue) {
        if (StrUtil.isBlank(attributeValue)) {
            return Collections.emptyList();
        }
        return baseMapper.getIdsByAttrVal(attributeValue);
    }

    @Override
    public List<AttributeOptionPo> listByIds(Collection<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return super.listByIds(idList);
    }
}
