package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SampleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleParentExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/11 23:39
 */
public interface SampleParentFacade {
    CommonResult<Integer> getExportTotals(SampleSearchDto dto);

    CommonPageResult<SampleParentExportVo> getExportList(SampleSearchDto dto);
}
