package com.hete.supply.scm.server.scm.process.handler;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.process.entity.bo.CreateRepairOrderSyncBo;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBaseService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/1/8.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReOrderInvMatchHandler implements AsyncHandler<CreateRepairOrderSyncBo> {
    private final RepairOrderBaseService repairOrderBaseService;

    private String beanName;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid CreateRepairOrderSyncBo createRepairOrderSyncBo) {
        String planNo = createRepairOrderSyncBo.getPlanNo();
        String repairOrderNo = createRepairOrderSyncBo.getRepairOrderNo();
        repairOrderBaseService.doRepairOrderStockMatching(planNo, repairOrderNo);
        return true;
    }

    @Override
    public void failed(@NotNull @Valid CreateRepairOrderSyncBo request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("创建返修单结果异步任务失败！request:{},msg:{}",
                JSON.toJSONString(request), failCallbackBo.getBizErrorMsg());
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
