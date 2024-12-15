package com.hete.supply.scm.server.scm.develop.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleOrderExportVo;
import com.hete.supply.scm.api.scm.facade.DevelopSampleOrderFacade;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleOrderBaseService;
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
public class DevelopSampleOrderFacadeImpl implements DevelopSampleOrderFacade {
    private final DevelopSampleOrderBaseService developSampleOrderBaseService;

    @Override
    public CommonResult<Integer> getExportTotals(DevelopSampleOrderSearchDto dto) {
        return CommonResult.success(developSampleOrderBaseService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<DevelopSampleOrderExportVo>> getExportList(DevelopSampleOrderSearchDto dto) {
        return developSampleOrderBaseService.getExportList(dto);
    }
}
