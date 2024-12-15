package com.hete.supply.scm.server.scm.ibfs.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderItemSkuPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 财务对账单明细sku详情表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Component
@Validated
public class FinanceRecoOrderItemSkuDao extends BaseDao<FinanceRecoOrderItemSkuMapper, FinanceRecoOrderItemSkuPo> {

    public List<FinanceRecoOrderItemSkuPo> getListByFinanceRecoOrderNo(String financeRecoOrderNo) {
        if (StringUtils.isBlank(financeRecoOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderItemSkuPo>lambdaQuery()
                .in(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderNo, financeRecoOrderNo));

    }

    public List<FinanceRecoOrderItemSkuPo> getListByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<FinanceRecoOrderItemSkuPo>lambdaQuery()
                .in(FinanceRecoOrderItemSkuPo::getFinanceRecoOrderItemSkuId, idList));

    }

    public List<FinanceRecoOrderItemSkuPo> getListByCollectOrderNoAndNotStatus(List<String> collectOrderNoList,
                                                                               List<FinanceRecoOrderStatus> financeRecoOrderStatusNotList) {
        if (CollectionUtils.isEmpty(collectOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListByCollectOrderNoAndNotStatus(collectOrderNoList, financeRecoOrderStatusNotList);
    }
}
