package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseReturnExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/13 23:53
 */
public interface PurchaseReturnFacade {
    CommonResult<Integer> getExportTotals(PurchaseReturnDto dto);

    CommonResult<ExportationListResultBo<PurchaseReturnExportVo>> getExportList(PurchaseReturnDto dto);


    CommonResult<Integer> getSupplierExportTotals(PurchaseReturnDto dto);

    CommonResult<ExportationListResultBo<PurchaseReturnExportVo>> getSupplierExportList(PurchaseReturnDto dto);


}
