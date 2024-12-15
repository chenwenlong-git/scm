package com.hete.supply.scm.server.scm.adjust.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.GoodsPriceDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsPriceExportVo;
import com.hete.supply.scm.api.scm.facade.GoodsPriceFacade;
import com.hete.supply.scm.server.scm.adjust.service.biz.GoodsPriceBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2024/6/19 14:16
 */
@DubboService
@RequiredArgsConstructor
public class GoodsPriceFacadeFacadeImpl implements GoodsPriceFacade {
    private final GoodsPriceBizService goodsPriceBizService;

    @Override
    public CommonResult<Integer> getExportTotals(GoodsPriceDto dto) {
        return CommonResult.success(goodsPriceBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<GoodsPriceExportVo>> getExportList(GoodsPriceDto dto) {
        return goodsPriceBizService.getExportList(dto);
    }
}
