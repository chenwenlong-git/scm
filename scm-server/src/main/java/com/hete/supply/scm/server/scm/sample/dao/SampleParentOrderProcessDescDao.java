package com.hete.supply.scm.server.scm.sample.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderProcessDescPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 样品需求母单工序描述 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-28
 */
@Component
@Validated
public class SampleParentOrderProcessDescDao extends BaseDao<SampleParentOrderProcessDescMapper, SampleParentOrderProcessDescPo> {

    public List<SampleParentOrderProcessDescPo> getListByNo(String sampleParentOrderNo) {
        if (StringUtils.isBlank(sampleParentOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<SampleParentOrderProcessDescPo>lambdaQuery()
                .eq(SampleParentOrderProcessDescPo::getSampleParentOrderNo, sampleParentOrderNo));
    }
}
