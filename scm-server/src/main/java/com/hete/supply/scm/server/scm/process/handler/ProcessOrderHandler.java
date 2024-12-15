package com.hete.supply.scm.server.scm.process.handler;

import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.entity.bo.OperatorUserBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderBo;
import com.hete.supply.scm.server.scm.process.enums.MissingInfoOperationType;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderBaseService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/19 11:50
 */
@Component
@RequiredArgsConstructor
public class ProcessOrderHandler implements AsyncHandler<ProcessOrderBo> {

    private final ProcessOrderBaseService processOrderBaseService;

    private String beanName;

    @Override
    public void failed(@NotNull @Valid ProcessOrderBo request, @NotNull FailCallbackBo failCallbackBo) throws Exception {

    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    @RedisLock(prefix = ScmRedisConstant.SCM_REFRESH_MISSING_INFO_PREFIX, key = "#processOrderBo.traceId", waitTime = 3, leaseTime = -1)
    public boolean tryAsyncTask(@NotNull @Valid ProcessOrderBo processOrderBo) {
        OperatorUserBo operatorUserBo = new OperatorUserBo();
        operatorUserBo.setOperator(processOrderBo.getOperator());
        operatorUserBo.setOperatorUsername(processOrderBo.getOperatorUsername());
        processOrderBaseService.refreshMissingInfo(processOrderBo.getProcessOrderNo(), operatorUserBo,
                MissingInfoOperationType.CHECK_MATERIAL_PROCEDURE, true);
        return true;
    }


    @Override
    public void setBeanName(String name) {
        // BeanNameAware的接口,保存beanName即可
        this.beanName = name;
    }
}
