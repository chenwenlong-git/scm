package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderInfoPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 样品采购子单详细信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-12-02
 */
@Component
@Validated
public class SampleChildOrderInfoDao extends BaseDao<SampleChildOrderInfoMapper, SampleChildOrderInfoPo> {

    public List<SampleChildOrderInfoPo> getInfoListByChildNo(@NotBlank String sampleChildOrderNo) {
        return baseMapper.selectList(Wrappers.<SampleChildOrderInfoPo>lambdaQuery()
                .eq(SampleChildOrderInfoPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    public List<SampleChildOrderInfoPo> getBySampleInfoValueList(List<String> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderInfoPo>lambdaQuery()
                .in(SampleChildOrderInfoPo::getSampleInfoValue, properties));
    }

    public List<SampleChildOrderInfoPo> getListByChildNoList(Set<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderInfoPo>lambdaQuery()
                .in(SampleChildOrderInfoPo::getSampleChildOrderNo, sampleChildOrderNoList));
    }

    public Map<String, List<SampleChildOrderInfoPo>> getMapByChildNoList(List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyMap();
        }


        return baseMapper.selectList(Wrappers.<SampleChildOrderInfoPo>lambdaQuery()
                        .in(SampleChildOrderInfoPo::getSampleChildOrderNo, sampleChildOrderNoList))
                .stream()
                .collect(Collectors.groupingBy(SampleChildOrderInfoPo::getSampleChildOrderNo));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SampleChildOrderInfoPo> getInfoListByChildNoList(List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }


        return baseMapper.selectList(Wrappers.<SampleChildOrderInfoPo>lambdaQuery()
                .in(SampleChildOrderInfoPo::getSampleChildOrderNo, sampleChildOrderNoList));
    }
}
