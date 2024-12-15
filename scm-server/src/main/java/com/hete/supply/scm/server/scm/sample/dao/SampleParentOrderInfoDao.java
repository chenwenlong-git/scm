package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderInfoPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品需求母单详细信息 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class SampleParentOrderInfoDao extends BaseDao<SampleParentOrderInfoMapper, SampleParentOrderInfoPo> {

    public List<SampleParentOrderInfoPo> getInfoListByParentNo(@NotBlank String sampleParentOrderNo) {
        return baseMapper.selectList(Wrappers.<SampleParentOrderInfoPo>lambdaQuery()
                .eq(SampleParentOrderInfoPo::getSampleParentOrderNo, sampleParentOrderNo));
    }

    public List<SampleParentOrderInfoPo> getBySampleInfoValueList(List<String> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SampleParentOrderInfoPo>lambdaQuery()
                .in(SampleParentOrderInfoPo::getSampleInfoValue, properties));
    }

    public void deleteAllParentInfo(@NotBlank String sampleParentOrderNo) {
        baseMapper.deleteSkipCheck(Wrappers.<SampleParentOrderInfoPo>lambdaUpdate()
                .eq(SampleParentOrderInfoPo::getSampleParentOrderNo, sampleParentOrderNo));
    }

    public List<SampleParentOrderInfoPo> getInfoListByParentNoList(List<String> sampleParentOrderNoList) {
        if (CollectionUtils.isEmpty(sampleParentOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleParentOrderInfoPo>lambdaQuery()
                .in(SampleParentOrderInfoPo::getSampleParentOrderNo, sampleParentOrderNoList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
