package com.hete.supply.scm.server.scm.adjust.api.task;

import com.hete.supply.scm.server.scm.adjust.service.biz.GoodsPriceBizService;
import com.hete.supply.scm.server.scm.adjust.service.init.GoodsPriceInitService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ChenWenLong
 * @date 2024/6/20 15:35
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class GoodsPriceJob {
    private final GoodsPriceBizService goodsPriceBizService;
    private final GoodsPriceInitService goodsPriceInitService;

    @XxlJob(value = "updateGoodsPriceEffectiveTask")
    public void updateGoodsPriceEffectiveTask() {
        log.info("自动更新商品调价的生效时间任务");
        long start = System.currentTimeMillis();
        String param = XxlJobHelper.getJobParam();
        goodsPriceBizService.updateGoodsPriceEffectiveTask(param);
        log.info("结束更新商品调价的生效时间任务,耗时={}ms", System.currentTimeMillis() - start);
    }

    @XxlJob(value = "initGoodsPriceTask")
    public void initGoodsPriceTask() {
        log.info("初始化商品调价的生效状态任务");
        long start = System.currentTimeMillis();
        String param = XxlJobHelper.getJobParam();
        goodsPriceInitService.initGoodsPriceTask(param);
        log.info("初始化商品调价的生效状态任务,耗时={}ms", System.currentTimeMillis() - start);
    }
}
