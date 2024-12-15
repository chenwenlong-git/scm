package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderChangePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 样品需求子单变更记录 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Component
@Validated
public class SampleChildOrderChangeDao extends BaseDao<SampleChildOrderChangeMapper, SampleChildOrderChangePo> {

    public SampleChildOrderChangePo getByChildOrderId(@NotNull Long sampleChildOrderId) {
        return baseMapper.selectOne(Wrappers.<SampleChildOrderChangePo>lambdaQuery()
                .eq(SampleChildOrderChangePo::getSampleChildOrderId, sampleChildOrderId));
    }

    public List<SampleChildOrderChangePo> getByChildOrderIdList(Set<Long> childOrderIdList) {
        if (CollectionUtils.isEmpty(childOrderIdList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleChildOrderChangePo>lambdaQuery()
                .in(SampleChildOrderChangePo::getSampleChildOrderId, childOrderIdList));
    }

    /**
     * 通过选样时间查询列表
     *
     * @author ChenWenLong
     * @date 2023/4/14 16:37
     */
    public List<SampleChildOrderChangePo> getBySampleTime(LocalDateTime sampleTimeStart, LocalDateTime sampleTimeEnd) {
        return baseMapper.selectList(Wrappers.<SampleChildOrderChangePo>lambdaQuery()
                .ge(sampleTimeStart != null, SampleChildOrderChangePo::getSampleTime, sampleTimeStart)
                .le(sampleTimeEnd != null, SampleChildOrderChangePo::getSampleTime, sampleTimeEnd));
    }
}
