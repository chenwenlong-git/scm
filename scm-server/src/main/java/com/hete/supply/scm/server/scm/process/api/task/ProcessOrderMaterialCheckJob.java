package com.hete.supply.scm.server.scm.process.api.task;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/12/5 14:14
 */
@Component
@RequiredArgsConstructor
public class ProcessOrderMaterialCheckJob {
    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderBizService processOrderBizService;

    @XxlJob(value = "processOrderMaterialCheckTask")
    public void processOrderMaterialCheckTask() {
        // 查询待下单状态的加工单
        List<ProcessOrderPo> processOrderPos = processOrderDao.getByTypeAndStatus(ProcessOrderType.LIMITED, ProcessOrderStatus.WAIT_ORDER);
        if (CollectionUtils.isNotEmpty(processOrderPos)) {
            processOrderPos.forEach(processOrderBizService::checkMaterial);
        }
    }

}
