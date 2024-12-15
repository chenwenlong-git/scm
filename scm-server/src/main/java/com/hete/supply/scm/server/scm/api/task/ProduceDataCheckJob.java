package com.hete.supply.scm.server.scm.api.task;

import com.hete.supply.scm.server.scm.service.init.ProduceDataInitService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ChenWenLong
 * @date 2024/2/26 14:24
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProduceDataCheckJob {
    private final ProduceDataInitService produceDataInitService;

    /**
     * 初始化生产信息BOM名称
     */
    @XxlJob(value = "initProduceDataItemBomTask")
    public void initProduceDataItemBomTask() {
        produceDataInitService.initProduceDataItemBomTask();
    }

    /**
     * 初始化生产信息商品采购价格
     */
    @XxlJob(value = "initProduceDataGoodsPriceTask")
    public void initProduceDataGoodsPriceTask() {
        produceDataInitService.initProduceDataGoodsPriceTask();
    }


    /**
     * 初始化生产信息规格书不存在主表的数据
     */
    @XxlJob(value = "initProduceDataSpecTask")
    public void initProduceDataSpecTask() {
        produceDataInitService.initProduceDataSpecTask();
    }

    /**
     * 初始化生产信息的重量数据
     */
    @XxlJob(value = "initProduceDataWeightTask")
    public void initProduceDataWeightTask() {
        produceDataInitService.initProduceDataWeightTask();
    }

    /**
     * 初始化生产属性的数据
     */
    @XxlJob(value = "initProduceDataAttrTask")
    public void initProduceDataAttrTask() {
        produceDataInitService.initProduceDataAttrTask();
    }


}
