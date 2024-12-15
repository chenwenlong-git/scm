package com.hete.supply.scm.server.scm.sample.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReceiptExportVo;
import com.hete.supply.scm.api.scm.facade.SampleReceiptFacade;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleReceiptExportService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/12 23:31
 */
@DubboService
@RequiredArgsConstructor
public class SampleReceiptFacadeImpl implements SampleReceiptFacade {
    private final SampleReceiptExportService sampleReceiptExportService;

    @Override
    public CommonResult<Integer> getExportTotals(SampleReceiptSearchDto dto) {


        return CommonResult.success(sampleReceiptExportService.getExportTotals(dto));

    }

    @Override
    public CommonPageResult<SampleReceiptExportVo> getExportList(SampleReceiptSearchDto dto) {
        return new CommonPageResult<>(sampleReceiptExportService.getExportList(dto));
    }
}
