package com.hete.supply.scm.server.scm.purchase.api.controller;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */

import com.hete.supply.scm.api.scm.entity.dto.PurchaseProductSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSearchNewDto;
import com.hete.supply.scm.api.scm.entity.dto.SkuCodeListDto;
import com.hete.supply.scm.api.scm.entity.dto.SkuListDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuUndeliveredCntVo;
import com.hete.supply.scm.server.scm.entity.vo.RawDeliverVo;
import com.hete.supply.scm.server.scm.entity.vo.RawReceiveOrderVo;
import com.hete.supply.scm.server.scm.entity.vo.WmsDetailVo;
import com.hete.supply.scm.server.scm.feishu.service.base.FeiShuBaseService;
import com.hete.supply.scm.server.scm.purchase.entity.dto.*;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.*;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseBizService;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseExportService;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseRawBizService;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildOrderNoDto;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderNoListVo;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.OverseasWarehouseMsgDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseDeliverIdAndVersionDto;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawDispenseMsgVo;
import com.hete.supply.wms.api.interna.entity.vo.InventoryVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/scm/purchase")
@RequiredArgsConstructor
@Api(tags = "采购需求")
public class PurchaseController {
    private final PurchaseBizService purchaseBizService;
    private final SampleBaseService sampleBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final PurchaseRawBaseService purchaseRawBaseService;
    private final PurchaseExportService purchaseExportService;
    private final PurchaseRawBizService purchaseRawBizService;
    private final FeiShuBaseService feiShuBaseService;

    @ApiOperation("新建采购需求单")
    @PostMapping("/createPurchase")
    public CommonResult<PurchaseParentOrderPo> createPurchase(@NotNull @Validated @RequestBody PurchaseCreateDto dto) {
        return CommonResult.success(purchaseBizService.createPurchase(dto));
    }

    @ApiOperation("新采购需求列表")
    @PostMapping("/searchPurchaseNew")
    public CommonPageResult<PurchaseSearchNewVo> searchPurchaseNew(@NotNull @Validated @RequestBody PurchaseSearchNewDto dto) {
        return new CommonPageResult<>(purchaseBizService.searchPurchaseNew(dto));
    }

    @ApiOperation("采购需求单导出")
    @PostMapping("/exportPurchaseParent")
    public CommonResult<Void> exportPurchaseParent(@NotNull @Validated @RequestBody PurchaseSearchNewDto dto) {
        purchaseBizService.exportPurchaseParent(dto);
        return CommonResult.success();
    }

    @ApiOperation("采购需求单按sku导出")
    @PostMapping("/exportPurchaseParentBySku")
    public CommonResult<Void> exportPurchaseParentBySku(@NotNull @Validated @RequestBody PurchaseSearchNewDto dto) {
        purchaseBizService.exportPurchaseParentBySku(dto);
        return CommonResult.success();
    }

    @ApiOperation("采购订单导出")
    @PostMapping("/exportPurchaseChild")
    public CommonResult<Void> exportPurchaseChild(@NotNull @Validated @RequestBody PurchaseSearchNewDto dto) {
        purchaseBizService.exportPurchaseChild(dto);
        return CommonResult.success();
    }

    @ApiOperation("新采购需求详情页")
    @PostMapping("/purchaseDetailNew")
    public CommonResult<PurchaseDetailNewVo> purchaseDetailNew(@NotNull @Validated @RequestBody PurchaseParentNoDto dto) {
        return CommonResult.success(purchaseBizService.purchaseDetailNew(dto));
    }

    @ApiOperation("取消采购【母单】")
    @PostMapping("/cancelPurchase")
    public CommonResult<Void> cancelPurchase(@NotNull @Validated @RequestBody PurchaseParentNoListDto dto) {
        purchaseBizService.cancelPurchase(dto);
        return CommonResult.success();
    }

    @Deprecated
    @ApiOperation("后模糊查询样品子单号")
    @PostMapping("/searchSampleNo")
    public CommonResult<SampleChildOrderNoListVo> searchSampleNo(@NotNull @Validated @RequestBody SampleChildOrderNoDto dto) {
        return CommonResult.success(sampleBaseService.searchSampleNo(dto));
    }

    @ApiOperation("拆分子单")
    @PostMapping("/splitChildOrderNew")
    public CommonResult<Void> splitChildOrderNew(@NotNull @Validated @RequestBody PurchaseSplitNewDto dto) {
        purchaseBizService.splitChildOrderNew(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据供应商与sku获取发货信息")
    @PostMapping("/getSupplierDateDetail")
    public CommonResult<ResultList<PurchaseSkuSupplierItemVo>> getSupplierDateDetail(@NotNull @Validated @RequestBody PurchaseSkuSupplierDto dto) {
        return CommonResult.successForList(purchaseBaseService.getSupplierDateDetail(dto));
    }

    @ApiOperation("计划确认")
    @PostMapping("/planConfirm")
    public CommonResult<Void> planConfirm(@NotNull @Validated @RequestBody PurchasePlanConfirmDto dto) {
        purchaseBizService.planConfirm(dto);
        return CommonResult.success();
    }

    @ApiOperation("跟单确认")
    @PostMapping("/followConfirm")
    public CommonResult<Void> followConfirm(@NotNull @Validated @RequestBody PurchaseFollowConfirmDto dto) {
        purchaseBizService.followConfirm(dto);
        return CommonResult.success();
    }

    @ApiOperation("回退采购子单状态")
    @PostMapping("/purchaseBackStatus")
    public CommonResult<Void> purchaseBackStatus(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {
        purchaseBizService.purchaseBackStatus(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量提交审核")
    @PostMapping("/batchSubmitApprove")
    public CommonResult<Void> batchSubmitApprove(@NotNull @Validated @RequestBody PurchaseParentNoListDto dto) {
        purchaseBizService.batchSubmitApprove(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量审核")
    @PostMapping("/batchApprove")
    public CommonResult<Void> batchApprove(@NotNull @Validated @RequestBody PurchaseApproveDto dto) {
        purchaseBizService.batchApprove(dto);
        return CommonResult.success();
    }

    @ApiOperation("编辑采购单")
    @PostMapping("/editPurchase")
    public CommonResult<Void> editPurchase(@NotNull @Validated @RequestBody PurchaseEditDto dto) {
        purchaseBizService.editPurchase(dto);
        return CommonResult.success();
    }

    @ApiOperation("采购子单列表")
    @PostMapping("/searchProductPurchase")
    public CommonPageResult<PurchaseProductSearchVo> searchProductPurchase(@NotNull @Validated @RequestBody PurchaseProductSearchDto dto) {
        return new CommonPageResult<>(purchaseBizService.searchProductPurchase(dto));
    }

    @ApiOperation("拆分子单页面编辑回显")
    @PostMapping("/getSplitDetail")
    public CommonResult<PurchaseSplitDetailVo> getSplitDetail(@NotNull @Validated @RequestBody PurchaseChildNoListDto dto) {
        return CommonResult.success(purchaseBizService.getSplitDetail(dto));
    }

    @ApiOperation("编辑子单页面")
    @PostMapping("/editSplitDetail")
    public CommonResult<Void> editSplitDetail(@NotNull @Validated @RequestBody PurchaseChildEditDto dto) {
        purchaseBizService.editSplitDetail(dto);
        return CommonResult.success();
    }

    @ApiOperation("采购子单详情页")
    @PostMapping("/childOrderPurchaseDetail")
    public CommonResult<PurchaseChildDetailVo> childOrderPurchaseDetail(@NotNull @Validated @RequestBody PurchaseChildDetailDto dto) {
        return CommonResult.success(purchaseBizService.childOrderPurchaseDetail(dto));
    }

    @Deprecated
    @ApiOperation("批量确认采购单")
    @PostMapping("/batchConfirmPurchaseOrder")
    public CommonResult<Void> batchConfirmPurchaseOrder(@NotNull @Validated @RequestBody PurchaseBatchConfirmDto dto) {
        purchaseBizService.batchConfirmPurchaseOrder(dto);
        return CommonResult.success();
    }


    @ApiOperation("采购单终止来货（大货/加工）")
    @PostMapping("/forceFinishPurchase")
    public CommonResult<Void> forceFinishPurchase(@NotNull @Validated @RequestBody PurchaseFinishDto dto) {
        purchaseBizService.forceFinishPurchase(dto);
        return CommonResult.success();
    }

    @Deprecated
    @ApiOperation("根据采购母单号查询sku列表")
    @PostMapping("/getSkuListByPurchaseParentNo")
    public CommonResult<ResultList<String>> getSkuListByPurchaseParentNo(@NotNull @Validated @RequestBody PurchaseParentNoDto dto) {

        return CommonResult.successForList(purchaseBizService.getSkuListByPurchaseParentNo(dto));
    }

    @ApiOperation("母单终结状态判断")
    @PostMapping("/isPurchaseEndStatus")
    public CommonResult<ResultList<PurchaseEndStatusVo>> isPurchaseEndStatus(@NotNull @Validated @RequestBody PurchaseChildNoListDto dto) {
        return CommonResult.successForList(purchaseBizService.isPurchaseEndStatus(dto));
    }

    @ApiOperation("作废")
    @PostMapping("/cancel")
    public CommonResult<Void> cancel(@NotNull @Validated @RequestBody PurchaseBatchCancelDto dto) {
        purchaseBizService.batchCancel(dto);
        return CommonResult.success();
    }

    @ApiOperation("修改收货仓库")
    @PostMapping("/editWarehouse")
    public CommonResult<Void> editWarehouse(@NotNull @Validated @RequestBody PurchaseEditWarehouseDto dto) {
        purchaseBizService.editWarehouse(dto);
        return CommonResult.success();
    }

    @Deprecated
    @ApiOperation("上传海外仓文件")
    @PostMapping("/uploadOverseasFile")
    public CommonResult<Void> uploadOverseasFile(@NotNull @Validated @RequestBody OverseasWarehouseMsgDto dto) {
        purchaseBizService.uploadOverseasFile(dto);
        return CommonResult.success();
    }

    @Deprecated
    @ApiOperation("打印海外仓条码")
    @PostMapping("/printOverseasBarcode")
    public CommonResult<ResultList<String>> printOverseasBarcode(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.successForList(purchaseBizService.printOverseasBarcode(dto));
    }

    @ApiOperation("获取采购未交数量")
    @PostMapping("/getPurchaseUndeliveredCnt")
    public CommonResult<ResultList<PurchaseSkuCntVo>> getPurchaseUndeliveredCnt(@NotNull @Validated @RequestBody SkuCodeListDto dto) {
        return CommonResult.successForList(purchaseBaseService.getPurchaseUndeliveredCnt(dto));
    }

    @ApiOperation("获取采购在途数量")
    @PostMapping("/getPurchaseInTransitCnt")
    public CommonResult<ResultList<PurchaseSkuCntVo>> getPurchaseInTransitCnt(@NotNull @Validated @RequestBody SkuCodeListDto dto) {
        return CommonResult.successForList(purchaseBaseService.getPurchaseInTransitCnt(dto));
    }

    @ApiOperation("补充原料")
    @PostMapping("/supplyRaw")
    public CommonResult<Void> supplyRaw(@NotNull @Validated @RequestBody PurchaseSupplyRawDto dto) {
        purchaseBizService.supplyRaw(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量需求完成")
    @PostMapping("/batchFinishPurchase")
    public CommonResult<Void> batchFinishPurchase(@NotNull @Validated @RequestBody PurchaseParentNoListDto dto) {
        purchaseBizService.batchFinishPurchase(dto);
        return CommonResult.success();
    }

    @ApiOperation("完结采购单")
    @PostMapping("/finishPurchase")
    public CommonResult<Void> finishPurchase(@NotNull @Validated @RequestBody PurchaseParentIdAndVersionDto dto) {
        purchaseBizService.finishPurchase(dto);
        return CommonResult.success();
    }

    @ApiOperation("原料出库信息")
    @PostMapping("/rawDeliveryOrder")
    public CommonResult<ResultList<RawDeliverVo>> rawDeliveryOrder(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.successForList(purchaseBizService.rawDeliveryOrder(dto));
    }

    @ApiOperation("原料入库信息")
    @PostMapping("/getReceiveOrder")
    public CommonResult<ResultList<RawReceiveOrderVo>> rawReceiveOrder(@NotNull @Valid @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.successForList(purchaseRawBaseService.rawReceiveOrder(dto.getPurchaseChildOrderNo()));
    }

    @ApiOperation("仓储信息")
    @PostMapping("/getWmsDetailVo")
    public CommonResult<WmsDetailVo> getWmsDetailVo(@NotNull @Valid @RequestBody PurchaseChildNoListDto dto) {
        return CommonResult.success(purchaseRawBaseService.getWmsDetailVo(dto));
    }

    @ApiOperation("根据sku和供应商获取采购价")
    @PostMapping("/getPurchasePriceBySkuAndSupplier")
    public CommonResult<ResultList<PurchaseSkuPriceVo>> getPurchasePriceBySkuAndSupplier(@NotNull @Valid @RequestBody PurchaseSkuAndSupplierDto dto) {
        return CommonResult.successForList(purchaseRawBaseService.getPurchasePriceBySkuAndSupplier(dto));
    }

    @ApiOperation("拆单补交")
    @PostMapping("/splitOrderSupply")
    public CommonResult<Void> splitOrderSupply(@NotNull @Valid @RequestBody PurchaseSplitSupplyDto dto) {
        purchaseBizService.splitOrderSupply(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据子单获取补交数")
    @PostMapping("/getSupplyCntByChildOrderNo")
    public CommonResult<PurchaseSupplyCntVo> getSupplyCntByChildOrderNo(@NotNull @Valid @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.success(purchaseBizService.getSupplyCntByChildOrderNo(dto.getPurchaseChildOrderNo()));
    }

    @ApiOperation("获取采购单sku可拆分数")
    @PostMapping("/getSkuSpiltCnt")
    public CommonResult<PurchaseSkuSplitCntVo> getSkuSpiltCnt(@NotNull @Valid @RequestBody PurchaseParentNoDto dto) {
        return CommonResult.success(purchaseBizService.getSkuSpiltCnt(dto));
    }

    @ApiOperation("取消发货")
    @PostMapping("/cancelDeliver")
    public CommonResult<Void> cancelDeliver(@NotNull @Validated @RequestBody PurchaseDeliverIdAndVersionDto dto) {
        purchaseBizService.cancelDeliver(dto);

        return CommonResult.success();
    }

    @ApiOperation("获取产前样类型采购单单号")
    @PostMapping("/getPrenatalSampleOrderByNo")
    public CommonResult<PurchaseChildOrderNoListVo> getPrenatalSampleOrderByNo(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.success(purchaseBizService.getPrenatalSampleOrderByNo(dto));
    }

    @ApiOperation("采购子单发货单维度导出")
    @PostMapping("/exportPurchaseChildDeliver")
    public CommonResult<Void> exportPurchaseChildDeliver(@NotNull @Validated @RequestBody PurchaseProductSearchDto dto) {
        purchaseBizService.exportPurchaseChildDeliver(dto);
        return CommonResult.success();
    }

    @ApiOperation("SCM采购单待确认列表导出")
    @PostMapping("/getPurchasePreConfirmExport")
    public CommonResult<Void> getPurchasePreConfirmExport(@NotNull @Validated @RequestBody PurchaseProductSearchDto dto) {
        purchaseExportService.getPurchasePreConfirmExport(dto);
        return CommonResult.success();
    }

    @ApiOperation("原料信息")
    @PostMapping("/getRawMsgByNo")
    public CommonResult<ResultList<PurchaseRawMsgVo>> getRawMsgByNo(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.successForList(purchaseRawBizService.getRawMsgByNo(dto));
    }

    @ApiOperation("采购快速补单")
    @PostMapping("/fastSupply")
    public CommonResult<Void> fastSupply(@NotNull @Validated @RequestBody PurchaseFastSupplyDto dto) {
        purchaseBizService.fastSupply(dto);
        return CommonResult.success();
    }

    @ApiOperation("原料分配信息")
    @PostMapping("/getRawDispenseMsgByNo")
    public CommonResult<ResultList<PurchaseRawDispenseMsgVo>> getRawDispenseCntMsgByNo(@NotNull @Validated @RequestBody PurchaseDispenseDto dto) {
        return CommonResult.successForList(purchaseRawBizService.getRawDispenseCntMsgByNo(dto));
    }

    @ApiOperation("采购未交总数=sum（SKU关联非已作废采购订单的采购未交数）提供给wms")
    @PostMapping("/getPurchaseUndeliveredCntBySku")
    public CommonResult<ResultList<SkuUndeliveredCntVo>> getPurchaseUndeliveredCntBySku(@NotNull @Validated @RequestBody SkuListDto dto) {
        return CommonResult.successForList(purchaseBaseService.getPurchaseUndeliveredCntBySku(dto));
    }

    @ApiOperation("查询可创建质检的采购子单号")
    @PostMapping("/qcCheck")
    public CommonResult<QcCheckVo> qcCheck(@NotNull @Validated @RequestBody QcCheckRequestDto dto) {
        return CommonResult.success(purchaseBizService.qcCheck(dto));
    }

    @ApiOperation("采购调价")
    @PostMapping("/adjustPrice")
    public CommonResult<Void> adjustPrice(@NotNull @Validated @RequestBody PurchaseAdjustPriceDto dto) {
        purchaseBizService.adjustPrice(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取辅料默认价格")
    @PostMapping("/getDefaultPrice")
    public CommonResult<ResultList<PurchaseDefaultPriceItemVo>> getDefaultPrice(@NotNull @Validated @RequestBody PurchaseDefaultPriceDto dto) {
        return CommonResult.successForList(purchaseBizService.getDefaultPrice(dto));
    }

    @ApiOperation("获取指定库位库存")
    @PostMapping("/getAvailableInventory")
    CommonResult<ResultList<InventoryVo>> getAvailableInventory(@NotNull @Validated @RequestBody PurchaseAIQDto dto) {
        return CommonResult.successForList(purchaseBizService.getAvailableInventory(dto));
    }

    @ApiOperation("采购子单的BOM原料导出")
    @PostMapping("/exportPurchaseChildBomRaw")
    public CommonResult<Void> exportPurchaseChildBomRaw(@NotNull @Validated @RequestBody PurchaseProductSearchDto dto) {
        purchaseBizService.exportPurchaseChildBomRaw(dto);
        return CommonResult.success();
    }

    @PostMapping("/test1")
    void test1(@NotNull @Validated @RequestBody String msg) {
        feiShuBaseService.test(msg);
    }
}
