package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.RepairOrderSearchDto;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBizService;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@RestController
@Api(tags = "返修单管理")
@RequestMapping("/scm/repairOrder")
@RequiredArgsConstructor
public class RepairOrderController {

    private final RepairOrderBizService bizService;

    @ApiIgnore
    @ApiOperation("创建返修单")
    @PostMapping("/create")
    public void createRepairOrder(@RequestBody CreateRepairOrderMqDto createRepairOrderMqDto) {
        createRepairOrderMqDto.validate();
        bizService.createRepairOrder(createRepairOrderMqDto);
    }

    @ApiIgnore
    @ApiOperation("库存匹配定时任务")
    @PostMapping("/repairOrderStockMatchingJob")
    public void repairOrderStockMatchingJob() {
        bizService.repairOrderStockMatchingJob();
    }

    @ApiOperation("返修单列表")
    @PostMapping("/searchRepairOrder")
    public CommonPageResult<RepairOrderSearchVo> searchRepairOrder(@NotNull @Validated @RequestBody RepairOrderSearchDto dto) {
        return new CommonPageResult<>(bizService.searchRepairOrder(dto));
    }

    @ApiOperation("返修单详情")
    @PostMapping("/repairOrderDetail")
    public CommonResult<RepairOrderDetailVo> repairOrderDetail(@NotNull @Validated @RequestBody RepairOrderNoDto dto) {
        return CommonResult.success(bizService.repairOrderDetail(dto));
    }

    @ApiOperation("导出返修单")
    @PostMapping("/exportRepairOrder")
    public CommonResult<Void> exportRepairOrder(@NotNull @Validated @RequestBody RepairOrderSearchDto dto) {
        bizService.exportRepairOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("导出返修记录")
    @PostMapping("/exportRepairOrderResult")
    public CommonResult<Void> exportRepairOrderResult(@NotNull @Validated @RequestBody RepairOrderSearchDto dto) {
        bizService.exportRepairOrderResult(dto);
        return CommonResult.success();
    }

    @PostMapping("/submitDetailResult")
    @ApiOperation("提交返修结果")
    public CommonResult<Void> submitRepairDetailResult(@NotNull @Validated @RequestBody RepairDetailResultRequestDto request) {
        // 调用服务层处理业务逻辑
        bizService.submitRepairDetailResult(request);
        return CommonResult.success();
    }

    @PostMapping("/completeProcessing")
    @ApiOperation("完成返修单加工")
    public CommonResult<Void> completeProcessing(@NotNull @Validated @RequestBody CompletionRequestDto request) {
        bizService.completeProcessing(request);
        return CommonResult.success();
    }

    @PostMapping("/submitInspectionResult")
    @ApiOperation("提交返修单质检结果")
    public CommonResult<Void> submitInspectionResult(@NotNull @Validated @RequestBody InspectionResultRequestDto request) {
        bizService.submitInspectionResult(request);
        return CommonResult.success();
    }

    @PostMapping("/getRepairOrderPrintInfo")
    @ApiOperation("获取返修单打印信息")
    public CommonResult<ResultList<RepairOrderPrintResultVo>> getRepairOrderPrintInfo(@NotNull @Validated @RequestBody RepairOrderPrintRequestDto request) {
        return CommonResult.successForList(bizService.getRepairOrderPrintInfo(request));
    }

    @ApiOperation("获取列表打印批次码")
    @PostMapping("/getRepairOrderPrintBatchCode")
    public CommonPageResult<RepairOrderPrintBatchCodeVo> getRepairOrderPrintBatchCode(@NotNull @Validated @RequestBody RepairOrderNoPageDto dto) {
        return new CommonPageResult<>(bizService.getRepairOrderPrintBatchCode(dto));
    }

    @ApiOperation("获取返修单原料绑定信息列表")
    @PostMapping("/getRepairMaterialBindingInfo")
    public CommonResult<ResultList<RepairOrderPrintMaterialBatchCodeVo>> getRepairMaterialBindingInfo(@NotNull @Validated @RequestBody RepairMaterialBatchCodeRequestDto request) {
        return CommonResult.successForList(bizService.getRepairMaterialBindingInfo(request));
    }

    @ApiOperation("获取返修单原料归还信息列表")
    @PostMapping("/getReturnMaterialInfo")
    public CommonResult<RepairOrderReturnMaterialInfoVo> getReturnMaterialInfo(@NotNull @Validated @RequestBody RepairOrderReturnMaterialInfoRequestDto request) {
        return CommonResult.success(bizService.getReturnMaterialInfo(request));
    }

    @ApiOperation("提交原料归还")
    @PostMapping("/submitReturnMaterial")
    public CommonResult<Void> submitReturnMaterial(@NotNull @Validated @RequestBody SubmitReturnMaterialRequestDto request) {
        bizService.submitReturnMaterial(request);
        return CommonResult.success();
    }

    @ApiOperation("打印返修单成品收货信息列表")
    @PostMapping("/printProductReceiptInfo")
    public CommonResult<ResultList<RepairOrderPrintProductReceiptVo>> printProductReceiptInfo(@NotNull @Validated @RequestBody PrintRepairOrderProductRequestDto request) {
        return CommonResult.successForList(bizService.printProductReceiptInfo(request));
    }

    @ApiOperation("打印返修单原料归还信息列表")
    @PostMapping("/printMaterialReturnInfo")
    public CommonResult<ResultList<RepairOrderPrintMaterialReturnVo>> printMaterialReturnInfo(@NotNull @Validated @RequestBody PrintRepairOrderMaterialBackRequestDto request) {
        return CommonResult.successForList(bizService.printMaterialReturnInfo(request));
    }

}
