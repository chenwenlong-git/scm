package com.hete.supply.scm.server.scm.settle.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportVo;
import com.hete.supply.scm.server.scm.entity.dto.DeductBusinessSettleDto;
import com.hete.supply.scm.server.scm.entity.vo.DeductSupplementBusinessSettleVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuDropDownVo;
import com.hete.supply.scm.server.scm.settle.entity.dto.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderSettleDropDownVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductReturnDropDownVo;
import com.hete.supply.scm.server.scm.settle.service.biz.DeductOrderBizService;
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
 * @date 2022/11/1 17:47
 */
@Validated
@RestController
@RequestMapping("/scm/deductOrder")
@RequiredArgsConstructor
@Api(tags = "扣款单")
public class DeductOrderController {
    private final DeductOrderBizService deductOrderBizService;

    @ApiOperation("查询扣款单列表")
    @PostMapping("/searchDeductOrder")
    public CommonPageResult<DeductOrderVo> searchDeductOrder(@NotNull @Validated @RequestBody DeductOrderDto dto) {
        return new CommonPageResult<>(deductOrderBizService.searchDeductOrder(dto));
    }

    @ApiOperation("查询扣款单详情")
    @PostMapping("/getDeductOrderDetail")
    public CommonResult<DeductOrderDetailVo> getDeductOrderDetail(@NotNull @Validated @RequestBody DeductOrderDetailDto dto) {
        return CommonResult.success(deductOrderBizService.getDeductOrderDetail(dto));
    }

    @ApiOperation("添加扣款单")
    @PostMapping("/addDeductOrder")
    public CommonResult<Boolean> addDeductOrder(@NotNull @Validated @RequestBody DeductOrderAddDto dto) {
        return CommonResult.success(deductOrderBizService.addDeductOrder(dto));
    }

    @ApiOperation("编辑扣款单")
    @PostMapping("/editDeductOrder")
    public CommonResult<Boolean> editDeductOrder(@NotNull @Validated @RequestBody DeductOrderEditDto dto) {
        return CommonResult.success(deductOrderBizService.editDeductOrder(dto));
    }

    @ApiOperation("获取扣款明细单据下拉列表")
    @PostMapping("/getDeductReturnDropDown")
    public CommonResult<ResultList<DeductReturnDropDownVo>> getPurchaseDropDown(@NotNull @Validated @RequestBody DeductReturnDropDownDto dto) {
        return CommonResult.successForList(deductOrderBizService.getDeductReturnDropDown(dto));
    }

    @ApiOperation("获取SKU信息下拉列表")
    @PostMapping("/getSkuDropDown")
    public CommonResult<ResultList<SkuDropDownVo>> getSkuDropDown(@NotNull @Validated @RequestBody DeductSkuDropDownDto dto) {
        return CommonResult.successForList(deductOrderBizService.getSkuDropDown(dto));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@NotNull @Validated @RequestBody DeductOrderVersionDto dto) {
        return CommonResult.success(deductOrderBizService.update(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/examine")
    public CommonResult<Boolean> examine(@NotNull @Validated @RequestBody DeductOrderUpdateDto dto) {
        return CommonResult.success(deductOrderBizService.examine(dto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@NotNull @Validated @RequestBody DeductOrderVersionDto dto) {
        return CommonResult.success(deductOrderBizService.delete(dto));
    }

    @ApiOperation("导出列表")
    @PostMapping("/getExportList")
    public CommonPageResult<DeductOrderExportVo> getExportList(@NotNull @Validated @RequestBody DeductOrderQueryByApiDto dto) {
        return new CommonPageResult<>(deductOrderBizService.getExportList(dto));
    }

    @ApiOperation("获取结算单下拉列表")
    @PostMapping("/getSettleOrderDropDown")
    public CommonResult<ResultList<DeductOrderSettleDropDownVo>> getSettleOrderDropDown(@NotNull @Validated @RequestBody DeductOrderSettleDropDownDto dto) {
        return CommonResult.successForList(deductOrderBizService.getSettleOrderDropDown(dto));
    }

    @ApiOperation("明细导出")
    @PostMapping("/exportSku")
    public CommonResult<Void> exportSku(@NotNull @Validated @RequestBody DeductOrderDto dto) {
        deductOrderBizService.exportSku(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量获取单据结算金额")
    @PostMapping("/getBusinessSettle")
    public CommonResult<ResultList<DeductSupplementBusinessSettleVo>> getBusinessSettle(@NotNull @Validated @RequestBody DeductBusinessSettleDto dto) {
        return CommonResult.successForList(deductOrderBizService.getBusinessSettle(dto));
    }

}
