package com.hete.supply.scm.server.scm.ibfs.handler;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.server.scm.ibfs.service.biz.FinanceSettleOrderBizService;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import com.hete.trace.util.TraceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/5/30.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateSettleOrderHandler implements AsyncHandler<SupplierPo> {
    private String beanName;

    private final FinanceSettleOrderBizService financeSettleOrderBizService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid SupplierPo request) throws Exception {
        financeSettleOrderBizService.createFinanceSettleOrderJob(request);
        return true;
    }

    @Override
    public void failed(@NotNull @Valid SupplierPo request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("结算单创建失败！供应商编码:{},异常信息:{} spanId:{}", request.getSupplierCode(),
                JSON.toJSONString(failCallbackBo), TraceUtil.getSpanId());
    }

    @Override
    public @NotBlank String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
