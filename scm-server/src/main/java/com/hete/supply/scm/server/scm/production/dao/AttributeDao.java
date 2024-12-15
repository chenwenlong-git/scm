package com.hete.supply.scm.server.scm.production.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.production.entity.dto.GetAttributePageDto;
import com.hete.supply.scm.server.scm.production.entity.po.AttributePo;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributePageVo;
import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeScope;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应链属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-14
 */
@Component
@Validated
public class AttributeDao extends BaseDao<AttributeMapper, AttributePo> {

    public List<Long> getIdsByAttrIdsAndScope(List<Long> attrIds, AttributeScope goods, AttributeStatus status) {
        return baseMapper.getIdsByAttrIdsAndScope(attrIds, goods, status);
    }

    public List<AttributePo> listByAttrIdsAndInputTypes(List<Long> attrIds, List<AttributeInputType> attributeInputTypes) {
        if (CollectionUtils.isEmpty(attributeInputTypes)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(attrIds)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<AttributePo>lambdaQuery()
                .in(AttributePo::getInputType, attributeInputTypes)
                .in(AttributePo::getAttributeId, attrIds));
    }

    public CommonPageResult.PageInfo<AttributePageVo> getByPage(Page<Void> page, GetAttributePageDto dto) {
        IPage<AttributePageVo> pageResult = baseMapper.getByPage(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<AttributePo> listByAttrNameAndScope(String trimAttrName, AttributeScope attributeScope) {
        if (StrUtil.isBlank(trimAttrName)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AttributePo> wrapper = Wrappers.<AttributePo>lambdaQuery()
                .eq(AttributePo::getScope, attributeScope)
                .eq(AttributePo::getAttributeName, trimAttrName);
        return baseMapper.selectList(wrapper);
    }


    public AttributePo getByIdAndVersion(Long attrId, Integer version) {
        return baseMapper.selectOne(Wrappers.<AttributePo>lambdaQuery().eq(AttributePo::getAttributeId, attrId).eq(AttributePo::getVersion, version));
    }

    public List<Long> listIdsByIdsAndInputType(List<Long> attrIds,
                                               List<AttributeInputType> attributeInputTypeList,
                                               AttributeStatus attrStatus) {
        return baseMapper.listIdsByIdsAndInputType(attrIds, attributeInputTypeList, attrStatus);
    }

    @Override
    public List<AttributePo> listByIds(Collection<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return super.listByIds(idList);
    }
}
