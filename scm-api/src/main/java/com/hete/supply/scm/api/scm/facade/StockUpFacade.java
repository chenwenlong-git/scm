package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.StockUpSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.StockUpExportVo;
import com.hete.supply.scm.api.scm.entity.vo.StockUpItemExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/1/22 18:09
 */
public interface StockUpFacade {
    CommonResult<Integer> getExportTotals(StockUpSearchDto dto);

    CommonResult<ExportationListResultBo<StockUpExportVo>> getExportList(StockUpSearchDto dto);

    CommonResult<Integer> getItemExportTotals(StockUpSearchDto dto);

    CommonResult<ExportationListResultBo<StockUpItemExportVo>> getItemExportList(StockUpSearchDto dto);
}
