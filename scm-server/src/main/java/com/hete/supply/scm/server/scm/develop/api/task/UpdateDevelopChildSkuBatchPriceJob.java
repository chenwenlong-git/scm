package com.hete.supply.scm.server.scm.develop.api.task;

import com.hete.supply.scm.server.scm.develop.service.init.DevelopChildInitService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 一次性初始化开发子单批次码的单价
 *
 * @author ChenWenLong
 * @date 2024/1/27 21:52
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateDevelopChildSkuBatchPriceJob {
    private final DevelopChildInitService developChildInitService;

    @XxlJob(value = "initDevelopOrderPricePriceJob")
    public void initDevelopOrderPricePrice() {
        developChildInitService.initDevelopOrderPricePrice();
    }
}

