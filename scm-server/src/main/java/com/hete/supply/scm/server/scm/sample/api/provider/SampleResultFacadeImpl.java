package com.hete.supply.scm.server.scm.sample.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SampleChildOrderResultSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildResultExportVo;
import com.hete.supply.scm.api.scm.facade.SampleResultFacade;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleResultExportService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2023/5/15 17:25
 */
@DubboService
@RequiredArgsConstructor
public class SampleResultFacadeImpl implements SampleResultFacade {
    private final SampleResultExportService sampleResultExportService;

    @Override
    public CommonResult<Integer> getExportTotals(SampleChildOrderResultSearchDto dto) {
        return CommonResult.success(sampleResultExportService.getExportTotals(dto));
    }

    @Override
    public CommonPageResult<SampleChildResultExportVo> getExportList(SampleChildOrderResultSearchDto dto) {
        return new CommonPageResult<>(sampleResultExportService.getExportList(dto));
    }
}
