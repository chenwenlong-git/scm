package com.hete.supply.scm.server.scm.purchase.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.supply.scm.api.scm.facade.PurchaseChildFacade;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseExportService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/12 15:37
 */
@DubboService
@RequiredArgsConstructor
public class PurchaseChildFacadeImpl implements PurchaseChildFacade {
    private final PurchaseExportService purchaseExportService;
    private final PurchaseBaseService purchaseBaseService;

    @Override
    public CommonResult<Integer> getExportTotals(PurchaseProductSearchDto dto) {
        return CommonResult.success(purchaseExportService.getChildExportTotals(dto));
    }

    @Override
    public CommonPageResult<PurchaseChildExportVo> getExportList(PurchaseProductSearchDto dto) {
        return new CommonPageResult<>(purchaseExportService.getChildExportList(dto));
    }

    @Override
    public CommonResult<Integer> getSkuExportTotals(PurchaseProductSearchDto dto) {
        return CommonResult.success(purchaseExportService.getSkuChildExportTotals(dto));
    }

    @Override
    public CommonPageResult<PurchaseChildSkuExportVo> getSkuExportList(PurchaseProductSearchDto dto) {
        return new CommonPageResult<>(purchaseExportService.getSkuChildExportList(dto));
    }

    @Override
    public CommonResult<Integer> getRawExportTotals(PurchaseProductSearchDto dto) {
        return CommonResult.success(purchaseExportService.getRawChildExportTotals(dto));
    }

    @Override
    public CommonPageResult<PurchaseChildRawExportVo> getRawExportList(PurchaseProductSearchDto dto) {
        return new CommonPageResult<>(purchaseExportService.getRawChildExportList(dto));
    }


    @Override
    public CommonResult<Integer> getSupplierExportTotals(PurchaseProductSearchDto dto) {
        return CommonResult.success(purchaseExportService.getSupplierChildExportTotals(dto));
    }

    @Override
    public CommonPageResult<PurchaseChildExportVo> getSupplierExportList(PurchaseProductSearchDto dto) {
        return new CommonPageResult<>(purchaseExportService.getSupplierChildExportList(dto));
    }

    @Override
    public CommonResult<Integer> getSupplierSkuExportTotals(PurchaseProductSearchDto dto) {
        return CommonResult.success(purchaseExportService.getSupplierSkuChildExportTotals(dto));
    }

    @Override
    public CommonPageResult<PurchaseChildSkuExportVo> getSupplierSkuExportList(PurchaseProductSearchDto dto) {
        return new CommonPageResult<>(purchaseExportService.getSupplierSkuChildExportList(dto));
    }

    @Override
    public CommonResult<ResultList<PurchaseSkuCntVo>> getPurchaseUndeliveredCnt(SkuCodeListDto dto) {
        return CommonResult.successForList(purchaseBaseService.getPurchaseUndeliveredCnt(dto));
    }

    @Override
    public CommonResult<ResultList<PurchaseSkuCntVo>> getPurchaseInTransitCnt(SkuCodeListDto dto) {
        return CommonResult.successForList(purchaseBaseService.getPurchaseInTransitCnt(dto));
    }

    @Override
    public CommonResult<Integer> exportPurchaseChildTotals(PurchaseSearchNewDto dto) {
        return CommonResult.success(purchaseExportService.exportPurchaseParentTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseChildNewExportVo>> exportPurchaseChild(PurchaseSearchNewDto dto) {

        return purchaseExportService.exportPurchaseChild(dto);
    }

    @Override
    public CommonResult<Integer> exportPurchaseBySkuTotals(PurchaseSearchNewDto dto) {
        return CommonResult.success(purchaseExportService.exportPurchaseBySkuTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseParentSkuExportVo>> exportPurchaseBySku(PurchaseSearchNewDto dto) {
        return purchaseExportService.exportPurchaseBySku(dto);
    }

    @Override
    public CommonResult<Integer> exportPurchaseChildDeliverTotals(PurchaseDeliverListDto dto) {
        return CommonResult.success(purchaseExportService.exportPurchaseChildDeliverTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseChildDeliverExportVo>> exportPurchaseChildDeliver(PurchaseDeliverListDto dto) {
        return purchaseExportService.exportPurchaseChildDeliver(dto);
    }

    @Override
    public CommonResult<ResultList<SupplierSkuBatchCodeVo>> getSupplierBySkuBatchCode(SkuBatchCodeDto dto) {
        return CommonResult.successForList(purchaseBaseService.getSupplierBySkuBatchCode(dto));
    }

    @Override
    public CommonResult<Integer> exportPurchaseChildPreConfirmTotals(PurchaseProductSearchDto dto) {
        return CommonResult.success(purchaseExportService.getChildExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseChildPreConfirmExportVo>> exportPurchaseChildPreConfirm(PurchaseProductSearchDto dto) {
        return purchaseExportService.exportPurchaseChildPreConfirm(dto);
    }

    @Override
    public CommonResult<ResultList<SkuUndeliveredCntVo>> getPurchaseUndeliveredCntBySku(SkuListDto dto) {
        return CommonResult.successForList(purchaseBaseService.getPurchaseUndeliveredCntBySku(dto));
    }

    @Override
    public CommonResult<ResultList<PurchaseVo>> getPurchaseVoByNo(PurchaseChildNoListDto dto) {
        return CommonResult.successForList(purchaseBaseService.getPurchaseVoByNo(dto));
    }

    @Override
    public CommonResult<Integer> getRawSkuExportTotals(PurchaseProductSearchDto dto) {
        return CommonResult.success(purchaseExportService.getRawSkuChildExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseChildSkuRawExportVo>> getRawSkuExportList(PurchaseProductSearchDto dto) {
        return purchaseExportService.getRawSkuChildExportList(dto);
    }


}
