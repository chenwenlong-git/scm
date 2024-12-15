package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReceiptExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/12 23:28
 */
public interface SampleReceiptFacade {
    CommonResult<Integer> getExportTotals(SampleReceiptSearchDto dto);

    CommonPageResult<SampleReceiptExportVo> getExportList(SampleReceiptSearchDto dto);
}
