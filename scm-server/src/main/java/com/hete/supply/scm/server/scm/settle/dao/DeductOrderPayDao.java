package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPayPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 扣款单预付款明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-23
 */
@Component
@Validated
public class DeductOrderPayDao extends BaseDao<DeductOrderPayMapper, DeductOrderPayPo> {

    /**
     * 根据扣款单ID删除
     *
     * @author ChenWenLong
     * @date 2023/2/24 11:28
     */
    public void deleteByDeductOrderId(Long deductOrderId) {
        baseMapper.delete(Wrappers.<DeductOrderPayPo>lambdaUpdate()
                .eq(DeductOrderPayPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 根据扣款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2023/2/24 11:42
     */
    public List<DeductOrderPayPo> getByDeductOrderId(Long deductOrderId) {
        return list(Wrappers.<DeductOrderPayPo>lambdaQuery().eq(DeductOrderPayPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 通过编号批量查询列表预付款类型详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 15:37
     */
    public List<DeductOrderPayPo> getPayBatchDeductOrderNo(List<String> deductOrderNoList) {
        if (CollectionUtils.isEmpty(deductOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DeductOrderPayPo>lambdaQuery()
                .in(DeductOrderPayPo::getDeductOrderNo, deductOrderNoList));
    }

    /**
     * 通过扣款单id批量查询
     *
     * @param deductOrderIdList:
     * @return List<DeductOrderPayPo>
     * @author ChenWenLong
     * @date 2023/7/10 15:32
     */
    public List<DeductOrderPayPo> getByBatchDeductOrderId(List<Long> deductOrderIdList) {
        if (CollectionUtils.isEmpty(deductOrderIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DeductOrderPayPo>lambdaQuery()
                .in(DeductOrderPayPo::getDeductOrderId, deductOrderIdList));
    }
}
