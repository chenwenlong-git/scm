package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PrepaymentSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.PrepaymentExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/5/25 16:17
 */
public interface FinancePrepaymentFacade {

    CommonResult<Integer> getExportTotals(PrepaymentSearchDto dto);


    CommonResult<ExportationListResultBo<PrepaymentExportVo>> getExportList(PrepaymentSearchDto dto);
}
