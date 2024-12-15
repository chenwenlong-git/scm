package com.hete.supply.scm.server.scm.settle.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleSettleOrderExportVo;
import com.hete.supply.scm.api.scm.facade.DevelopSampleSettleOrderFacade;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopExportBizService;
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
public class DevelopSampleSettleOrderFacadeImpl implements DevelopSampleSettleOrderFacade {
    private final DevelopExportBizService developExportBizService;

    @Override
    public CommonResult<Integer> getExportTotals(DevelopSampleSettleSearchDto dto) {
        return CommonResult.success(developExportBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<DevelopSampleSettleOrderExportVo>> getExportList(DevelopSampleSettleSearchDto dto) {
        return developExportBizService.getExportList(dto);
    }
}
