package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 根据加工成品SKU与订单类型修改加工单原料发货仓库
 *
 * @author yanjiawei
 * Created on 2024/11/11.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcDeliveryWarehouseJob {

    private final ProcessOrderBizService processOrderBizService;

    @XxlJob(value = "procDeliveryWarehouseJob")
    public void execute() {
        processOrderBizService.updateProcDeliveryWarehouseJob();
    }
}
