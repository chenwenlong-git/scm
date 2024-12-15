package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SearchInventoryDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/1/22 18:09
 */
public interface SupplierInventoryFacade {
    CommonResult<Integer> getExportTotals(SearchInventoryDto dto);

    CommonResult<ExportationListResultBo<SupplierInventoryExportVo>> getExportList(SearchInventoryDto dto);
}
