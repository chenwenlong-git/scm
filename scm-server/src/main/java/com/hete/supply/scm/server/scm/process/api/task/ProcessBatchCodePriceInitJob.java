package com.hete.supply.scm.server.scm.process.api.task;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderItemBizService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessBatchCodePriceInitJob {
    private final ProcessOrderItemBizService processOrderItemBizService;

    @XxlJob(value = "processBatchCodePriceInitTask")
    public void execute() {
        String ponStr = XxlJobHelper.getJobParam();

        if (StrUtil.isNotBlank(ponStr)) {
            processOrderItemBizService.initializeBatchCodePrices(ponStr);
        } else {
            processOrderItemBizService.initializeBatchCodePricesByTypes(
                    Set.of(ProcessOrderType.NORMAL, ProcessOrderType.EXTRA, ProcessOrderType.LIMITED,
                            ProcessOrderType.OVERSEAS_REPAIR));
        }
    }
}
