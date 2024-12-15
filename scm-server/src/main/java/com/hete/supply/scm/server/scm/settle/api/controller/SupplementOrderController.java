package com.hete.supply.scm.server.scm.settle.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportVo;
import com.hete.supply.scm.server.scm.entity.dto.SupplementBusinessSettleDto;
import com.hete.supply.scm.server.scm.entity.dto.SupplementSkuDropDownDto;
import com.hete.supply.scm.server.scm.entity.vo.DeductSupplementBusinessSettleVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuDropDownVo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDropDownDto;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseDropDownVo;
import com.hete.supply.scm.server.scm.settle.entity.dto.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementDeductOrderListVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderSettleDropDownVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderVo;
import com.hete.supply.scm.server.scm.settle.service.biz.SupplementOrderBizService;
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
 * @date 2022/11/1 15:55
 */
@Validated
@RestController
@RequestMapping("/scm/supplementOrder")
@RequiredArgsConstructor
@Api(tags = "补款单")
public class SupplementOrderController {
    private final SupplementOrderBizService supplementOrderBizService;

    @ApiOperation("查询补款单列表")
    @PostMapping("/searchSupplementOrder")
    public CommonPageResult<SupplementOrderVo> searchSupplementOrder(@NotNull @Validated @RequestBody SupplementOrderDto dto) {
        return new CommonPageResult<>(supplementOrderBizService.searchSupplementOrder(dto));
    }

    @ApiOperation("查询补款单详情")
    @PostMapping("/getSupplementOrderDetail")
    public CommonResult<SupplementOrderDetailVo> getSupplementOrderDetail(@NotNull @Validated @RequestBody SupplementOrderDetailDto dto) {
        return CommonResult.success(supplementOrderBizService.getSupplementOrderDetail(dto));
    }

    @ApiOperation("添加补款单")
    @PostMapping("/addSupplementOrder")
    public CommonResult<Boolean> addSupplementOrder(@NotNull @Validated @RequestBody SupplementOrderAddDto dto) {
        return CommonResult.success(supplementOrderBizService.addSupplementOrder(dto));
    }

    @ApiOperation("编辑补款单")
    @PostMapping("/editSupplementOrder")
    public CommonResult<Boolean> editSupplementOrder(@NotNull @Validated @RequestBody SupplementOrderEditDto dto) {
        return CommonResult.success(supplementOrderBizService.editSupplementOrder(dto));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@NotNull @Validated @RequestBody SupplementOrderVersionDto dto) {
        return CommonResult.success(supplementOrderBizService.update(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/examine")
    public CommonResult<Boolean> examine(@NotNull @Validated @RequestBody SupplementOrderUpdateDto dto) {
        return CommonResult.success(supplementOrderBizService.examine(dto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@NotNull @Validated @RequestBody SupplementOrderVersionDto dto) {
        return CommonResult.success(supplementOrderBizService.delete(dto));
    }

    @ApiOperation("获取采购单据信息下拉列表")
    @PostMapping("/getPurchaseDropDown")
    public CommonResult<ResultList<PurchaseDropDownVo>> getPurchaseDropDown(@NotNull @Validated @RequestBody PurchaseDropDownDto dto) {
        return CommonResult.successForList(supplementOrderBizService.getPurchaseDropDown(dto));
    }

    @ApiOperation("获取SKU信息下拉列表")
    @PostMapping("/getSkuDropDown")
    public CommonResult<ResultList<SkuDropDownVo>> getSkuDropDown(@NotNull @Validated @RequestBody SupplementSkuDropDownDto dto) {
        return CommonResult.successForList(supplementOrderBizService.getSkuDropDown(dto));
    }

    @ApiOperation("补款单导出列表")
    @PostMapping("/getExportList")
    public CommonPageResult<SupplementOrderExportVo> getExportList(@NotNull @Validated @RequestBody SupplementOrderQueryByApiDto dto) {
        return new CommonPageResult<>(supplementOrderBizService.getExportList(dto));
    }

    @ApiOperation("获取结算单下拉列表")
    @PostMapping("/getSettleOrderDropDown")
    public CommonResult<ResultList<SupplementOrderSettleDropDownVo>> getSettleOrderDropDown(@NotNull @Validated @RequestBody SupplementOrderSettleDropDownDto dto) {
        return CommonResult.successForList(supplementOrderBizService.getSettleOrderDropDown(dto));
    }

    @ApiOperation("获取个人补扣款的记录")
    @PostMapping("/getSupplementDeductOrderList")
    public CommonResult<ResultList<SupplementDeductOrderListVo>> getSupplementDeductOrderList() {
        return CommonResult.successForList(supplementOrderBizService.getSupplementDeductOrderList());
    }

    @ApiOperation("明细导出")
    @PostMapping("/exportSku")
    public CommonResult<Void> exportSku(@NotNull @Validated @RequestBody SupplementOrderDto dto) {
        supplementOrderBizService.exportSku(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量获取单据结算金额")
    @PostMapping("/getBusinessSettle")
    public CommonResult<ResultList<DeductSupplementBusinessSettleVo>> getBusinessSettle(@NotNull @Validated @RequestBody SupplementBusinessSettleDto dto) {
        return CommonResult.successForList(supplementOrderBizService.getBusinessSettle(dto));
    }


}
