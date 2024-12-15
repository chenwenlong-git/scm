package com.hete.supply.scm.server.scm.production.api.task;

import com.hete.supply.scm.server.scm.production.service.biz.SkuProdBizService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 更新颜色生产属性
 *
 * @author yanjiawei
 * Created on 2024/9/27.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshColorAttrJob {
    private final SkuProdBizService skuProdBizService;

    @XxlJob(value = "refreshColorAttrJob")
    public void execute() {
        skuProdBizService.refreshColorAttr();
    }
}
