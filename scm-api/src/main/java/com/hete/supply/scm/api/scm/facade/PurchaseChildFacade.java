package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

import javax.validation.Valid;

/**
 * @author weiwenxin
 * @date 2022/12/12 11:33
 */
public interface PurchaseChildFacade {
    CommonResult<Integer> getExportTotals(PurchaseProductSearchDto dto);

    CommonPageResult<PurchaseChildExportVo> getExportList(PurchaseProductSearchDto dto);

    CommonResult<Integer> getSkuExportTotals(PurchaseProductSearchDto dto);

    CommonPageResult<PurchaseChildSkuExportVo> getSkuExportList(PurchaseProductSearchDto dto);

    CommonResult<Integer> getRawExportTotals(PurchaseProductSearchDto dto);

    CommonPageResult<PurchaseChildRawExportVo> getRawExportList(PurchaseProductSearchDto dto);

    /**
     * 获取供应商系统采购子单导出数据
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierExportTotals(PurchaseProductSearchDto dto);

    /**
     * 获取供应商系统采购子单导出数据
     *
     * @param dto
     * @return
     */
    CommonPageResult<PurchaseChildExportVo> getSupplierExportList(PurchaseProductSearchDto dto);

    /**
     * 供应商系统采购子单导出数据（按sku）
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierSkuExportTotals(PurchaseProductSearchDto dto);

    /**
     * 供应商系统采购子单导出数据（按sku）
     *
     * @param dto
     * @return
     */
    CommonPageResult<PurchaseChildSkuExportVo> getSupplierSkuExportList(PurchaseProductSearchDto dto);

    /**
     * 获取采购未交数量（已弃用，请使用SkuFacade接口getSkuRelatedDataBySkuList）
     *
     * @param dto
     * @return
     */
    @Deprecated
    CommonResult<ResultList<PurchaseSkuCntVo>> getPurchaseUndeliveredCnt(SkuCodeListDto dto);

    /**
     * 获取采购在途数量（已弃用，请使用SkuFacade接口getSkuRelatedDataBySkuList）
     *
     * @param dto
     * @return
     */
    @Deprecated
    CommonResult<ResultList<PurchaseSkuCntVo>> getPurchaseInTransitCnt(SkuCodeListDto dto);

    CommonResult<Integer> exportPurchaseChildTotals(PurchaseSearchNewDto dto);

    CommonResult<ExportationListResultBo<PurchaseChildNewExportVo>> exportPurchaseChild(PurchaseSearchNewDto dto);

    /**
     * 采购需求单按sku维度导出
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> exportPurchaseBySkuTotals(PurchaseSearchNewDto dto);

    /**
     * 采购需求单按sku维度导出
     *
     * @param dto
     * @return
     */
    CommonResult<ExportationListResultBo<PurchaseParentSkuExportVo>> exportPurchaseBySku(PurchaseSearchNewDto dto);

    CommonResult<Integer> exportPurchaseChildDeliverTotals(PurchaseDeliverListDto dto);

    CommonResult<ExportationListResultBo<PurchaseChildDeliverExportVo>> exportPurchaseChildDeliver(PurchaseDeliverListDto dto);

    CommonResult<ResultList<SupplierSkuBatchCodeVo>> getSupplierBySkuBatchCode(@Valid SkuBatchCodeDto skuBatchCodeDto);

    /**
     * 导出待确认采购订单列表
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> exportPurchaseChildPreConfirmTotals(PurchaseProductSearchDto dto);


    /**
     * 导出待确认采购订单列表
     *
     * @param dto
     * @return
     */
    CommonResult<ExportationListResultBo<PurchaseChildPreConfirmExportVo>> exportPurchaseChildPreConfirm(PurchaseProductSearchDto dto);

    /**
     * 采购未交总数=sum（SKU关联非已作废采购订单的采购未交数）提供给wms
     *
     * @param dto
     * @return
     */
    CommonResult<ResultList<SkuUndeliveredCntVo>> getPurchaseUndeliveredCntBySku(SkuListDto dto);

    /**
     * 根据单号查找采购子单
     *
     * @param dto
     * @return
     */
    CommonResult<ResultList<PurchaseVo>> getPurchaseVoByNo(PurchaseChildNoListDto dto);

    /**
     * 采购子单的BOM原料导出总数
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/11/27 14:25
     */
    CommonResult<Integer> getRawSkuExportTotals(PurchaseProductSearchDto dto);

    /**
     * 采购子单的BOM原料导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < PurchaseChildSkuRawExportVo>>
     * @author ChenWenLong
     * @date 2024/11/27 18:27
     */
    CommonResult<ExportationListResultBo<PurchaseChildSkuRawExportVo>> getRawSkuExportList(PurchaseProductSearchDto dto);

}
