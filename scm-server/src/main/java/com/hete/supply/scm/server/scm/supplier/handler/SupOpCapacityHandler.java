package com.hete.supply.scm.server.scm.supplier.handler;

import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBatchBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierCapacityBaseService;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierCapacityBizService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/8/6.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SupOpCapacityHandler implements AsyncHandler<SupOpCapacityBatchBo> {
    private final SupplierCapacityBizService supplierCapacityBizService;
    private String beanName;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid SupOpCapacityBatchBo request) throws Exception {
        supplierCapacityBizService.updateSupplierCapacityBatch(request);
        return true;
    }

    @Override
    public void failed(@NotNull @Valid SupOpCapacityBatchBo request, @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("批量变更供应商产能失败！request: {}, failCallbackBo: {}", request, failCallbackBo);
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
