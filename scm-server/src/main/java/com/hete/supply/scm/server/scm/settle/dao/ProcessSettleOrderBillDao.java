package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderBillPo;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 加工结算单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-15
 */
@Component
@Validated
public class ProcessSettleOrderBillDao extends BaseDao<ProcessSettleOrderBillMapper, ProcessSettleOrderBillPo> {

    /**
     * 根据加工结算单明细ID获取列表
     *
     * @author ChenWenLong
     * @date 2022/11/15 19:41
     */
    public List<ProcessSettleOrderBillPo> getByProcessSettleOrderItemId(Long processSettleOrderItemId, ProcessSettleOrderBillType processSettleOrderBillType) {
        return list(Wrappers.<ProcessSettleOrderBillPo>lambdaQuery()
                .eq(ProcessSettleOrderBillPo::getProcessSettleOrderItemId, processSettleOrderItemId)
                .eq(ProcessSettleOrderBillPo::getProcessSettleOrderBillType, processSettleOrderBillType));
    }

    public List<ProcessSettleOrderBillPo> getByProcessSettleOrderItemIds(Collection<Long> processSettleOrderItemIds, ProcessSettleOrderBillType processSettleOrderBillType) {
        return list(Wrappers.<ProcessSettleOrderBillPo>lambdaQuery()
                .in(ProcessSettleOrderBillPo::getProcessSettleOrderItemId, processSettleOrderItemIds)
                .eq(ProcessSettleOrderBillPo::getProcessSettleOrderBillType, processSettleOrderBillType));
    }

    /**
     * 根据加工结算单明细ID获取列表
     *
     * @author ChenWenLong
     * @date 2022/11/15 19:41
     */
    public List<ProcessSettleOrderBillPo> getBatchProcessSettleOrderItemId(List<Long> processSettleOrderItemIdList, ProcessSettleOrderBillType processSettleOrderBillType) {
        if (CollectionUtils.isEmpty(processSettleOrderItemIdList)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessSettleOrderBillPo>lambdaQuery()
                .in(ProcessSettleOrderBillPo::getProcessSettleOrderItemId, processSettleOrderItemIdList)
                .eq(ProcessSettleOrderBillPo::getProcessSettleOrderBillType, processSettleOrderBillType));
    }

    /**
     * 通过关联单据查询
     *
     * @param businessNos
     * @return
     */
    public List<ProcessSettleOrderBillPo> getByBussinessNos(List<String> businessNos) {
        if (CollectionUtils.isEmpty(businessNos)) {
            return new ArrayList<>();
        }
        return list(Wrappers.<ProcessSettleOrderBillPo>lambdaQuery()
                .in(ProcessSettleOrderBillPo::getBusinessNo, businessNos));
    }

    /**
     * 通过关联单据查询
     *
     * @param businessNo
     * @return
     */
    public List<ProcessSettleOrderBillPo> getByBussinessNo(String businessNo) {
        return list(Wrappers.<ProcessSettleOrderBillPo>lambdaQuery()
                .eq(ProcessSettleOrderBillPo::getBusinessNo, businessNo));
    }

}
