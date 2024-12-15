package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseRawReceiptExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RawReceiptExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/13 01:15
 */
public interface RawReceiptFacade {
    CommonResult<Integer> getExportTotals(PurchaseRawReceiptSearchDto dto);

    CommonPageResult<RawReceiptExportVo> getExportList(PurchaseRawReceiptSearchDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierExportTotals(PurchaseRawReceiptSearchDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonPageResult<RawReceiptExportVo> getSupplierExportList(PurchaseRawReceiptSearchDto dto);


    /**
     * 采购原料收货导出
     *
     * @param dto
     * @return
     */
    CommonResult<ExportationListResultBo<PurchaseRawReceiptExportVo>> getNewRawReceiptExportList(PurchaseRawReceiptSearchDto dto);
}
