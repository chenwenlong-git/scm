package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 生产信息表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Component
@Validated
public class ProduceDataDao extends BaseDao<ProduceDataMapper, ProduceDataPo> {

    public ProduceDataPo getBySku(String sku) {
        if (StringUtils.isBlank(sku)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<ProduceDataPo>lambdaQuery()
                .eq(ProduceDataPo::getSku, sku));
    }

    public List<ProduceDataPo> getListBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataPo>lambdaQuery()
                .in(ProduceDataPo::getSku, skuList));
    }

    public List<ProduceDataPo> getListBySpuList(List<String> spuList) {
        if (CollectionUtils.isEmpty(spuList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataPo>lambdaQuery()
                .in(ProduceDataPo::getSpu, spuList));
    }

    public List<ProduceDataPo> getListByBindingProduceData(BindingProduceData bindingProduceData) {
        if (bindingProduceData == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataPo>lambdaQuery()
                .in(ProduceDataPo::getBindingProduceData, bindingProduceData));
    }

    public List<ProduceDataPo> getAll() {
        return list(Wrappers.<ProduceDataPo>lambdaQuery());
    }

    public Long getTotalsCnt() {
        return baseMapper.selectCount(null);
    }

    public IPage<ProduceDataPo> getByPage(Page<ProduceDataPo> page) {
        return baseMapper.selectPage(page, null);
    }

    public Map<String, ProduceDataPo> getMapBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataPo>lambdaQuery()
                        .in(ProduceDataPo::getSku, skuList))
                .stream().collect(Collectors.toMap(ProduceDataPo::getSku, Function.identity()));
    }
}
