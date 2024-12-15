package com.hete.supply.scm.server.scm.production.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuDto;
import com.hete.supply.scm.server.scm.production.entity.dto.*;
import com.hete.supply.scm.server.scm.production.entity.vo.*;
import com.hete.supply.scm.server.scm.production.service.biz.SkuProdBizService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDetailDto;
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
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/23 10:11
 */
@RestController
@Api(tags = "供应链商品生产信息管理")
@RequestMapping("/scm/skuProd")
@RequiredArgsConstructor
public class SkuProdController {

    private final SkuProdBizService skuProdBizService;
    private final ProduceDataBaseService produceDataBaseService;

    @ApiOperation("商品信息列表")
    @PostMapping("/searchPlmSku")
    public CommonPageResult<PlmSkuSearchVo> searchPlmSku(@NotNull @Validated @RequestBody PlmSkuSearchDto dto) {
        return new CommonPageResult<>(skuProdBizService.searchPlmSku(dto));
    }

    @ApiOperation("商品信息列表sku导出")
    @PostMapping("/exportPlmSku")
    public CommonResult<Void> exportPlmSku(@NotNull @Validated @RequestBody PlmSkuSearchDto dto) {
        skuProdBizService.exportPlmSku(dto);
        return CommonResult.success();
    }

    @ApiOperation("导出sku和原料工序信息")
    @PostMapping("/exportSkuSup")
    public CommonResult<Void> exportSkuSup(@NotNull @Validated @RequestBody PlmSkuSearchDto dto) {
        skuProdBizService.exportSkuSup(dto);
        return CommonResult.success();
    }

    @ApiOperation("商品信息列表sku导出")
    @PostMapping("/exportPlmSkuCompare")
    public CommonResult<Void> exportPlmSkuCompare(@NotNull @Validated @RequestBody PlmSkuSearchDto dto) {
        skuProdBizService.exportPlmSkuCompare(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取商品信息详情头部信息")
    @PostMapping("/getSkuTop")
    public CommonResult<SkuTopDetailVo> getSkuTop(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.success(skuProdBizService.getSkuTop(dto));
    }

    @ApiOperation("销售属性详情")
    @PostMapping("/produceDataAttrDetail")
    public CommonResult<ProduceDataAttrDetailVo> produceDataAttrDetail(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.success(skuProdBizService.produceDataAttrDetail(dto));
    }

    @ApiOperation("更新销售属性详情")
    @PostMapping("/updateProduceDataAttr")
    public CommonResult<Void> updateProduceDataAttr(@NotNull @Validated @RequestBody UpdateProduceDataAttrDto dto) {
        skuProdBizService.updateProduceDataAttr(dto);
        return CommonResult.success();
    }

    @ApiOperation("更新并拷贝销售属性详情")
    @PostMapping("/updateCopyProduceDataAttr")
    public CommonResult<Void> updateCopyProduceDataAttr(@NotNull @Validated @RequestBody UpdateProduceDataAttrDto dto) {
        skuProdBizService.updateCopyProduceDataAttr(dto);
        return CommonResult.success();
    }

    @ApiOperation("采购信息详情")
    @PostMapping("/purchaseProduceDataDetail")
    public CommonResult<PurchaseProduceDataDetailVo> purchaseProduceDataDetail(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.success(skuProdBizService.purchaseProduceDataDetail(dto));
    }

    @ApiOperation("更新采购信息")
    @PostMapping("/updatePurchaseProduceData")
    public CommonResult<Void> updatePurchaseProduceData(@NotNull @Validated @RequestBody UpdatePurchaseProduceDataDto dto) {
        skuProdBizService.updatePurchaseProduceData(dto);
        return CommonResult.success();
    }

    @ApiOperation("原料&工序信息详情")
    @PostMapping("/produceDataItemDetail")
    public CommonResult<ProduceDataItemDetailVo> produceDataItemDetail(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.success(skuProdBizService.produceDataItemDetail(dto));
    }

    @ApiOperation("更新原料&工序信息信息")
    @PostMapping("/updateProduceDataItem")
    public CommonResult<Void> updateProduceDataItem(@NotNull @Validated @RequestBody UpdateProduceDataItemDto dto) {
        skuProdBizService.updateProduceDataItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("规格书详情")
    @PostMapping("/getSpecBook")
    public CommonResult<SpecBookDetailVo> getSpecBook(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.success(skuProdBizService.getSpecBook(dto));
    }

    @ApiOperation("供应链属性详情")
    @PostMapping("/scmAttrDetail")
    public CommonResult<SkuProdDetailVo> scmAttrDetail(@NotNull @Validated @RequestBody GetSkuProductionDto dto) {
        SkuProdDetailVo skuProductionAttributeInfo = skuProdBizService.scmAttrDetail(dto);
        return CommonResult.success(skuProductionAttributeInfo);
    }

    @ApiOperation("更新供应链属性")
    @PostMapping("/updateScmAttr")
    public CommonResult<Void> updateScmAttr(@NotNull @Validated @RequestBody UpdateSkuProductionDto dto) {
        skuProdBizService.updateScmAttr(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取可维护属性列表")
    @PostMapping("/getMaintainableAttrList")
    public CommonResult<ResultList<AttributeInfoVo>> getMaintainableAttrList(@NotNull @Validated @RequestBody GetMaintainableAttrDto dto) {
        List<AttributeInfoVo> maintainableAttrVo = skuProdBizService.getMaintainableAttrList(dto);
        return CommonResult.successForList(maintainableAttrVo);
    }

    @ApiOperation("商品风险日志")
    @PostMapping("/getSkuRiskLog")
    public CommonResult<SkuRiskLogVo> getSkuRiskLog(@NotNull @Validated @RequestBody GetSkuRiskLogDto dto) {
        SkuRiskLogVo skuRiskLogVo = skuProdBizService.getSkuRiskLog(dto);
        return CommonResult.success(skuRiskLogVo);
    }

    @ApiOperation("查询商品对照关系详情")
    @PostMapping("/getSupplierProductCompareDetail")
    public CommonResult<SupplierProductCompareInfoVo> getSupplierProductCompareDetail(@NotNull @Validated @RequestBody SupplierProductCompareDetailDto dto) {
        return CommonResult.success(skuProdBizService.getSupplierProductCompareDetail(dto));
    }

    @ApiOperation("确认提交对照关系")
    @PostMapping("/editSupplierProductCompare")
    public CommonResult<Void> editSupplierProductCompare(@NotNull @Validated @RequestBody SupplierProductCompareUpdateDto dto) {
        skuProdBizService.editSupplierProductCompare(dto);
        return CommonResult.success();
    }

    @ApiOperation("初始化特殊属性")
    @PostMapping("/initProdDataAttr")
    public CommonResult<Void> initProdDataAttr(@NotNull @Validated @RequestBody SkuDto dto) {
        produceDataBaseService.initProdDataAttr(dto.getSku());
        return CommonResult.success();
    }

    @ApiOperation("刷新颜色色系属性")
    @PostMapping("/refreshColorAttr")
    public CommonResult<Void> refreshColorAttr() {
        skuProdBizService.refreshColorAttr();
        return CommonResult.success();
    }
}
