package com.hete.supply.scm.server.scm.develop.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.DevelopPricingOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopPricingOrderExportVo;
import com.hete.supply.scm.api.scm.facade.DevelopPricingOrderFacade;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopPricingOrderBaseService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2023/8/5 10:46
 */
@DubboService
@RequiredArgsConstructor
public class DevelopPricingOrderFacadeImpl implements DevelopPricingOrderFacade {
    private final DevelopPricingOrderBaseService developPricingOrderBaseService;

    @Override
    public CommonResult<Integer> getExportTotals(DevelopPricingOrderSearchDto dto) {
        return CommonResult.success(developPricingOrderBaseService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<DevelopPricingOrderExportVo>> getExportList(DevelopPricingOrderSearchDto dto) {
        return developPricingOrderBaseService.getExportList(dto);
    }
}
