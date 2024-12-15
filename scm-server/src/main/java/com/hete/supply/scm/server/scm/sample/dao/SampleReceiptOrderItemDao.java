package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品收货单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-13
 */
@Component
@Validated
public class SampleReceiptOrderItemDao extends BaseDao<SampleReceiptOrderItemMapper, SampleReceiptOrderItemPo> {
    public List<SampleReceiptOrderItemPo> getListByReceiptNo(String sampleReceiptOrderNo) {
        if (StringUtils.isBlank(sampleReceiptOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleReceiptOrderItemPo>lambdaQuery()
                .eq(SampleReceiptOrderItemPo::getSampleReceiptOrderNo, sampleReceiptOrderNo));
    }

    public List<SampleReceiptOrderItemPo> getListBySampleChildOrderNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReceiptOrderItemPo>lambdaQuery()
                .eq(SampleReceiptOrderItemPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    public List<SampleReceiptOrderItemPo> getListBySpu(String spu) {
        if (StringUtils.isBlank(spu)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReceiptOrderItemPo>lambdaQuery()
                .eq(SampleReceiptOrderItemPo::getSpu, spu));
    }

    public List<SampleReceiptOrderItemPo> getListByChildOrderNoList(List<String> childOrderNoList) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReceiptOrderItemPo>lambdaQuery()
                .in(SampleReceiptOrderItemPo::getSampleChildOrderNo, childOrderNoList)
                .orderByDesc(SampleReceiptOrderItemPo::getCreateTime));
    }

    public SampleReceiptOrderItemPo getLatestDeliverPoByChildNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<SampleReceiptOrderItemPo>lambdaQuery()
                .eq(SampleReceiptOrderItemPo::getSampleChildOrderNo, sampleChildOrderNo)
                .orderByDesc(SampleReceiptOrderItemPo::getCreateTime)
                .last("limit 1"));
    }

    public List<SampleReceiptOrderItemPo> getListByParentNo(String sampleParentOrderNo) {
        if (StringUtils.isBlank(sampleParentOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReceiptOrderItemPo>lambdaQuery()
                .eq(SampleReceiptOrderItemPo::getSampleParentOrderNo, sampleParentOrderNo));
    }

    public List<SampleReceiptOrderItemPo> getListBySampleReceiptOrderNoList(List<String> sampleReceiptOrderNoList) {
        if (CollectionUtils.isEmpty(sampleReceiptOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleReceiptOrderItemPo>lambdaQuery()
                .in(SampleReceiptOrderItemPo::getSampleReceiptOrderNo, sampleReceiptOrderNoList)
                .orderByDesc(SampleReceiptOrderItemPo::getCreateTime));
    }
}
