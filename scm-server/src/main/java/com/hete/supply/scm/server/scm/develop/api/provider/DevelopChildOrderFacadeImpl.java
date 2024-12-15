package com.hete.supply.scm.server.scm.develop.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopChildOrderExportVo;
import com.hete.supply.scm.api.scm.facade.DevelopChildOrderFacade;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopChildBaseService;
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
public class DevelopChildOrderFacadeImpl implements DevelopChildOrderFacade {
    private final DevelopChildBaseService developChildBaseService;

    @Override
    public CommonResult<Integer> getExportTotals(DevelopChildSearchDto dto) {
        return CommonResult.success(developChildBaseService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<DevelopChildOrderExportVo>> getExportList(DevelopChildSearchDto dto) {
        return developChildBaseService.getExportList(dto);
    }
}
