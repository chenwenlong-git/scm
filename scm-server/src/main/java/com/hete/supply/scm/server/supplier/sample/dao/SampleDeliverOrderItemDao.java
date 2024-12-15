package com.hete.supply.scm.server.supplier.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 样品发货单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class SampleDeliverOrderItemDao extends BaseDao<SampleDeliverOrderItemMapper, SampleDeliverOrderItemPo> {

    public List<SampleDeliverOrderItemPo> getListByDeliverOrderNo(String sampleDeliverOrderNo) {
        if (StringUtils.isBlank(sampleDeliverOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .eq(SampleDeliverOrderItemPo::getSampleDeliverOrderNo, sampleDeliverOrderNo));
    }

    public List<SampleDeliverOrderItemPo> getListByChildOrderNoList(List<String> childOrderNoList) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .in(SampleDeliverOrderItemPo::getSampleChildOrderNo, childOrderNoList));
    }

    public List<SampleDeliverOrderItemPo> getListByParentNo(String sampleParentOrderNo) {
        if (StringUtils.isBlank(sampleParentOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .eq(SampleDeliverOrderItemPo::getSampleParentOrderNo, sampleParentOrderNo));

    }

    public List<SampleDeliverOrderItemPo> getListByChildNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .eq(SampleDeliverOrderItemPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    public Map<String, List<SampleDeliverOrderItemPo>> getMapByDeliverOrderNoList(List<String> sampleDeliverOrderNoList) {
        if (CollectionUtils.isEmpty(sampleDeliverOrderNoList)) {
            return Collections.emptyMap();
        }

        final List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .in(SampleDeliverOrderItemPo::getSampleDeliverOrderNo, sampleDeliverOrderNoList));

        return sampleDeliverOrderItemPoList.stream()
                .collect(Collectors.groupingBy(SampleDeliverOrderItemPo::getSampleDeliverOrderNo));
    }

    public List<SampleDeliverOrderItemPo> getListByDeliverOrderNoList(List<String> deliverOrderNoList) {
        if (CollectionUtils.isEmpty(deliverOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .in(SampleDeliverOrderItemPo::getSampleDeliverOrderNo, deliverOrderNoList));
    }

    public List<SampleDeliverOrderItemPo> getListByLikeParentNo(String sampleParentOrderNo) {
        if (StringUtils.isBlank(sampleParentOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .like(SampleDeliverOrderItemPo::getSampleParentOrderNo, sampleParentOrderNo));

    }

    public List<SampleDeliverOrderItemPo> getListByLikeChildNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleDeliverOrderItemPo>lambdaQuery()
                .like(SampleDeliverOrderItemPo::getSampleChildOrderNo, sampleChildOrderNo));
    }
}
