package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderProcessPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品需求子单工序 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Component
@Validated
public class SampleChildOrderProcessDao extends BaseDao<SampleChildOrderProcessMapper, SampleChildOrderProcessPo> {

    public List<SampleChildOrderProcessPo> getListByNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderProcessPo>lambdaQuery()
                .eq(SampleChildOrderProcessPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    public void removeByNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return;
        }
        baseMapper.deleteSkipCheck(Wrappers.<SampleChildOrderProcessPo>lambdaUpdate().
                eq(SampleChildOrderProcessPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SampleChildOrderProcessPo> getListByNoList(List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleChildOrderProcessPo>lambdaQuery()
                .in(SampleChildOrderProcessPo::getSampleChildOrderNo, sampleChildOrderNoList).
                orderByAsc(SampleChildOrderProcessPo::getSampleChildOrderProcessId));
    }
}
