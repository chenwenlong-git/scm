package com.hete.supply.scm.server.supplier.purchase.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseReturnExportVo;
import com.hete.supply.scm.api.scm.facade.PurchaseReturnFacade;
import com.hete.supply.scm.server.supplier.purchase.service.biz.PurchaseReturnExportService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/13 23:58
 */
@DubboService
@RequiredArgsConstructor
public class PurchaseReturnFacadeImpl implements PurchaseReturnFacade {
    private final PurchaseReturnExportService purchaseReturnExportService;

    @Override
    public CommonResult<Integer> getExportTotals(PurchaseReturnDto dto) {
        return CommonResult.success(purchaseReturnExportService.getExportTotals(dto));

    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseReturnExportVo>> getExportList(PurchaseReturnDto dto) {
        return purchaseReturnExportService.getExportList(dto);
    }

    @Override
    public CommonResult<Integer> getSupplierExportTotals(PurchaseReturnDto dto) {
        return CommonResult.success(purchaseReturnExportService.getSupplierExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseReturnExportVo>> getSupplierExportList(PurchaseReturnDto dto) {
        return purchaseReturnExportService.getSupplierExportList(dto);
    }
}
