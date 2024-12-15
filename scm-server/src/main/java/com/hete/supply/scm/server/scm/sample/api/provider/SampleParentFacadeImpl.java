package com.hete.supply.scm.server.scm.sample.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SampleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleParentExportVo;
import com.hete.supply.scm.api.scm.facade.SampleParentFacade;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleExportService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/12 00:34
 */
@DubboService
@RequiredArgsConstructor
public class SampleParentFacadeImpl implements SampleParentFacade {
    private final SampleExportService sampleExportService;

    @Override
    public CommonResult<Integer> getExportTotals(SampleSearchDto dto) {
        return CommonResult.success(sampleExportService.getSampleParentExportTotals(dto));
    }

    @Override
    public CommonPageResult<SampleParentExportVo> getExportList(SampleSearchDto dto) {
        return new CommonPageResult<>(sampleExportService.getSampleParentExportList(dto));
    }
}
