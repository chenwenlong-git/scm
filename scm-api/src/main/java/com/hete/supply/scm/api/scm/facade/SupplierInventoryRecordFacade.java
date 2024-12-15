package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.InventoryRecordDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryRecordExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/1/22 18:09
 */
public interface SupplierInventoryRecordFacade {
    CommonResult<Integer> getExportTotals(InventoryRecordDto dto);

    CommonResult<ExportationListResultBo<SupplierInventoryRecordExportVo>> getExportList(InventoryRecordDto dto);
}
