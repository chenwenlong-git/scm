package com.hete.supply.scm.server.scm.supplier.api.task;

import com.hete.supply.scm.server.scm.supplier.service.init.SupplierInitService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/1/16 14:47
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SupplierInitJob {
    private final SupplierInitService supplierInitService;


    @XxlJob(value = "initSupplierInventory")
    public void initSupplierInventory() {
        supplierInitService.initSupplierInventory();
    }

    @XxlJob(value = "initSupplierWarehouse")
    public void initSupplierWarehouse() {
        supplierInitService.initSupplierWarehouse();
    }
}
