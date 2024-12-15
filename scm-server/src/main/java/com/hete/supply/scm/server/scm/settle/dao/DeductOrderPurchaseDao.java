package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPurchasePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 扣款单采购明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-05
 */
@Component
@Validated
public class DeductOrderPurchaseDao extends BaseDao<DeductOrderPurchaseMapper, DeductOrderPurchasePo> {

    /**
     * 根据扣款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 14:49
     */
    public List<DeductOrderPurchasePo> getByDeductOrderId(Long deductOrderId) {
        return list(Wrappers.<DeductOrderPurchasePo>lambdaQuery().eq(DeductOrderPurchasePo::getDeductOrderId, deductOrderId));
    }

    /**
     * 根据扣款单ID删除
     *
     * @author ChenWenLong
     * @date 2022/11/9 18:08
     */
    public void deleteByDeductOrderId(Long deductOrderId) {
        baseMapper.delete(Wrappers.<DeductOrderPurchasePo>lambdaUpdate()
                .eq(DeductOrderPurchasePo::getDeductOrderId, deductOrderId));
    }

    /**
     * 根据单据号查询
     *
     * @author ChenWenLong
     * @date 2022/11/14 10:43
     */
    public List<DeductOrderPurchasePo> getByLikeBusinessNo(String businessNo) {
        return Optional.ofNullable(baseMapper.selectList(Wrappers.<DeductOrderPurchasePo>lambdaQuery()
                        .like(DeductOrderPurchasePo::getBusinessNo, businessNo)))
                .orElse(new ArrayList<>());
    }

    /**
     * 通过扣款单id查询
     *
     * @param deductOrderIdList:
     * @return List<DeductOrderPurchasePo>
     * @author ChenWenLong
     * @date 2023/7/6 09:59
     */
    public List<DeductOrderPurchasePo> getByBatchDeductOrderId(List<Long> deductOrderIdList) {
        if (CollectionUtils.isEmpty(deductOrderIdList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DeductOrderPurchasePo>lambdaQuery()
                .in(DeductOrderPurchasePo::getDeductOrderId, deductOrderIdList));
    }

    public List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(List<String> businessNoList, List<DeductStatus> statusList) {
        if (CollectionUtils.isEmpty(businessNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListByBusinessNoAndStatus(businessNoList, statusList);
    }
}
