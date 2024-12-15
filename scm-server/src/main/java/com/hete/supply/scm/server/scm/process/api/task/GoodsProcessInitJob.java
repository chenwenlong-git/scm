package com.hete.supply.scm.server.scm.process.api.task;

import com.hete.supply.scm.server.scm.service.biz.SkuBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2023/2/3 11:30
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GoodsProcessInitJob {
    private final SkuBizService skuBizService;

    @XxlJob(value = "goodsProcessInitTask")
    public void execute() {
        skuBizService.batchInsertGoodsProcess();
        log.info("同步sku到plmSku和GoodsProcess成功！");
    }
}
