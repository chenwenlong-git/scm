package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderChangePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品需求母单变更记录 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-02
 */
@Component
@Validated
public class SampleParentOrderChangeDao extends BaseDao<SampleParentOrderChangeMapper, SampleParentOrderChangePo> {

    public SampleParentOrderChangePo getByParentId(@NotNull Long sampleParentOrderId) {
        return baseMapper.selectOne(Wrappers.<SampleParentOrderChangePo>lambdaQuery()
                .eq(SampleParentOrderChangePo::getSampleParentOrderId, sampleParentOrderId));
    }

    public List<SampleParentOrderChangePo> getByParentIdList(List<Long> parentIdList) {
        if (CollectionUtils.isEmpty(parentIdList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleParentOrderChangePo>lambdaQuery()
                .in(SampleParentOrderChangePo::getSampleParentOrderId, parentIdList));
    }
}
