package com.hete.supply.scm.server.supplier.settle.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderProductDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderProductVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderVo;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierPurchaseSettleOrderBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
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
 * @date 2022/11/1 19:38
 */
@Validated
@RestController
@RequestMapping("/supplier/processSettleOrder")
@RequiredArgsConstructor
@Api(tags = "供应商-采购结算单")
public class SupplierPurchaseSettleOrderController {
    private final SupplierPurchaseSettleOrderBizService supplierPurchaseSettleOrderBizService;

    @ApiOperation("查询采购结算单列表")
    @PostMapping("/searchPurchaseSettleOrder")
    public CommonPageResult<PurchaseSettleOrderVo> searchPurchaseSettleOrder(@NotNull @Validated @RequestBody PurchaseSettleOrderSearchDto dto) {
        return new CommonPageResult<>(supplierPurchaseSettleOrderBizService.searchPurchaseSettleOrder(dto));
    }

    @ApiOperation("查询采购结算单详情")
    @PostMapping("/getPurchaseSettleOrderDetail")
    public CommonResult<PurchaseSettleOrderDetailVo> getDetailByUserSeq(@NotNull @Validated @RequestBody PurchaseSettleOrderDetailDto dto) {
        return CommonResult.success(supplierPurchaseSettleOrderBizService.getPurchaseSettleOrderDetail(dto));
    }

    @ApiOperation("查询结算产品明细")
    @PostMapping("/searchPurchaseSettleOrderProduct")
    public CommonResult<PurchaseSettleOrderProductVo> getDetailByUserSeq(@NotNull @Validated @RequestBody PurchaseSettleOrderProductDto dto) {
        return CommonResult.success(supplierPurchaseSettleOrderBizService.searchPurchaseSettleOrderProduct(dto));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@NotNull @Validated @RequestBody PurchaseSettleOrderUpdateDto dto) {
        return CommonResult.success(supplierPurchaseSettleOrderBizService.update(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/updatePurchaseSettleStatus")
    public CommonResult<Boolean> edit(@NotNull @Validated @RequestBody PurchaseSettleOrderExamineDto dto) {
        return CommonResult.success(supplierPurchaseSettleOrderBizService.examine(dto));
    }


}
