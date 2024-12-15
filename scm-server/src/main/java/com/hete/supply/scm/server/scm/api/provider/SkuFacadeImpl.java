package com.hete.supply.scm.server.scm.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SkuCodeListDto;
import com.hete.supply.scm.api.scm.entity.vo.SkuProduceDataVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuRelatedDataVo;
import com.hete.supply.scm.api.scm.facade.SkuFacade;
import com.hete.supply.scm.server.scm.service.biz.SkuBizService;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2023/10/12 14:41
 */
@DubboService
@RequiredArgsConstructor
public class SkuFacadeImpl implements SkuFacade {
    private final SkuBizService skuBizService;

    @Override
    public CommonResult<ResultList<SkuProduceDataVo>> getSkuProduceDataBySkuList(SkuCodeListDto dto) {
        return CommonResult.successForList(skuBizService.getSkuProduceDataBySkuList(dto));
    }

    @Override
    public CommonResult<ResultList<SkuRelatedDataVo>> getSkuRelatedDataBySkuList(SkuCodeListDto dto) {
        return CommonResult.successForList(skuBizService.getSkuRelatedDataBySkuList(dto));
    }
}
