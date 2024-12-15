package com.hete.supply.scm.server.supplier.purchase.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseReturnConfirmDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseReturnNoDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseReturnPrintDto;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnPrintVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnVo;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.scm.server.supplier.service.biz.SupplierReturnBizService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 09:37
 */
@Validated
@RestController
@RequestMapping("/supplier/purchase/return")
@RequiredArgsConstructor
@Api(tags = "供应商--退货管理")
public class SupplierPurchaseReturnController {
    private final SupplierReturnBizService supplierReturnBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("退货管理列表")
    @PostMapping("/purchaseReturnList")
    public CommonPageResult<PurchaseReturnVo> purchaseReturnList(@NotNull @Validated @RequestBody PurchaseReturnDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtils.isEmpty(dto.getReturnOrderStatusList())) {
            ArrayList<ReturnOrderStatus> returnOrderStatusList = new ArrayList<>();
            returnOrderStatusList.add(ReturnOrderStatus.WAIT_RECEIVE);
            returnOrderStatusList.add(ReturnOrderStatus.RECEIPTED);
            dto.setReturnOrderStatusList(returnOrderStatusList);
        }

        return new CommonPageResult<>(supplierReturnBizService.purchaseReturnList(dto));
    }

    @ApiOperation("退货管理详情")
    @PostMapping("/purchaseReturnDetail")
    public CommonResult<PurchaseReturnDetailVo> purchaseReturnDetail(@NotNull @Validated @RequestBody PurchaseReturnNoDto dto) {

        return CommonResult.success(supplierReturnBizService.purchaseReturnDetail(dto));
    }

    @ApiOperation("批量打印退货单")
    @PostMapping("/batchPrint")
    public CommonResult<ResultList<PurchaseReturnPrintVo>> batchPrint(@NotNull @Validated @RequestBody PurchaseReturnPrintDto dto) {
        return CommonResult.successForList(supplierReturnBizService.batchPrintReturn(dto));
    }

    @ApiOperation("退货单导出")
    @PostMapping("/exportSku")
    public CommonResult<Void> exportSku(@NotNull @Validated @RequestBody PurchaseReturnDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtils.isEmpty(dto.getReturnOrderStatusList())) {
            ArrayList<ReturnOrderStatus> returnOrderStatusList = new ArrayList<>();
            returnOrderStatusList.add(ReturnOrderStatus.WAIT_RECEIVE);
            returnOrderStatusList.add(ReturnOrderStatus.RECEIPTED);
            dto.setReturnOrderStatusList(returnOrderStatusList);
        }
        supplierReturnBizService.exportSkuSupplier(dto);
        return CommonResult.success();
    }

    @ApiOperation("确认收货")
    @PostMapping("/returnConfirm")
    public CommonResult<Void> returnConfirm(@NotNull @Validated @RequestBody PurchaseReturnConfirmDto dto) {
        supplierReturnBizService.returnConfirm(dto);
        return CommonResult.success();
    }


}
