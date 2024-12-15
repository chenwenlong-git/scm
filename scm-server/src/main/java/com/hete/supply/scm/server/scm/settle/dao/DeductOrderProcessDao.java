package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderProcessPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 扣款单加工明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-05
 */
@Component
@Validated
public class DeductOrderProcessDao extends BaseDao<DeductOrderProcessMapper, DeductOrderProcessPo> {

    /**
     * 根据扣款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 14:49
     */
    public List<DeductOrderProcessPo> getByDeductOrderId(Long deductOrderId) {
        return list(Wrappers.<DeductOrderProcessPo>lambdaQuery().eq(DeductOrderProcessPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 根据扣款单ID删除
     *
     * @author ChenWenLong
     * @date 2022/11/9 18:08
     */
    public void deleteByDeductOrderId(Long deductOrderId) {
        baseMapper.delete(Wrappers.<DeductOrderProcessPo>lambdaUpdate()
                .eq(DeductOrderProcessPo::getDeductOrderId, deductOrderId));
    }

    /**
     * 根据单据号查询
     *
     * @author ChenWenLong
     * @date 2022/11/14 10:43
     */
    public List<DeductOrderProcessPo> getByLikeProcessOrderNo(String processOrderNo) {
        return Optional.ofNullable(baseMapper.selectList(Wrappers.<DeductOrderProcessPo>lambdaQuery()
                        .like(DeductOrderProcessPo::getProcessOrderNo, processOrderNo)))
                .orElse(new ArrayList<>());
    }


    /**
     * 根据批量补款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2023/3/28 10:34
     */
    public List<DeductOrderProcessPo> getBatchByDeductOrderId(List<Long> deductOrderIdList) {
        if (CollectionUtils.isEmpty(deductOrderIdList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DeductOrderProcessPo>lambdaQuery()
                .in(DeductOrderProcessPo::getDeductOrderId, deductOrderIdList));
    }
}
