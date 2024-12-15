package com.hete.supply.scm.server.supplier.purchase.api.controller;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseProductSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildDetailDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoListDto;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseChildDetailVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseChildPrintVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseDeliverRawVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseProductSearchVo;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseBizService;
import com.hete.supply.scm.server.supplier.entity.dto.OverseasWarehouseMsgDto;
import com.hete.supply.scm.server.supplier.entity.vo.HeteCodeVo;
import com.hete.supply.scm.server.supplier.entity.vo.SkuBatchCodeVo;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.*;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.ConfirmCommissioningMsgVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDemandRawDetailVo;
import com.hete.supply.scm.server.supplier.purchase.enums.PurchaseRawReceived;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierDeliverBizService;
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

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/supplier/purchase")
@RequiredArgsConstructor
@Api(tags = "供应商--采购需求")
public class SupplierPurchaseController {
    private final PurchaseBizService purchaseBizService;
    private final SupplierDeliverBizService supplierDeliverBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("采购单列表")
    @PostMapping("/searchProductPurchase")
    public CommonPageResult<PurchaseProductSearchVo> searchProductPurchase(@NotNull @Validated @RequestBody PurchaseProductSearchDto dto) {
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getPurchaseOrderStatusList())) {
            dto.setPurchaseOrderStatusList(PurchaseOrderStatus.getSupplierAllStatusList());
        }

        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);

        return new CommonPageResult<>(purchaseBizService.searchProductPurchase(dto));
    }

    @ApiOperation("采购详情页")
    @PostMapping("/processPurchaseDetail")
    public CommonResult<PurchaseChildDetailVo> processPurchaseDetail(@NotNull @Validated @RequestBody PurchaseChildDetailDto dto) {
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getPurchaseOrderStatusList())) {
            dto.setPurchaseOrderStatusList(PurchaseOrderStatus.getSupplierAllStatusList());
        }

        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        return CommonResult.success(purchaseBizService.childOrderPurchaseDetail(dto));
    }

    @ApiOperation("批量打印加工采购单")
    @PostMapping("/batchPrintProcessPurchase")
    public CommonResult<ResultList<PurchaseChildPrintVo>> batchPrintProcessPurchase(@NotNull @Validated @RequestBody PurchaseChildNoListDto dto) {
        return CommonResult.successForList(purchaseBizService.batchPrintProcessPurchase(dto));
    }


    @ApiOperation("批量确认接单（大货/加工）")
    @PostMapping("/batchReceive")
    public CommonResult<Void> batchReceive(@NotNull @Validated @RequestBody PurchaseChildBatchReceiveDto dto) {
        purchaseBizService.batchReceive(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量供应商操作（大货/加工）")
    @PostMapping("/batchSupplierCtrl")
    public CommonResult<Void> batchSupplierCtrl(@NotNull @Validated @RequestBody PurchaseSupplierCtrlDto dto) {
        purchaseBizService.batchSupplierCtrl(dto);
        return CommonResult.success();
    }

    @ApiOperation("确认投产反查信息（大货采购）")
    @PostMapping("/confirmCommissioningMsg")
    public CommonResult<ResultList<ConfirmCommissioningMsgVo>> confirmCommissioningMsg(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {

        return CommonResult.successForList(purchaseBizService.confirmCommissioningMsg(dto));
    }

    @ApiOperation("确认投产")
    @PostMapping("/confirmCommissioning")
    public CommonResult<Void> confirmCommissioning(@NotNull @Validated @RequestBody ConfirmCommissioningDto dto) {
        purchaseBizService.confirmCommissioning(dto);
        return CommonResult.success();
    }


    @ApiOperation("采购原料是否完成收货")
    @PostMapping("/isPurchaseRawReceived")
    public CommonResult<PurchaseRawReceived> isPurchaseRawReceived(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {

        return CommonResult.success(purchaseBizService.isPurchaseRawReceived(dto.getPurchaseChildOrderNo()));
    }

    @ApiOperation("跳过/回退工序（大货/加工）")
    @PostMapping("/passProcess")
    public CommonResult<Void> passOrBackProcess(@NotNull @Validated @RequestBody PurchasePassDto dto) {
        purchaseBizService.passOrBackProcess(dto);
        return CommonResult.success();
    }

    @ApiOperation("打印赫特码（大货/加工）")
    @PostMapping("/printHeteCode")
    public CommonResult<HeteCodeVo> printHeteCode(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.success(purchaseBizService.printHeteCode(dto));
    }

    @ApiOperation("生成发货单")
    @PostMapping("/createDeliver")
    public CommonResult<Void> createDeliver(@NotNull @Validated @RequestBody PurchaseDeliverCreateDto dto) {
        supplierDeliverBizService.createDeliver(dto);
        return CommonResult.success();
    }

    @ApiOperation("归还原料")
    @PostMapping("/returnRaw")
    public CommonResult<Void> returnRaw(@NotNull @Validated @RequestBody PurchaseReturnRawDto dto) {
        supplierDeliverBizService.returnRaw(dto);
        return CommonResult.success();
    }

    @ApiOperation("打印批次码")
    @PostMapping("/printBatchCode")
    public CommonResult<ResultList<SkuBatchCodeVo>> printBatchCode(@NotNull @Validated @RequestBody PurchaseChildNoListDto dto) {

        return CommonResult.successForList(purchaseBizService.printBatchCode(dto));
    }

    @ApiOperation("上传海外仓文件")
    @PostMapping("/uploadOverseasFile")
    public CommonResult<Void> uploadOverseasFile(@NotNull @Validated @RequestBody OverseasWarehouseMsgDto dto) {
        purchaseBizService.uploadOverseasFile(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取原料库存情况")
    @PostMapping("/getDemandRawList")
    public CommonResult<PurchaseDemandRawDetailVo> getDemandRawList(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {

        return CommonResult.success(purchaseBizService.getDemandRawList(dto));
    }

    @ApiOperation("根据采购单号获取发货单原料消耗")
    @PostMapping("/getRawConsumeByPurchaseNo")
    public CommonResult<ResultList<PurchaseDeliverRawVo>> getRawConsumeByPurchaseNo(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {

        return CommonResult.successForList(purchaseBizService.getRawConsumeByPurchaseNo(dto));
    }
}
