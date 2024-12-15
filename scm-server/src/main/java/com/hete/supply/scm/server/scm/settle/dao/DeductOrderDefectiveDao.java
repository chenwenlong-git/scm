package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderDefectivePo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 扣款单表次品退供明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-06-21
 */
@Component
@Validated
public class DeductOrderDefectiveDao extends BaseDao<DeductOrderDefectiveMapper, DeductOrderDefectivePo> {


    /**
     * 通过扣款单ID查询列表
     *
     * @param deductOrderId:
     * @return List<DeductOrderDefectivePo>
     * @author ChenWenLong
     * @date 2023/6/25 15:43
     */
    public List<DeductOrderDefectivePo> getByDeductOrderId(Long deductOrderId) {
        return list(Wrappers.<DeductOrderDefectivePo>lambdaQuery().eq(DeductOrderDefectivePo::getDeductOrderId, deductOrderId));
    }

    /**
     * 通过扣款单ID批量查询列表
     *
     * @param deductOrderIdList:
     * @return List<DeductOrderDefectivePo>
     * @author ChenWenLong
     * @date 2023/7/6 09:51
     */
    public List<DeductOrderDefectivePo> getByBatchDeductOrderId(List<Long> deductOrderIdList) {
        if (CollectionUtils.isEmpty(deductOrderIdList)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<DeductOrderDefectivePo>lambdaQuery().in(DeductOrderDefectivePo::getDeductOrderId, deductOrderIdList));
    }

    public List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(List<String> businessNoList, List<DeductStatus> statusList) {
        if (CollectionUtils.isEmpty(businessNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.getListByBusinessNoAndStatus(businessNoList, statusList);
    }
}
