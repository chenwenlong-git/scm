package com.hete.supply.scm.server.scm.handler;

import com.hete.supply.scm.server.scm.entity.dto.ProduceDataPurchasePriceDto;
import com.hete.supply.scm.server.scm.service.biz.ProduceDataBizService;
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
 * 异步更新生产资料采购商品价格
 *
 * @author ChenWenLong
 * @date 2024/3/26 14:04
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProduceDataPurchasePriceHandler implements AsyncHandler<ProduceDataPurchasePriceDto> {

    private String beanName;
    private final ProduceDataBizService produceDataBizService;

    @Override
    public boolean tryAsyncTask(@NotNull @Valid ProduceDataPurchasePriceDto request) {
        produceDataBizService.updateGoodsPurchasePriceBySkuHandler(request.getProduceDataUpdatePurchasePriceBoList());
        return true;
    }

    @Override
    public void failed(@NotNull @Valid ProduceDataPurchasePriceDto request,
                       @NotNull FailCallbackBo failCallbackBo) throws Exception {
        log.error("采购更新生产资料的商品采购价格失败，bo={}", JacksonUtil.parse2Str(request));
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
