package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderScanPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 加工结算单明细工序扫码记录 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-15
 */
@Component
@Validated
public class ProcessSettleOrderScanDao extends BaseDao<ProcessSettleOrderScanMapper, ProcessSettleOrderScanPo> {


    /**
     * 根据加工结算单明细ID获取列表
     *
     * @author ChenWenLong
     * @date 2022/11/15 19:41
     */
    public List<ProcessSettleOrderScanPo> getByProcessSettleOrderItemId(Long processSettleOrderItemId) {
        return list(Wrappers.<ProcessSettleOrderScanPo>lambdaQuery().eq(ProcessSettleOrderScanPo::getProcessSettleOrderItemId, processSettleOrderItemId));
    }


    /**
     * 根据加工结算单明细ID获取列表
     *
     * @author ChenWenLong
     * @date 2022/11/15 19:41
     */
    public Map<String, List<ProcessSettleOrderScanPo>> getMapByProcessSettleOrderItemId(List<Long> processSettleOrderItemIdList) {
        if (CollectionUtils.isEmpty(processSettleOrderItemIdList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<ProcessSettleOrderScanPo>lambdaQuery()
                .in(ProcessSettleOrderScanPo::getProcessSettleOrderItemId, processSettleOrderItemIdList))
                .stream()
                .collect(Collectors.groupingBy(ProcessSettleOrderScanPo::getCompleteUser));
    }
}
