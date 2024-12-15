package com.hete.supply.scm.server.scm.sample.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.vo.SampleInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleProductVo;
import com.hete.supply.scm.server.scm.entity.dto.ReSampleDto;
import com.hete.supply.scm.server.scm.entity.dto.SampleSettleCreateDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuListDto;
import com.hete.supply.scm.server.scm.entity.vo.RawDeliverVo;
import com.hete.supply.scm.server.scm.entity.vo.RawReceiveOrderVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuDetailVo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
import com.hete.supply.scm.server.scm.sample.entity.dto.*;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.*;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleBizService;
import com.hete.supply.scm.server.supplier.entity.vo.SkuBatchCodeVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleSplitDetailVo;
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
 * @author weiwenxin
 * @date 2022/11/1
 */
@Validated
@RestController
@RequestMapping("/scm/sample")
@RequiredArgsConstructor
@Api(tags = "样品采购需求")
public class SampleController {
    private final SampleBizService sampleBizService;
    private final SampleBaseService sampleBaseService;
    private final PurchaseRawBaseService purchaseRawBaseService;


    @ApiOperation("新建样品需求页（需要打样）")
    @PostMapping("/createSample")
    public CommonResult<SampleParentOrderPo> createSample(@NotNull @Validated @RequestBody SampleCreateDto dto) {

        return CommonResult.success(sampleBizService.createSample(dto));
    }

    @ApiOperation("新建样品需求页（不需要打样）")
    @PostMapping("/createSettleSample")
    public CommonResult<SampleParentOrderPo> createSettleSample(@NotNull @Validated @RequestBody SampleSettleCreateDto dto) {

        return CommonResult.success(sampleBizService.createSettleSample(dto));
    }

    @ApiOperation("新建衍生样/自荐款样品需求")
    @PostMapping("/createSpecialSample")
    public CommonResult<SampleParentOrderPo> createSpecialSample(@NotNull @Validated @RequestBody SampleSpecialCreateDto dto) {

        return CommonResult.success(sampleBizService.createSpecialSample(dto));
    }


    @ApiOperation("样品需求列表")
    @PostMapping("/searchSample")
    public CommonPageResult<SampleSearchVo> searchSample(@NotNull @Validated @RequestBody SampleSearchDto dto) {

        return new CommonPageResult<>(sampleBizService.searchSample(dto));
    }

    @ApiOperation("样品需求详情页")
    @PostMapping("/sampleDetail")
    public CommonResult<SampleDetailVo> sampleDetail(@NotNull @Validated @RequestBody SampleParentNoDto dto) {
        return CommonResult.success(sampleBizService.sampleDetail(dto));
    }

    @ApiOperation("次品样品需求详情页")
    @PostMapping("/sampleDefectiveDetail")
    public CommonResult<SampleDetailVo> sampleDefectiveDetail(@NotNull @Validated @RequestBody SampleResultNoDto dto) {
        return CommonResult.success(sampleBizService.sampleDefectiveDetail(dto));
    }

    @ApiOperation("提交开款")
    @PostMapping("/submitDisbursement")
    public CommonResult<Void> submitDisbursement(@NotNull @Validated @RequestBody SampleParentIdAndVersionDto dto) {
        sampleBizService.submitDisbursement(dto);
        return CommonResult.success();
    }

    @ApiOperation("拆分子单")
    @PostMapping("/splitChildOrder")
    public CommonResult<ResultList<SampleChildOrderPo>> splitChildOrder(@NotNull @Validated @RequestBody SampleSplitDto dto) {
        return CommonResult.successForList(sampleBizService.splitChildOrder(dto));
    }

    @ApiOperation("下发打版")
    @PostMapping("/typeset")
    public CommonResult<Void> typeset(@NotNull @Validated @RequestBody SampleChildTypesetDto dto) {
        sampleBizService.typeset(dto);

        return CommonResult.success();
    }

    @ApiOperation("取消样品母单采购")
    @PostMapping("/cancelSampleOrder")
    public CommonResult<Void> cancelSampleOrder(@NotNull @Validated @RequestBody SampleParentIdAndVersionDto dto) {
        sampleBizService.cancelSampleOrder(dto);
        return CommonResult.success();
    }


    @ApiOperation("批量取消样品单")
    @PostMapping("/batchCancelSampleOrder")
    public CommonResult<Void> batchCancelSampleOrder(@NotNull @Validated @RequestBody SampleBatchIdDto dto) {
        sampleBizService.batchCancelSampleOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("编辑样品需求单")
    @PostMapping("/editSample")
    public CommonResult<Void> editSample(@NotNull @Validated @RequestBody SampleEditDto dto) {
        sampleBizService.editSample(dto);
        return CommonResult.success();
    }

    @ApiOperation("拆分子单页面编辑回显")
    @PostMapping("/getSplitDetail")
    public CommonResult<SampleSplitDetailVo> getSplitDetail(@NotNull @Validated @RequestBody SampleParentNoDto dto) {
        return CommonResult.success(sampleBizService.getSplitDetail(dto));
    }

    @ApiOperation("编辑子单页面")
    @PostMapping("/editChildSample")
    public CommonResult<Void> editChildSample(@NotNull @Validated @RequestBody SampleChildEditDto dto) {
        sampleBizService.editChildSample(dto);
        return CommonResult.success();
    }


    @ApiOperation("样品采购列表页")
    @PostMapping("/searchSamplePurchase")
    public CommonPageResult<SamplePurchaseSearchVo> searchSamplePurchase(@NotNull @Validated @RequestBody SamplePurchaseSearchDto dto) {
        return new CommonPageResult<>(sampleBizService.searchSamplePurchase(dto));
    }

    @ApiOperation("样品采购详情页")
    @PostMapping("/samplePurchaseDetail")
    public CommonResult<SamplePurchaseDetailVo> samplePurchaseDetail(@NotNull @Validated @RequestBody SampleChildNoDto dto) {
        return CommonResult.success(sampleBizService.samplePurchaseDetail(dto));
    }

    @ApiOperation("重新打样")
    @PostMapping("/reSample")
    public CommonResult<Void> reSample(@NotNull @Validated @RequestBody ReSampleDto dto) {
        sampleBizService.reSample(dto);
        return CommonResult.success();
    }

    @ApiOperation("强制完成")
    @PostMapping("/forceFinishSample")
    public CommonResult<Void> forceFinishSample(@NotNull @Validated @RequestBody SampleChildIdAndVersionDto dto) {
        sampleBizService.forceFinishSample(dto);
        return CommonResult.success();
    }


    @ApiOperation("样品退货")
    @PostMapping("/sampleReturnGoods")
    public CommonResult<Void> sampleReturnGoods(@NotNull @Validated @RequestBody SampleReturnCreateDto dto) {
        sampleBizService.sampleReturnGoods(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据采购子单号查询样品信息")
    @PostMapping("/getSampleProductVoByChildNo")
    public CommonResult<SampleProductVo> getSampleProductVoByChildNo(@NotNull @Validated @RequestBody SampleChildNoDto dto) {
        return CommonResult.success(sampleBaseService.getSampleProductVoByChildNo(dto.getSampleChildOrderNo()));
    }


    @ApiOperation("修改收货仓库")
    @PostMapping("/editWarehouse")
    public CommonResult<Void> editWarehouse(@NotNull @Validated @RequestBody SampleEditWarehouseDto dto) {
        sampleBizService.editWarehouse(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取次品样品单列表")
    @PostMapping("/getDefectiveSampleNoList")
    public CommonResult<ResultList<String>> getDefectiveSampleNoList(@NotNull @Validated @RequestBody SampleDefNoDto dto) {
        return CommonResult.successForList(sampleBizService.getDefectiveSampleNoList(dto));
    }

    @ApiOperation("成品入仓")
    @PostMapping("/productDeliver")
    public CommonResult<Void> productDeliver(@NotNull @Validated @RequestBody SampleProductDeliverDto dto) {
        sampleBizService.productDeliver(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据spu获取样品单信息")
    @PostMapping("/getSampleMsgBySpuList")
    public CommonResult<SampleMsgVo> getSampleMsgBySpuList(@NotNull @Validated @RequestBody SampleSpuListDto dto) {
        return CommonResult.success(sampleBizService.getSampleMsgBySpuList(dto));
    }

    @ApiOperation("根据sku列表查生产信息")
    @PostMapping("/getSampleInfoVoBySkuList")
    public CommonResult<ResultList<SampleInfoVo>> getSampleInfoBySkuList(@NotNull @Validated @RequestBody SampleSkuListDto dto) {
        return CommonResult.successForList(sampleBaseService.getSampleInfoBySkuList(dto));
    }

    @ApiOperation("母单终结状态判断")
    @PostMapping("/isSampleEndStatus")
    public CommonResult<SampleParentOrderPo> isSampleEndStatus(@NotNull @Validated @RequestBody SampleChildIdAndVersionDto dto) {
        return CommonResult.success(sampleBizService.isSampleEndStatus(dto));
    }

    @ApiOperation("作废")
    @PostMapping("/cancel")
    public CommonResult<Void> cancel(@NotNull @Validated @RequestBody SampleChildIdAndVersionDto dto) {
        sampleBizService.cancel(dto);
        return CommonResult.success();
    }

    @ApiOperation("打印批次码")
    @PostMapping("/printBatchCode")
    public CommonResult<ResultList<SkuBatchCodeVo>> printBatchCode(@NotNull @Validated @RequestBody SampleChildNoListDto dto) {

        return CommonResult.successForList(sampleBizService.printBatchCode(dto));
    }

    @ApiOperation("提供给plm根据spu查询样品选样图片")
    @PostMapping("/getSamplePicBySpu")
    public CommonPageResult<String> getSamplePicBySpu(@NotNull @Validated @RequestBody SpuDto dto) {

        return new CommonPageResult<>(sampleBizService.getSamplePicBySpu(dto));
    }


    @ApiOperation("原料出库信息")
    @PostMapping("/rawDeliveryOrder")
    public CommonResult<ResultList<RawDeliverVo>> rawDeliveryOrder(@NotNull @Validated @RequestBody SampleChildNoDto dto) {

        return CommonResult.successForList(sampleBizService.rawDeliveryOrder(dto));
    }

    @ApiOperation("原料入库信息")
    @PostMapping("/getReceiveOrder")
    public CommonResult<ResultList<RawReceiveOrderVo>> rawReceiveOrder(@NotNull @Valid @RequestBody SampleChildNoDto dto) {
        return CommonResult.successForList(purchaseRawBaseService.rawReceiveOrder(dto.getSampleChildOrderNo()));
    }

    @ApiOperation("打样成功弹窗回显")
    @PostMapping("/sampleSuccessToastMsg")
    public CommonResult<SampleToastMsgVo> sampleSuccessToastMsg(@NotNull @Valid @RequestBody SampleChildNoDto dto) {
        return CommonResult.success(sampleBizService.sampleSuccessToastMsg(dto));
    }

    @ApiOperation("选样结果管理列表")
    @PostMapping("/searchSampleChildOrderResult")
    public CommonPageResult<SampleChildOrderResultSearchVo> searchSampleChildOrderResult(@NotNull @Validated @RequestBody SampleChildOrderResultSearchDto dto) {
        return new CommonPageResult<>(sampleBizService.searchSampleChildOrderResult(dto));
    }

    @ApiOperation("样品选样")
    @PostMapping("/sampleSelection")
    public CommonResult<Void> sampleSelection(@NotNull @Validated @RequestBody SampleSelectionDto dto) {
        sampleBizService.sampleSelection(dto);
        return CommonResult.success();
    }

    @ApiOperation("编辑子单信息")
    @PostMapping("/editChildSampleOrder")
    public CommonResult<Void> editChildSampleOrder(@NotNull @Validated @RequestBody SampleChildEditDetailDto dto) {
        sampleBizService.editChildSampleOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("查询SKU的信息")
    @PostMapping("/searchSampleChildOrderSku")
    public CommonResult<ResultList<SampleChildOrderSkuVo>> searchSampleChildOrderSku(@NotNull @Validated @RequestBody SampleChildOrderSkuDto dto) {
        return CommonResult.successForList(sampleBizService.searchSampleChildOrderSku(dto));
    }

    @ApiOperation("根据sku获取产品名称与spu")
    @PostMapping("/getSkuDetailBySku")
    public CommonResult<ResultList<SkuDetailVo>> getSkuDetailBySku(@NotNull @Validated @RequestBody SkuListDto dto) {
        return CommonResult.successForList(sampleBaseService.getSkuDetailBySku(dto));
    }

    @ApiOperation("添加生产标签")
    @PostMapping("/addProduceLabel")
    public CommonResult<Void> addProduceLabel(@NotNull @Validated @RequestBody SampleAddProduceLabelDto dto) {
        sampleBizService.addProduceLabel(dto);
        return CommonResult.success();
    }
}
