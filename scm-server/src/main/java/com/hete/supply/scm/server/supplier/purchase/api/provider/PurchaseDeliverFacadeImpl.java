package com.hete.supply.scm.server.supplier.purchase.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseDeliverExportVo;
import com.hete.supply.scm.api.scm.facade.PurchaseDeliverFacade;
import com.hete.supply.scm.server.supplier.purchase.service.biz.PurchaseDeliverExportService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/18 18:22
 */
@DubboService
@RequiredArgsConstructor
public class PurchaseDeliverFacadeImpl implements PurchaseDeliverFacade {
    private final PurchaseDeliverExportService purchaseDeliverExportService;

    @Override
    public CommonResult<Integer> getExportTotals(PurchaseDeliverListDto dto) {
        return CommonResult.success(purchaseDeliverExportService.getExportTotals(dto));

    }

    @Override
    public CommonPageResult<PurchaseDeliverExportVo> getExportList(PurchaseDeliverListDto dto) {
        return new CommonPageResult<>(purchaseDeliverExportService.getExportList(dto));
    }

    @Override
    public CommonResult<Integer> getSupplierExportTotals(PurchaseDeliverListDto dto) {
        return CommonResult.success(purchaseDeliverExportService.getSupplierExportTotals(dto));

    }

    @Override
    public CommonPageResult<PurchaseDeliverExportVo> getSupplierExportList(PurchaseDeliverListDto dto) {
        return new CommonPageResult<>(purchaseDeliverExportService.getSupplierExportList(dto));
    }
}
