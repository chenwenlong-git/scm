package com.hete.supply.scm.server.supplier.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseRawReceiptExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RawReceiptExportVo;
import com.hete.supply.scm.api.scm.facade.RawReceiptFacade;
import com.hete.supply.scm.server.supplier.service.biz.SupplierRawReceiptExportService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/13 01:24
 */
@DubboService
@RequiredArgsConstructor
public class RawReceiptFacadeImpl implements RawReceiptFacade {
    private final SupplierRawReceiptExportService rawReceiptExportService;

    @Override
    public CommonResult<Integer> getExportTotals(PurchaseRawReceiptSearchDto dto) {
        return CommonResult.success(rawReceiptExportService.getExportTotals(dto));
    }

    @Override
    public CommonPageResult<RawReceiptExportVo> getExportList(PurchaseRawReceiptSearchDto dto) {
        return new CommonPageResult<>(rawReceiptExportService.getExportList(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseRawReceiptExportVo>> getNewRawReceiptExportList(PurchaseRawReceiptSearchDto dto) {
        return rawReceiptExportService.getNewRawReceiptExportList(dto);
    }


    /**
     * 供应商系统
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getSupplierExportTotals(PurchaseRawReceiptSearchDto dto) {
        return CommonResult.success(rawReceiptExportService.getSupplierExportTotals(dto));
    }

    /**
     * 供应商系统
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<RawReceiptExportVo> getSupplierExportList(PurchaseRawReceiptSearchDto dto) {
        return new CommonPageResult<>(rawReceiptExportService.getSupplierExportList(dto));
    }
}
