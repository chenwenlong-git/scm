package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemInspectPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务对账单明细校验表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class FinanceRecoOrderItemInspectDao extends BaseDao<FinanceRecoOrderItemInspectMapper, FinanceRecoOrderItemInspectPo> {

    public List<FinanceRecoOrderItemInspectPo> getListByRecoOrderItemSkuIdList(List<Long> financeRecoOrderItemSkuIdList) {
        if (CollectionUtils.isEmpty(financeRecoOrderItemSkuIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderItemInspectPo>lambdaQuery()
                .in(FinanceRecoOrderItemInspectPo::getFinanceRecoOrderItemSkuId, financeRecoOrderItemSkuIdList));
    }

    public List<FinanceRecoOrderItemInspectPo> getListByRecoOrderItemSkuId(Long financeRecoOrderItemSkuId) {

        if (null == financeRecoOrderItemSkuId) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderItemInspectPo>lambdaQuery()
                .eq(FinanceRecoOrderItemInspectPo::getFinanceRecoOrderItemSkuId, financeRecoOrderItemSkuId));

    }
}
