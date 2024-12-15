package com.hete.supply.scm.server.scm.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataSpecSupplierPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生产信息产品规格书关联供应商 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-03-07
 */
@Component
@Validated
public class ProduceDataSpecSupplierDao extends BaseDao<ProduceDataSpecSupplierMapper, ProduceDataSpecSupplierPo> {

    public List<ProduceDataSpecSupplierPo> getByProduceDataSpecIdList(List<Long> produceDataSpecIdList) {
        if (CollectionUtils.isEmpty(produceDataSpecIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<ProduceDataSpecSupplierPo>lambdaUpdate()
                .in(ProduceDataSpecSupplierPo::getProduceDataSpecId, produceDataSpecIdList));
    }

    public void removeByProduceDataSpecIdList(List<Long> produceDataSpecIdList) {
        if (CollectionUtils.isEmpty(produceDataSpecIdList)) {
            return;
        }
        baseMapper.delete(Wrappers.<ProduceDataSpecSupplierPo>lambdaUpdate()
                .in(ProduceDataSpecSupplierPo::getProduceDataSpecId, produceDataSpecIdList));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return super.removeBatchByIds(list);
    }

}
