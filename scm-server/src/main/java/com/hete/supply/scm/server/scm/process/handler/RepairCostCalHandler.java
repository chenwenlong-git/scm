package com.hete.supply.scm.server.scm.process.handler;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.server.scm.process.entity.bo.CalculateRepairOrderCostBo;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBaseService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RepairCostCalHandler implements AsyncHandler<CalculateRepairOrderCostBo> {

    private String beanName;
    private final RepairOrderBaseService repairOrderBaseService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid CalculateRepairOrderCostBo request) {
        String repairOrderNo = request.getRepairOrderNo();
        if (StrUtil.isBlank(repairOrderNo)) {
            log.error("计算返修单成本失败！返修单号为空");
            return false;
        }
        repairOrderBaseService.calCost(repairOrderNo);
        return true;
    }

    @Override
    public void failed(@NotNull @Valid CalculateRepairOrderCostBo request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
    }

    @Override
    public @NotBlank String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
