package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPurchasePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 补款单采购明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class SupplementOrderPurchaseDao extends BaseDao<SupplementOrderPurchaseMapper, SupplementOrderPurchasePo> {

    /**
     * 根据补款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 14:49
     */
    public List<SupplementOrderPurchasePo> getBySupplementOrderId(Long supplementOrderId) {
        return list(Wrappers.<SupplementOrderPurchasePo>lambdaQuery().eq(SupplementOrderPurchasePo::getSupplementOrderId, supplementOrderId));
    }

    /**
     * 根据补款单ID删除
     *
     * @author ChenWenLong
     * @date 2022/11/9 18:08
     */
    public void deleteBySupplementOrderId(Long supplementOrderId) {
        baseMapper.delete(Wrappers.<SupplementOrderPurchasePo>lambdaUpdate()
                .eq(SupplementOrderPurchasePo::getSupplementOrderId, supplementOrderId));
    }

    /**
     * 根据单据号查询
     *
     * @author ChenWenLong
     * @date 2022/11/14 10:43
     */
    public List<SupplementOrderPurchasePo> getByLikeBusinessNo(String businessNo) {
        return Optional.ofNullable(baseMapper.selectList(Wrappers.<SupplementOrderPurchasePo>lambdaQuery()
                        .like(SupplementOrderPurchasePo::getBusinessNo, businessNo)))
                .orElse(new ArrayList<>());
    }

    /**
     * 根据批量补款单ID查询列表
     *
     * @param supplementOrderIdList:
     * @return List<SupplementOrderPurchasePo>
     * @author ChenWenLong
     * @date 2023/7/6 13:44
     */
    public List<SupplementOrderPurchasePo> getByBatchSupplementOrderId(List<Long> supplementOrderIdList) {
        if (CollectionUtils.isEmpty(supplementOrderIdList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplementOrderPurchasePo>lambdaQuery()
                .in(SupplementOrderPurchasePo::getSupplementOrderId, supplementOrderIdList));
    }

    public List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(List<String> businessNoList, List<SupplementStatus> statusList) {
        return baseMapper.getListByBusinessNoAndStatus(businessNoList, statusList);
    }
}
