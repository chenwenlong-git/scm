package com.hete.supply.scm.server.scm.cost.handler;

import com.hete.supply.scm.api.scm.importation.entity.dto.CostImportDto;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.cost.CostBuilder;
import com.hete.supply.scm.server.scm.cost.service.biz.CostBizService;
import com.hete.supply.scm.server.scm.process.entity.bo.MonthStartWeightAvgPriceResultBo;
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
 * Created on 2024/1/25.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdMonWightAvgHandler implements AsyncHandler<MonthStartWeightAvgPriceResultBo> {

    private final CostBizService costBizService;
    private String beanName;

    @Override
    @RedisLock(prefix = ScmRedisConstant.UPDATE_MONTH_START_WEIGHTED_AVERAGE, key = "#request.sku", waitTime = 3, leaseTime = -1)
    public boolean tryAsyncTask(@NotNull @Valid MonthStartWeightAvgPriceResultBo request) {
        CostImportDto costImportDto = CostBuilder.buildCostImportDto(request);
        costBizService.saveChangeMoDataSync(costImportDto);
        return true;
    }

    @Override
    public void failed(@NotNull @Valid MonthStartWeightAvgPriceResultBo request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("更新SKU:{} 价格:{}月初加权价失败! 异常信息：{}",
                request.getSku(), request.getMonthStartWeightedAveragePrice(), failCallbackBo.getBizErrorMsg());
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
