package com.hete.supply.scm.server.scm.purchase.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderProductVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderVo;
import com.hete.supply.scm.server.scm.settle.service.biz.PurchaseSettleOrderBizService;
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
 * @date 2022/11/1 13:39
 */
@Validated
@RestController
@RequestMapping("/scm/purchaseSettleOrder")
@RequiredArgsConstructor
@Api(tags = "采购结算单")
public class PurchaseSettleOrderController {
    private final PurchaseSettleOrderBizService purchaseSettleOrderBizService;

    @ApiOperation("查询采购结算单列表")
    @PostMapping("/searchPurchaseSettleOrder")
    public CommonPageResult<PurchaseSettleOrderVo> searchPurchaseSettleOrder(@NotNull @Validated @RequestBody PurchaseSettleOrderSearchDto dto) {
        return new CommonPageResult<>(purchaseSettleOrderBizService.searchPurchaseSettleOrder(dto));
    }

    @ApiOperation("查询采购结算单详情")
    @PostMapping("/getPurchaseSettleOrderDetail")
    public CommonResult<PurchaseSettleOrderDetailVo> getPurchaseSettleOrderDetail(@NotNull @Validated @RequestBody PurchaseSettleOrderDetailDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.getPurchaseSettleOrderDetail(dto));
    }

    @ApiOperation("查询结算产品明细")
    @PostMapping("/searchPurchaseSettleOrderProduct")
    public CommonResult<PurchaseSettleOrderProductVo> searchPurchaseSettleOrderProduct(@NotNull @Validated @RequestBody PurchaseSettleOrderProductDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.searchPurchaseSettleOrderProduct(dto));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@NotNull @Validated @RequestBody PurchaseSettleOrderUpdateDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.update(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/examine")
    public CommonResult<Boolean> examine(@NotNull @Validated @RequestBody PurchaseSettleOrderExamineDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.examine(dto));
    }

    @ApiOperation("提交支付信息")
    @PostMapping("/addPurchaseSettleOrderPay")
    public CommonResult<Boolean> addPurchaseSettleOrderPay(@NotNull @Validated @RequestBody PurchaseSettleOrderPayAddDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.addPurchaseSettleOrderPay(dto));
    }

    @ApiOperation("删除支付信息")
    @PostMapping("/delPurchaseSettleOrderPay")
    public CommonResult<Boolean> delPurchaseSettleOrderPay(@NotNull @Validated @RequestBody PurchaseSettleOrderPayDelDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.delPurchaseSettleOrderPay(dto));
    }

    @ApiOperation("编辑采购结算单明细信息")
    @PostMapping("/editPurchaseSettleOrderItem")
    public CommonResult<Boolean> editPurchaseSettleOrderItem(@NotNull @Validated @RequestBody EditPurchaseSettleOrderItemDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.editPurchaseSettleOrderItem(dto));
    }

}
