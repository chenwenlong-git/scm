package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrSkuDto;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataAttrSkuVo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueBySkuDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueDto;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrValueListDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import com.hete.supply.scm.server.scm.entity.vo.ProduceDataAttrValueListVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 生产信息的生产属性表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Component
@Validated
public class ProduceDataAttrDao extends BaseDao<ProduceDataAttrMapper, ProduceDataAttrPo> {

    public List<ProduceDataAttrPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                .in(ProduceDataAttrPo::getSku, skuList));
    }

    public List<ProduceDataAttrPo> getListBySkuList(List<String> skuList, List<Long> attrNameIdList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(attrNameIdList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                .in(ProduceDataAttrPo::getSku, skuList)
                .in(ProduceDataAttrPo::getAttributeNameId, attrNameIdList));
    }

    public void deleteBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return;
        }
        baseMapper.delete(Wrappers.<ProduceDataAttrPo>lambdaUpdate()
                .in(ProduceDataAttrPo::getSku, skuList));
    }

    public List<ProduceDataAttrPo> getBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                .eq(ProduceDataAttrPo::getSku, sku));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<ProduceDataAttrPo> getProduceDataAttrValue(ProduceDataAttrValueDto dto) {
        if (null == dto) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                .eq(dto.getAttributeNameId() != null,
                        ProduceDataAttrPo::getAttributeNameId, dto.getAttributeNameId())
                .eq(StringUtils.isNotBlank(dto.getAttrValue()),
                        ProduceDataAttrPo::getAttrValue, dto.getAttrValue()));
    }

    public List<ProduceDataAttrPo> produceDataAttrValueBySku(ProduceDataAttrValueBySkuDto dto) {
        if (null == dto) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(dto.getAttributeNameIdList()),
                        ProduceDataAttrPo::getAttributeNameId, dto.getAttributeNameIdList())
                .eq(StringUtils.isNotBlank(dto.getSku()), ProduceDataAttrPo::getSku,
                        dto.getSku()));
    }

    public Set<String> getSkusByContainAttrValues(Long attributeNameId,
                                                  List<String> attrValues) {
        List<ProduceDataAttrPo> produceDataAttrPos
                = baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                .eq(ProduceDataAttrPo::getAttributeNameId, attributeNameId)
                .in(ProduceDataAttrPo::getAttrValue, attrValues));
        return CollectionUtils.isEmpty(produceDataAttrPos) ? Collections.emptySet() :
                produceDataAttrPos.stream().map(ProduceDataAttrPo::getSku).collect(Collectors.toSet());
    }


    public List<ProduceDataAttrPo> listBySkus(Set<String> skuList,
                                              Long attributeNameId) {
        return CollectionUtils.isEmpty(skuList) ? Collections.emptyList() :
                baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                        .in(ProduceDataAttrPo::getSku, skuList)
                        .eq(ProduceDataAttrPo::getAttributeNameId, attributeNameId));
    }

    public CommonPageResult.PageInfo<String> selectProduceDataAttrWeightPage(Page<Void> page, List<Long> attributeNameIdList) {
        IPage<String> pageResult = baseMapper.selectProduceDataAttrWeightPage(page, attributeNameIdList);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public List<ProduceDataAttrValueListVo> getProduceDataAttrValueListByValueList(ProduceDataAttrValueListDto dto) {
        return baseMapper.getProduceDataAttrValueListByValueList(dto);
    }

    public CommonPageResult.PageInfo<String> selectProduceDataAttrPage(Page<Void> page, List<Long> attributeNameIdList) {
        IPage<String> pageResult = baseMapper.selectProduceDataAttrPage(page, attributeNameIdList);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    public Map<String, List<ProduceDataAttrPo>> getMapBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataAttrPo>lambdaQuery()
                        .in(ProduceDataAttrPo::getSku, skuList))
                .stream()
                .collect(Collectors.groupingBy(ProduceDataAttrPo::getSku));
    }

    public CommonPageResult.PageInfo<ProduceDataAttrSkuVo> getPageBySkuList(Page<Void> page, ProduceDataAttrSkuDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getPageBySkuList(page, dto));
    }

    public IPage<String> getSkuByPage(Page<String> page, List<Long> attributeNameIdList) {
        return baseMapper.getSkuByPage(page, attributeNameIdList);
    }

    @Override
    public boolean removeBatch(Collection<ProduceDataAttrPo> entityList) {
        return super.removeBatch(entityList);
    }
}
