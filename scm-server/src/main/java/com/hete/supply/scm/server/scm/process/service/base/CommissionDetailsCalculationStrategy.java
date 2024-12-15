package com.hete.supply.scm.server.scm.process.service.base;

import com.hete.supply.scm.server.scm.process.enums.ProcessType;

/**
 * 提成详情计算策略接口。
 *
 * @author yanjiawei
 * Created on 2024/2/26.
 */
public interface CommissionDetailsCalculationStrategy {

    /**
     * 根据工序订单扫描ID创建提成详情。
     *
     * @param processOrderScanId 工序订单扫描ID
     */
    void createCommissionDetails(Long processOrderScanId);

    /**
     * 获取工序类型。
     *
     * @return 工序类型
     */
    ProcessType getProcessType();
}
