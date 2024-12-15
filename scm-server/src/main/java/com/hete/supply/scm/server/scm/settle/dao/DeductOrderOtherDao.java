package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderOtherPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 扣款单其他单据明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-23
 */
@Component
@Validated
public class DeductOrderOtherDao extends BaseDao<DeductOrderOtherMapper, DeductOrderOtherPo> {

    /**
     * 根据扣款单ID删除
     *
     * @author ChenWenLong
     * @date 2023/2/24 11:28
     */
    public void deleteByDeductOrderId(Long deductOrderId) {
        baseMapper.delete(Wrappers.<DeductOrderOtherPo>lambdaUpdate()
                .eq(DeductOrderOtherPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 根据扣款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2023/2/24 11:42
     */
    public List<DeductOrderOtherPo> getByDeductOrderId(Long deductOrderId) {
        return list(Wrappers.<DeductOrderOtherPo>lambdaQuery().eq(DeductOrderOtherPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 通过编号批量查询列表其他类型详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 15:37
     */
    public List<DeductOrderOtherPo> getOtherBatchDeductOrderNo(List<String> deductOrderNoList) {
        if (CollectionUtils.isEmpty(deductOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DeductOrderOtherPo>lambdaQuery()
                .in(DeductOrderOtherPo::getDeductOrderNo, deductOrderNoList));
    }

    /**
     * 通过扣款单ID批量查询
     *
     * @param deductOrderIdList:
     * @return List<DeductOrderOtherPo>
     * @author ChenWenLong
     * @date 2023/7/10 15:31
     */
    public List<DeductOrderOtherPo> getByBatchDeductOrderId(List<Long> deductOrderIdList) {
        if (CollectionUtils.isEmpty(deductOrderIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<DeductOrderOtherPo>lambdaQuery()
                .in(DeductOrderOtherPo::getDeductOrderId, deductOrderIdList));
    }
}
