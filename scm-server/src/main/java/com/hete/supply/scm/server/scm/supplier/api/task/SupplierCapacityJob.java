package com.hete.supply.scm.server.scm.supplier.api.task;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierCapacityBizService;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

/**
 * @author yanjiawei
 * Created on 2024/8/5.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SupplierCapacityJob {
    private final SupplierCapacityBizService supplierCapacityBizService;

    @XxlJob(value = "initSupplierCapacityTask")
    public void initSupplierCapacityTask() {
        String addDaysStr = XxlJobHelper.getJobParam();
        log.info("initSupplierCapacityTask addDaysStr:{}", addDaysStr);
        supplierCapacityBizService.initSupplierCapacity(addDaysStr);
    }
}
