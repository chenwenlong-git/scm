package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderOtherPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 补款单其他单据明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-02-23
 */
@Component
@Validated
public class SupplementOrderOtherDao extends BaseDao<SupplementOrderOtherMapper, SupplementOrderOtherPo> {

    /**
     * 根据补款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2023/2/24 10:02
     */
    public List<SupplementOrderOtherPo> getBySupplementOrderId(Long supplementOrderId) {
        return list(Wrappers.<SupplementOrderOtherPo>lambdaQuery().eq(SupplementOrderOtherPo::getSupplementOrderId, supplementOrderId));
    }

    /**
     * 根据补款单ID删除
     *
     * @author ChenWenLong
     * @date 2023/2/24 10:11
     */
    public void deleteBySupplementOrderId(Long supplementOrderId) {
        baseMapper.delete(Wrappers.<SupplementOrderOtherPo>lambdaUpdate()
                .eq(SupplementOrderOtherPo::getSupplementOrderId, supplementOrderId));
    }

    /**
     * 通过编号批量查询列表其他类型详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 15:37
     */
    public List<SupplementOrderOtherPo> getOtherBatchSupplementOrderNo(List<String> supplementOrderNoList) {
        if (CollectionUtils.isEmpty(supplementOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplementOrderOtherPo>lambdaQuery()
                .in(SupplementOrderOtherPo::getSupplementOrderNo, supplementOrderNoList));
    }

    /**
     * 通过结算id批量查询列表其他类型详情
     *
     * @param supplementOrderIdList:
     * @return List<SupplementOrderOtherPo>
     * @author ChenWenLong
     * @date 2023/7/6 11:58
     */
    public List<SupplementOrderOtherPo> getByBatchSupplementOrderId(List<Long> supplementOrderIdList) {
        if (CollectionUtils.isEmpty(supplementOrderIdList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<SupplementOrderOtherPo>lambdaQuery()
                .in(SupplementOrderOtherPo::getSupplementOrderId, supplementOrderIdList));
    }
}
