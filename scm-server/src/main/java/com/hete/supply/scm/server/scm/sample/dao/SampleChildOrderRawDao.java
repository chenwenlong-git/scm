package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.SampleRawBizType;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderRawPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品需求子单原料 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Component
@Validated
public class SampleChildOrderRawDao extends BaseDao<SampleChildOrderRawMapper, SampleChildOrderRawPo> {

    public void removeByNoAndType(String sampleChildOrderNo, SampleRawBizType sampleRawBizType) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return;
        }

        baseMapper.deleteSkipCheck(Wrappers.<SampleChildOrderRawPo>lambdaUpdate()
                .eq(SampleChildOrderRawPo::getSampleChildOrderNo, sampleChildOrderNo)
                .eq(SampleChildOrderRawPo::getSampleRawBizType, sampleRawBizType));
    }

    public List<SampleChildOrderRawPo> getListByChildNoAndType(String sampleChildOrderNo,
                                                               SampleRawBizType sampleRawBizType) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }


        return baseMapper.selectList(Wrappers.<SampleChildOrderRawPo>lambdaQuery()
                .eq(SampleChildOrderRawPo::getSampleChildOrderNo, sampleChildOrderNo)
                .eq(SampleChildOrderRawPo::getSampleRawBizType, sampleRawBizType));

    }

    public List<SampleChildOrderRawPo> getListByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderRawPo>lambdaQuery()
                .in(SampleChildOrderRawPo::getSampleChildOrderRawId, idList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public void removeBySampleChildOrderNo(@NotBlank String sampleChildOrderNo, List<SampleRawBizType> sampleRawBizTypeList) {
        baseMapper.deleteSkipCheck(Wrappers.<SampleChildOrderRawPo>lambdaUpdate()
                .eq(SampleChildOrderRawPo::getSampleChildOrderNo, sampleChildOrderNo)
                .in(CollectionUtils.isNotEmpty(sampleRawBizTypeList), SampleChildOrderRawPo::getSampleRawBizType, sampleRawBizTypeList));
    }

    public List<SampleChildOrderRawPo> getListByChildNoListAndType(List<String> sampleChildOrderNoList, SampleRawBizType sampleRawBizType) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderRawPo>lambdaQuery()
                .in(SampleChildOrderRawPo::getSampleChildOrderNo, sampleChildOrderNoList)
                .eq(SampleChildOrderRawPo::getSampleRawBizType, sampleRawBizType));
    }
}
