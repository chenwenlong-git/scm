package com.hete.supply.scm.server.scm.develop.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.DevelopReviewSearchDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.*;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopChildBizService;
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
 * @author weiwenxin
 * @date 2023/8/3 14:08
 */
@Validated
@RestController
@RequestMapping("/scm/develop")
@RequiredArgsConstructor
@Api(tags = "开发子单")
public class DevelopChildController {

    private final DevelopChildBizService developChildBizService;

    @ApiOperation("开发子单列表")
    @PostMapping("/searchDevelopChild")
    public CommonPageResult<DevelopChildSearchVo> searchDevelopChild(@NotNull @Validated @RequestBody DevelopChildSearchDto dto) {
        return new CommonPageResult<>(developChildBizService.searchDevelopChild(dto));
    }

    @ApiOperation("开发子单详情页")
    @PostMapping("/developChildDetail")
    public CommonResult<DevelopChildDetailVo> developChildDetail(@NotNull @Validated @RequestBody DevelopChildNoDto dto) {
        return CommonResult.success(developChildBizService.developChildDetail(dto));
    }

    @ApiOperation("编辑开发子单")
    @PostMapping("/editDevelopChild")
    public CommonResult<Void> editDevelopChild(@NotNull @Validated @RequestBody DevelopChildEditDto dto) {
        developChildBizService.editDevelopChild(dto);
        return CommonResult.success();
    }

    @ApiOperation("审版单列表")
    @PostMapping("/searchDevelopReview")
    public CommonPageResult<DevelopReviewSearchVo> searchDevelopReview(@NotNull @Validated @RequestBody DevelopReviewSearchDto dto) {
        return new CommonPageResult<>(developChildBizService.searchDevelopReview(dto));
    }

    @ApiOperation("审版单列表导出")
    @PostMapping("/getReviewExportList")
    public CommonResult<Void> getReviewExportList(@NotNull @Validated @RequestBody DevelopReviewSearchDto dto) {
        developChildBizService.getReviewExportList(dto);
        return CommonResult.success();
    }


    @ApiOperation("审版单详情页")
    @PostMapping("/developReviewDetail")
    public CommonResult<DevelopReviewDetailVo> developReviewDetail(@NotNull @Validated @RequestBody DevelopReviewNoDto dto) {
        return CommonResult.success(developChildBizService.developReviewDetail(dto));
    }

    @ApiOperation("提交审版")
    @PostMapping("/submitDevelopReview")
    public CommonResult<Void> submitDevelopReview(@NotNull @Validated @RequestBody DevelopReviewSubmitDto dto) {
        developChildBizService.submitDevelopReview(dto);
        return CommonResult.success();
    }

    @ApiOperation("开始审版")
    @PostMapping("/startDevelopReview")
    public CommonResult<Void> startDevelopReview(@NotNull @Validated @RequestBody DevelopReviewNoDto dto) {
        developChildBizService.startDevelopReview(dto);
        return CommonResult.success();
    }

    @ApiOperation("保存审版信息")
    @PostMapping("/saveDevelopReview")
    public CommonResult<Void> saveDevelopReview(@NotNull @Validated @RequestBody DevelopReviewCompleteDto dto) {
        developChildBizService.saveDevelopReview(dto);
        return CommonResult.success();
    }

    @ApiOperation("完成审版信息")
    @PostMapping("/completeDevelopReview")
    public CommonResult<Void> completeDevelopReview(@NotNull @Validated @RequestBody DevelopReviewCompleteDto dto) {
        developChildBizService.completeDevelopReview(dto);
        return CommonResult.success();
    }

    @ApiOperation("创建审版单")
    @PostMapping("/createDevelopReview")
    public CommonResult<Void> createDevelopReview(@NotNull @Validated @RequestBody DevelopReviewCreateDto dto) {
        developChildBizService.createDevelopReview(dto);
        return CommonResult.success();
    }

    @ApiOperation("一键取消(plm)")
    @PostMapping("/cancel")
    public CommonResult<Void> cancel(@NotNull @Validated @RequestBody DevelopCancelDto dto) {
        developChildBizService.cancel(dto);
        return CommonResult.success();
    }

    @ApiOperation("开发子单列表导出")
    @PostMapping("/export")
    public CommonResult<Void> export(@NotNull @Validated @RequestBody DevelopChildSearchDto dto) {
        developChildBizService.export(dto);
        return CommonResult.success();
    }

    @ApiOperation("下单接口")
    @PostMapping("/submitOrder")
    public CommonResult<Void> submitOrder(@NotNull @Validated @RequestBody DevelopChildSubmitOrderItemDto dto) {
        developChildBizService.submitOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取版单原料列表")
    @PostMapping("/getDevelopPamphletOrderRaw")
    public CommonResult<ResultList<DevelopPamphletRawDetailVo>> getDevelopPamphletOrderRaw(@NotNull @Validated @RequestBody DevelopPamphletOrderRawDto dto) {
        return CommonResult.successForList(developChildBizService.getDevelopPamphletOrderRaw(dto));
    }

    @ApiOperation("原料下单提交")
    @PostMapping("/supplyRaw")
    public CommonResult<Void> supplyRaw(@NotNull @Validated @RequestBody DevelopPamphletOrderRawDeliveryDto dto) {
        developChildBizService.supplyRaw(dto);
        return CommonResult.success();
    }

    @ApiOperation("异常处理")
    @PostMapping("/exceptional")
    public CommonResult<Void> exceptional(@NotNull @Validated @RequestBody DevelopChildExceptionalDto dto) {
        developChildBizService.exceptional(dto);
        return CommonResult.success();
    }

    @ApiOperation("取消开发")
    @PostMapping("/cancelChild")
    public CommonResult<Void> cancelChild(@NotNull @Validated @RequestBody DevelopChildCancelDto dto) {
        developChildBizService.cancelChild(dto);
        return CommonResult.success();
    }

    @ApiOperation("齐备信息")
    @PostMapping("/completeInfo")
    public CommonResult<Void> completeInfo(@NotNull @Validated @RequestBody DevelopChildCompleteInfoDto dto) {
        developChildBizService.completeInfo(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取开发子单状态栏信息")
    @PostMapping("/developChildOrderStatus")
    public CommonResult<DevelopChildOrderStatusListVo> developChildOrderStatus() {
        return CommonResult.success(developChildBizService.developChildOrderStatus());
    }

    @ApiOperation("创建异常报告")
    @PostMapping("/createReviewUnusualReport")
    public CommonResult<Void> createReviewUnusualReport(@NotNull @Validated @RequestBody DevelopReviewUnusualDto dto) {
        developChildBizService.createReviewUnusualReport(dto);
        return CommonResult.success();
    }

    @ApiOperation("异常报告详情")
    @PostMapping("/reviewUnusualReportDetail")
    public CommonResult<DevelopReviewUnusualVo> reviewUnusualReportDetail(@NotNull @Validated @RequestBody DevelopSampleNoDto dto) {
        return CommonResult.success(developChildBizService.reviewUnusualReportDetail(dto));
    }

    @ApiOperation("通过核价单获取样品单列表")
    @PostMapping("/getDevelopSampleOrderList")
    public CommonResult<ResultList<DevelopSampleOrderListVo>> getDevelopSampleOrderList(@NotNull @Validated @RequestBody DevelopPricingOrderNoListDto dto) {
        return CommonResult.successForList(developChildBizService.getDevelopSampleOrderList(dto));
    }

    @ApiOperation("编辑要求打版完成时间")
    @PostMapping("/editExpectedOnShelvesDate")
    public CommonResult<Void> editExpectedOnShelvesDate(@NotNull @Validated @RequestBody DevelopExpectedOnShelvesDate dto) {
        developChildBizService.editExpectedOnShelvesDate(dto);
        return CommonResult.success();
    }

    @ApiOperation("增加产前样的样品单")
    @PostMapping("/addPrenatalSampleOrder")
    public CommonResult<Void> addPrenatalSampleOrder(@NotNull @Validated @RequestBody DevelopAddPrenatalSampleOrderDto dto) {
        developChildBizService.addPrenatalSampleOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("下单获取SPU的全部SKU列表")
    @PostMapping("/getSpuAllSkuList")
    public CommonResult<ResultList<DevelopPlaceOrderVo>> getSpuAllSkuList(@NotNull @Validated @RequestBody DevelopPlaceOrderDto dto) {
        return CommonResult.successForList(developChildBizService.getSpuAllSkuList(dto));
    }

}
