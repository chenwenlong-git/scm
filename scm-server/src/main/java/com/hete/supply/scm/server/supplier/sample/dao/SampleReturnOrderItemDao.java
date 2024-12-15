package com.hete.supply.scm.server.supplier.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品退货单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class SampleReturnOrderItemDao extends BaseDao<SampleReturnOrderItemMapper, SampleReturnOrderItemPo> {

    public List<SampleReturnOrderItemPo> getListByChildOrderNo(String sampleChildOrderNo) {
        return baseMapper.selectList(Wrappers.<SampleReturnOrderItemPo>lambdaQuery()
                .eq(SampleReturnOrderItemPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    public List<SampleReturnOrderItemPo> getListByReturnOrderNo(String sampleReturnOrderNo) {
        return baseMapper.selectList(Wrappers.<SampleReturnOrderItemPo>lambdaQuery()
                .eq(SampleReturnOrderItemPo::getSampleReturnOrderNo, sampleReturnOrderNo));
    }

    public List<SampleReturnOrderItemPo> getListByChildOrderNoList(List<String> childOrderNoList) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleReturnOrderItemPo>lambdaQuery()
                .in(SampleReturnOrderItemPo::getSampleChildOrderNo, childOrderNoList));
    }

    /**
     * 通过样品编号模糊查询
     *
     * @param sampleChildOrderNo:
     * @return List<SampleReturnOrderItemPo>
     * @author ChenWenLong
     * @date 2023/7/13 14:44
     */
    public List<SampleReturnOrderItemPo> getListLikeChildOrderNo(String sampleChildOrderNo) {
        return baseMapper.selectList(Wrappers.<SampleReturnOrderItemPo>lambdaQuery()
                .like(SampleReturnOrderItemPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    public List<SampleReturnOrderItemPo> getListBySampleReturnOrderNoList(List<String> sampleReturnOrderNoList) {
        if (CollectionUtils.isEmpty(sampleReturnOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleReturnOrderItemPo>lambdaQuery()
                .in(SampleReturnOrderItemPo::getSampleReturnOrderNo, sampleReturnOrderNoList));
    }
}
