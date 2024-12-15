package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderProcessPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 补款单加工明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class SupplementOrderProcessDao extends BaseDao<SupplementOrderProcessMapper, SupplementOrderProcessPo> {

    /**
     * 根据补款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/4 14:49
     */
    public List<SupplementOrderProcessPo> getBySupplementOrderId(Long supplementOrderId) {
        return list(Wrappers.<SupplementOrderProcessPo>lambdaQuery().eq(SupplementOrderProcessPo::getSupplementOrderId, supplementOrderId));
    }

    /**
     * 根据补款单ID删除
     *
     * @author ChenWenLong
     * @date 2022/11/9 18:08
     */
    public void deleteBySupplementOrderId(Long supplementOrderId) {
        baseMapper.delete(Wrappers.<SupplementOrderProcessPo>lambdaUpdate()
                .eq(SupplementOrderProcessPo::getSupplementOrderId, supplementOrderId));
    }

    /**
     * 根据单据号查询
     *
     * @author ChenWenLong
     * @date 2022/11/14 10:43
     */
    public List<SupplementOrderProcessPo> getByLikeProcessOrderNo(String processOrderNo) {
        return Optional.ofNullable(baseMapper.selectList(Wrappers.<SupplementOrderProcessPo>lambdaQuery()
                        .like(SupplementOrderProcessPo::getProcessOrderNo, processOrderNo)))
                .orElse(new ArrayList<>());
    }


    /**
     * 根据批量补款单ID查询列表
     *
     * @author ChenWenLong
     * @date 2023/3/28 10:34
     */
    public List<SupplementOrderProcessPo> getBatchBySupplementOrderId(List<Long> supplementOrderIdList) {
        if (CollectionUtils.isEmpty(supplementOrderIdList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<SupplementOrderProcessPo>lambdaQuery()
                .in(SupplementOrderProcessPo::getSupplementOrderId, supplementOrderIdList));
    }

}
