package com.hete.supply.scm.server.scm.qc.api.controller;

/**
 * @author weiwenxin
 * @date 2023/10/10 09:25
 */

import com.hete.supply.scm.api.scm.entity.dto.QcDto;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.QcDetailSkuVo;
import com.hete.supply.scm.server.scm.qc.entity.dto.*;
import com.hete.supply.scm.server.scm.qc.entity.vo.*;
import com.hete.supply.scm.server.scm.qc.service.biz.QcOrderBizService;
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

@Validated
@RestController
@RequestMapping("/scm/qc")
@RequiredArgsConstructor
@Api(tags = "质检")
public class QcOrderController {
    private final QcOrderBizService qcOrderBizService;

    @ApiOperation("质检列表")
    @PostMapping("/searchQc")
    public CommonPageResult<QcSearchVo> searchQc(@NotNull @Validated @RequestBody QcSearchDto dto) {
        return new CommonPageResult<>(qcOrderBizService.searchQc(dto));
    }

    @ApiOperation("获取质检打印信息")
    @PostMapping("/printQc")
    public CommonPageResult<QcSearchVo> printQc(@NotNull @Validated @RequestBody QcSearchDto dto) {

        return new CommonPageResult<>(qcOrderBizService.printQc(dto));
    }

    @ApiOperation("质检详情")
    @PostMapping("/qcDetail")
    public CommonResult<QcDetailVo> qcDetail(@NotNull @Validated @RequestBody QcOrderNoDto dto) {
        return CommonResult.success(qcOrderBizService.qcDetail(dto));
    }


    @ApiOperation("完成交接")
    @PostMapping("/completeHandover")
    public CommonResult<Void> completeHandover(@NotNull @Validated @RequestBody QcOrderNoListDto dto) {
        qcOrderBizService.completeHandover(dto);
        return CommonResult.success();
    }

    @ApiOperation("开始质检")
    @PostMapping("/startQc")
    public CommonResult<Void> startQc(@NotNull @Validated @RequestBody QcOrderNoListDto dto) {
        qcOrderBizService.startQc(dto);
        return CommonResult.success();
    }

    @ApiOperation("提交质检信息")
    @PostMapping("/submitQc")
    public CommonResult<Void> submitQc(@NotNull @Validated @RequestBody QcSubmitDto dto) {
        qcOrderBizService.submitQc(dto);
        return CommonResult.success();
    }

    @ApiOperation("整单合格/整单不合格/完成质检")
    @PostMapping("/completedQc")
    public CommonResult<Void> completedQc(@NotNull @Validated @RequestBody QcCompletedQcDto dto) {
        qcOrderBizService.completedQc(dto);
        return CommonResult.success();
    }

    @ApiOperation("质检审核")
    @PostMapping("/approve")
    public CommonResult<Void> approve(@NotNull @Validated @RequestBody QcApproveDto dto) {
        qcOrderBizService.approve(dto);
        return CommonResult.success();
    }

    @ApiOperation("重置质检")
    @PostMapping("/resetQc")
    public CommonResult<Void> resetQc(@NotNull @Validated @RequestBody QcOrderNoListDto dto) {
        qcOrderBizService.resetQc(dto);
        return CommonResult.success();
    }

    @ApiOperation("质检导出")
    @PostMapping("/exportQc")
    public CommonResult<Void> exportQcDetail(@NotNull @Validated @RequestBody QcSearchDto dto) {
        qcOrderBizService.exportQc(dto);
        return CommonResult.success();
    }

    @ApiOperation("创建采购质检单")
    @PostMapping("/createPurchaseQc")
    public CommonResult<Void> createPurchaseQc(@NotNull @Validated @RequestBody PurchaseQcCreateRequestDto purchaseQcCreateRequestDto) {
        qcOrderBizService.createPurchaseQc(purchaseQcCreateRequestDto);
        return CommonResult.success();
    }

    @ApiOperation("创建出库质检单")
    @PostMapping("/createOutBoundQc")
    public CommonResult<Void> createBizOrderQc(@NotNull @Validated @RequestBody BizNoQcCreateRequestDto dto) {
        qcOrderBizService.createOutBoundQc(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据出库单查询需要生成质检单信息")
    @PostMapping("/outBoundQcInfo")
    public CommonResult<ResultList<BizOrderCreateQcVo>> outBoundQcInfo(@NotNull @Validated @RequestBody BizOrderCreateQcReqDto dto) {
        return CommonResult.successForList(qcOrderBizService.outBoundQcInfo(dto));
    }

    @ApiOperation("查询规格书列表")
    @PostMapping("/getQcSpecBookList")
    public CommonResult<ResultList<QcSpecInfoVo>> getQcSpecBookList(@NotNull @Validated @RequestBody GetQcSpecBookDto dto) {
        return CommonResult.successForList(qcOrderBizService.getQcSpecBookList(dto));
    }

    @ApiOperation("查询质检样品信息")
    @PostMapping("/getQcSample")
    public CommonResult<ResultList<QcSampleVo>> getQcSample(@NotNull @Validated @RequestBody GetQcSampleDto dto) {
        return CommonResult.successForList(qcOrderBizService.getQcSample(dto));
    }

    @PostMapping("/listBySkuAndQcState")
    public CommonResult<ResultList<QcDetailSkuVo>> listBySkuAndQcState(QcDto dto) {
        return CommonResult.successForList(qcOrderBizService.listBySkuAndQcState(dto));
    }

}
