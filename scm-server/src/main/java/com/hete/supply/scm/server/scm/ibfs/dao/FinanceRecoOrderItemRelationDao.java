package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemRelationPo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.RecoOrderItemRelationCheckVo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务对账单明细SKU关联使用单据表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Component
@Validated
public class FinanceRecoOrderItemRelationDao extends BaseDao<FinanceRecoOrderItemRelationMapper, FinanceRecoOrderItemRelationPo> {

    public List<FinanceRecoOrderItemRelationPo> getListByRecoOrderItemSkuIdList(List<Long> financeRecoOrderItemSkuIdList) {
        if (CollectionUtils.isEmpty(financeRecoOrderItemSkuIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderItemRelationPo>lambdaQuery()
                .in(FinanceRecoOrderItemRelationPo::getFinanceRecoOrderItemSkuId, financeRecoOrderItemSkuIdList));
    }

    public List<RecoOrderItemRelationCheckVo> getListByBusinessIdAndNotStatus(List<Long> businessIdList,
                                                                              List<FinanceRecoOrderStatus> financeRecoOrderStatusNotList) {
        if (CollectionUtils.isEmpty(businessIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListByBusinessIdAndNotStatus(businessIdList, financeRecoOrderStatusNotList);
    }

    public List<FinanceRecoOrderItemRelationPo> getListByRecoOrderItemSkuId(Long financeRecoOrderItemSkuId) {
        if (null == financeRecoOrderItemSkuId) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderItemRelationPo>lambdaQuery()
                .eq(FinanceRecoOrderItemRelationPo::getFinanceRecoOrderItemSkuId, financeRecoOrderItemSkuId));
    }
}
