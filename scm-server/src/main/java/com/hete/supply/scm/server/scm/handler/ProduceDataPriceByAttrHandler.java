package com.hete.supply.scm.server.scm.handler;

import com.hete.supply.scm.server.scm.entity.dto.ProduceDataPriceByAttrDto;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.support.consistency.core.entity.bo.FailCallbackBo;
import com.hete.support.consistency.core.handler.AsyncHandler;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 异步任务调用事务SKU定价来更新商品采购价格
 *
 * @author ChenWenLong
 * @date 2024/3/26 14:04
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProduceDataPriceByAttrHandler implements AsyncHandler<ProduceDataPriceByAttrDto> {

    private String beanName;
    private final ProduceDataBaseService produceDataBaseService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid ProduceDataPriceByAttrDto request) {
        produceDataBaseService.updateGoodsPurchasePriceByAttrTask(request.getProduceDataAttrPriceUpdateBoList());
        return true;
    }

    @Override
    public void failed(@NotNull @Valid ProduceDataPriceByAttrDto request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("异步任务SKU定价来更新商品采购价格失败，bo={}", JacksonUtil.parse2Str(request));
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
