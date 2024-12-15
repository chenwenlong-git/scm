package com.hete.supply.scm.server.supplier.purchase.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuBatchCodeQuickSearchDto;
import com.hete.supply.scm.server.scm.entity.vo.SkuBatchCodeQuickSearchVo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDeliverDto;
import com.hete.supply.scm.server.supplier.entity.dto.*;
import com.hete.supply.scm.server.supplier.entity.vo.ShippingMarkDetailVo;
import com.hete.supply.scm.server.supplier.entity.vo.ShippingMarkListVo;
import com.hete.supply.scm.server.supplier.entity.vo.ShippingMarkNoVo;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseDeliverIdAndVersionDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseDeliverNoListDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseDeliverOrderNoDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseWaitDeliverDto;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverPrintVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseWaitDeliverVo;
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

/**
 * @author weiwenxin
 * @date 2022/11/3 11:44
 */
@Validated
@RestController
@RequestMapping("/supplier/purchase/deliver")
@RequiredArgsConstructor
@Api(tags = "供应商--发货管理")
public class SupplierPurchaseDeliverController {
    private final SupplierDeliverBizService supplierDeliverBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("发货管理列表")
    @PostMapping("/deliverList")
    public CommonPageResult<PurchaseDeliverVo> searchProductPurchase(@NotNull @Validated @RequestBody PurchaseDeliverListDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);

        return new CommonPageResult<>(supplierDeliverBizService.deliverList(dto));
    }

    @ApiOperation("发货管理详情")
    @PostMapping("/deliverDetail")
    public CommonResult<PurchaseDeliverDetailVo> deliverDetail(@NotNull @Validated @RequestBody PurchaseDeliverOrderNoDto dto) {
        return CommonResult.success(supplierDeliverBizService.deliverDetail(dto));
    }


    @ApiOperation("取消发货")
    @PostMapping("/cancelDeliver")
    public CommonResult<Void> cancelDeliver(@NotNull @Validated @RequestBody PurchaseDeliverIdAndVersionDto dto) {
        supplierDeliverBizService.cancelDeliver(dto);

        return CommonResult.success();
    }

    @ApiOperation("打印发货单")
    @PostMapping("/printDeliverOrder")
    public CommonResult<ResultList<PurchaseDeliverPrintVo>> printDeliverOrder(@NotNull @Validated @RequestBody PurchaseDeliverNoListDto dto) {
        return CommonResult.successForList(supplierDeliverBizService.printDeliverOrder(dto));
    }

    @ApiOperation("待发货采购发货单列表")
    @PostMapping("/waitDeliverOrderList")
    public CommonPageResult<PurchaseWaitDeliverVo> waitDeliverOrderList(@NotNull @Validated @RequestBody PurchaseWaitDeliverDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);

        return new CommonPageResult<>(supplierDeliverBizService.waitDeliverOrderList(dto));
    }

    @ApiOperation("生成箱唛")
    @PostMapping("/createShippingMark")
    public CommonResult<ShippingMarkNoVo> createShippingMark(@NotNull @Validated @RequestBody ShippingMarkDto dto) {
        return CommonResult.success(supplierDeliverBizService.createShippingMark(dto));
    }


    @ApiOperation("生成海外仓箱唛")
    @PostMapping("/createOverSeasShippingMark")
    public CommonResult<ShippingMarkNoVo> createOverSeasShippingMark(@NotNull @Validated @RequestBody OverSeasShippingMarkDto dto) {
        return CommonResult.success(supplierDeliverBizService.createOverSeasShippingMark(dto));
    }

    @ApiOperation("箱唛列表")
    @PostMapping("/shippingMarkList")
    public CommonPageResult<ShippingMarkListVo> shippingMarkList(@NotNull @Validated @RequestBody ShippingMarkListDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);

        return new CommonPageResult<>(supplierDeliverBizService.shippingMarkList(dto));
    }

    @ApiOperation("获取装箱明细")
    @PostMapping("/shippingMarkDetail")
    public CommonResult<ShippingMarkDetailVo> shippingMarkDetail(@NotNull @Validated @RequestBody ShippingMarkNoDto dto) {

        return CommonResult.success(supplierDeliverBizService.shippingMarkDetail(dto));
    }

    @ApiOperation("确认发货")
    @PostMapping("/deliver")
    public CommonResult<Void> deliver(@NotNull @Validated @RequestBody PurchaseDeliverDto dto) {
        supplierDeliverBizService.deliver(dto);

        return CommonResult.success();
    }

    @ApiOperation("作废箱唛")
    @PostMapping("/cancelShippingMark")
    public CommonResult<Void> cancelShippingMark(@NotNull @Validated @RequestBody ShippingMarkIdAndVersionDto dto) {
        supplierDeliverBizService.cancelShippingMark(dto);

        return CommonResult.success();
    }

    @ApiOperation("打印箱唛接口")
    @PostMapping("/printShippingMark")
    public CommonResult<ResultList<ShippingMarkDetailVo>> printShippingMark(@NotNull @Validated @RequestBody ShippingMarkNoListDto dto) {

        return CommonResult.successForList(supplierDeliverBizService.printShippingMark(dto));
    }

    @ApiOperation("获取装箱明细")
    @PostMapping("/h5ShippingMarkDetail")
    public CommonResult<ShippingMarkDetailVo> h5ShippingMarkDetail(@NotNull @Validated @RequestBody ShippingMarkNumDto dto) {

        return CommonResult.success(supplierDeliverBizService.h5ShippingMarkDetail(dto));
    }

    @ApiOperation("获取sku批次码下拉列表")
    @PostMapping("/getSkuBatchCodeQuickSearch")
    public CommonResult<ResultList<SkuBatchCodeQuickSearchVo>> getSkuBatchCodeQuickSearch(@NotNull @Validated @RequestBody SkuBatchCodeQuickSearchDto dto) {
        return CommonResult.successForList(supplierDeliverBizService.getSkuBatchCodeQuickSearch(dto));
    }

}
