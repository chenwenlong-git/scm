package com.hete.supply.scm.server.scm.supplier.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PurchasePreOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchasePreOrderVo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.*;
import com.hete.supply.scm.server.scm.supplier.entity.vo.*;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierBizService;
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

/**
 * @author ChenWenLong
 * @date 2022/11/25 17:47
 */
@Validated
@RestController
@RequestMapping("/scm/supplier")
@RequiredArgsConstructor
@Api(tags = "供应商管理")
public class SupplierController {
    private final SupplierBizService supplierBizService;

    @ApiOperation("查询供应商列表")
    @PostMapping("/searchSupplier")
    public CommonPageResult<SupplierVo> searchSupplier(@NotNull @Validated @RequestBody SupplierDto dto) {
        return new CommonPageResult<>(supplierBizService.searchSupplier(dto));
    }

    @ApiOperation("查询供应商详情")
    @PostMapping("/getSupplierDetail")
    public CommonResult<SupplierDetailVo> getSupplierDetail(@NotNull @Validated @RequestBody SupplierDetailDto dto) {
        return CommonResult.success(supplierBizService.getSupplierDetail(dto));
    }

    @ApiOperation("添加供应商")
    @PostMapping("/addSupplier")
    public CommonResult<SupplierAddVo> addSupplier(@NotNull @Validated @RequestBody SupplierAddDto dto) {
        return CommonResult.success(supplierBizService.addSupplier(dto));
    }

    @ApiOperation("编辑供应商")
    @PostMapping("/editSupplier")
    public CommonResult<Boolean> editSupplier(@NotNull @Validated @RequestBody SupplierEditDto dto) {
        return CommonResult.success(supplierBizService.editSupplier(dto));
    }

    @ApiOperation("开启关闭提交")
    @PostMapping("/openClose")
    public CommonResult<Boolean> openClose(@NotNull @Validated @RequestBody SupplierVersionDto dto) {
        return CommonResult.success(supplierBizService.openClose(dto));
    }

    @ApiOperation("获取所有供应商简略信息")
    @PostMapping("/getSimpleSupplierMsg")
    public CommonResult<ResultList<SupplierSimpleVo>> getSimpleSupplierMsg() {
        return CommonResult.successForList(supplierBizService.getSimpleSupplierMsg());
    }

    @ApiOperation("下拉框获取当前登录用户归属供应商")
    @PostMapping("/getSupplierQuickSearch")
    public CommonPageResult<SupplierQuickSearchVo> getSupplierQuickSearch(@NotNull @Validated @RequestBody SupplierQuickSearchDto dto) {
        return new CommonPageResult<>(supplierBizService.getSupplierQuickSearch(dto));
    }

    @ApiOperation("PLM获取供应商列表")
    @PostMapping("/getSupplierSearch")
    public CommonResult<ResultList<SupplierSearchVo>> getSupplierSearch(@NotNull @Validated @RequestBody SupplierSearchDto dto) {
        return CommonResult.successForList(supplierBizService.getSupplierSearch(dto));
    }

    @ApiOperation("获取采购预下单列表")
    @PostMapping("/queryPurchasePreOrderList")
    public CommonResult<PurchasePreOrderVo> queryPurchasePreOrderList(@NotNull @Validated @RequestBody PurchasePreOrderDto dto) {
        return CommonResult.success(supplierBizService.queryPurchasePreOrderList(dto));
    }

    @ApiOperation("下拉框获取供应商")
    @PostMapping("/getSupplierOpenSearch")
    public CommonResult<ResultList<SupplierDropDownSearchVo>> getSupplierOpenSearch(@NotNull @Validated @RequestBody SupplierDropDownSearchDto dto) {
        return CommonResult.successForList(supplierBizService.getSupplierOpenSearch(dto));
    }


}
