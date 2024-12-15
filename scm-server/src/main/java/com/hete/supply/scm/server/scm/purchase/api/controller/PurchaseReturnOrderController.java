package com.hete.supply.scm.server.scm.purchase.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseReturnNoDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseReturnPrintDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseReturnRepairDto;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnPrintVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseReturnVo;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2023/06/27 18:17
 */
@RestController
@Api(tags = "供应链退货单管理")
@RequestMapping("/scm/purchaseReturnOrder")
@RequiredArgsConstructor
public class PurchaseReturnOrderController {

    private final SupplierReturnBizService supplierReturnBizService;

    @ApiOperation("退货单列表")
    @PostMapping("/getByPage")
    public CommonPageResult<PurchaseReturnVo> getByPage(@NotNull @Valid @RequestBody PurchaseReturnDto dto) {
        return new CommonPageResult<>(supplierReturnBizService.purchaseReturnList(dto));
    }

    @ApiOperation("退货单详情")
    @PostMapping("/detail")
    public CommonResult<PurchaseReturnDetailVo> detail(@NotNull @Validated @RequestBody PurchaseReturnNoDto dto) {
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
        supplierReturnBizService.exportSku(dto);
        return CommonResult.success();
    }


    @ApiOperation("退货返修")
    @PostMapping("/repair")
    public CommonResult<Void> repair(@NotNull @Validated @RequestBody PurchaseReturnRepairDto dto) {
        supplierReturnBizService.repair(dto);
        return CommonResult.success();
    }

}
