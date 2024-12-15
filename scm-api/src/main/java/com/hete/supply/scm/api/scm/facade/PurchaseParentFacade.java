package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseSearchNewDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseParentExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/12 11:33
 */
public interface PurchaseParentFacade {
    CommonResult<Integer> exportPurchaseParentTotals(PurchaseSearchNewDto dto);

    CommonResult<ExportationListResultBo<PurchaseParentExportVo>> exportPurchaseParent(PurchaseSearchNewDto dto);

}
