package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderProcessDescPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品需求子单工序描述 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Component
@Validated
public class SampleChildOrderProcessDescDao extends BaseDao<SampleChildOrderProcessDescMapper, SampleChildOrderProcessDescPo> {

    public List<SampleChildOrderProcessDescPo> getListByNo(String sampleChildOrderNo) {
        if (StringUtils.isBlank(sampleChildOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderProcessDescPo>lambdaQuery()
                .eq(SampleChildOrderProcessDescPo::getSampleChildOrderNo, sampleChildOrderNo));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SampleChildOrderProcessDescPo> getListByNoList(List<String> sampleChildOrderNoList) {
        if (CollectionUtils.isEmpty(sampleChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderProcessDescPo>lambdaQuery()
                .in(SampleChildOrderProcessDescPo::getSampleChildOrderNo, sampleChildOrderNoList));
    }
}
