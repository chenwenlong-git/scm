package com.hete.supply.scm.server.scm.supplier.api.task;

import com.hete.supply.scm.server.scm.supplier.service.init.SupplierInitService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class SupProdCompareInitJob {
    private final SupplierInitService supplierInitService;

    @XxlJob(value = "supProdCompareInitJob")
    public void supProdCompareInit() {
        supplierInitService.supProdCompareInit();
    }
}
