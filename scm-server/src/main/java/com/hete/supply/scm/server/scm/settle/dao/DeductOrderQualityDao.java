package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderQualityPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 扣款单品质扣款明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-14
 */
@Component
@Validated
public class DeductOrderQualityDao extends BaseDao<DeductOrderQualityMapper, DeductOrderQualityPo> {

    /**
     * 根据扣款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 14:49
     */
    public List<DeductOrderQualityPo> getByDeductOrderId(Long deductOrderId) {
        return list(Wrappers.<DeductOrderQualityPo>lambdaQuery().eq(DeductOrderQualityPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 根据扣款单ID删除
     *
     * @author ChenWenLong
     * @date 2022/11/9 18:08
     */
    public void deleteByDeductOrderId(Long deductOrderId) {
        baseMapper.delete(Wrappers.<DeductOrderQualityPo>lambdaUpdate()
                .eq(DeductOrderQualityPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 通过扣款单id批量查询
     *
     * @param deductOrderIdList:
     * @return List<DeductOrderQualityPo>
     * @author ChenWenLong
     * @date 2023/7/10 15:32
     */
    public List<DeductOrderQualityPo> getByBatchDeductOrderId(List<Long> deductOrderIdList) {
        if (CollectionUtils.isEmpty(deductOrderIdList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DeductOrderQualityPo>lambdaQuery()
                .in(DeductOrderQualityPo::getDeductOrderId, deductOrderIdList));
    }

    public List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(List<String> businessNoList, List<DeductStatus> statusList) {
        if (CollectionUtils.isEmpty(businessNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListByBusinessNoAndStatus(businessNoList, statusList);
    }
}
