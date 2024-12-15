package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseDeliverExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/18 17:15
 */
public interface PurchaseDeliverFacade {
    CommonResult<Integer> getExportTotals(PurchaseDeliverListDto dto);

    CommonPageResult<PurchaseDeliverExportVo> getExportList(PurchaseDeliverListDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierExportTotals(PurchaseDeliverListDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonPageResult<PurchaseDeliverExportVo> getSupplierExportList(PurchaseDeliverListDto dto);
}
