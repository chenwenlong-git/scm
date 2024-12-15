package com.hete.supply.scm.server.scm.adjust.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.GoodsPriceDto;
import com.hete.supply.scm.server.scm.adjust.entity.dto.*;
import com.hete.supply.scm.server.scm.adjust.entity.vo.*;
import com.hete.supply.scm.server.scm.adjust.service.biz.GoodsPriceBizService;
import com.hete.supply.scm.server.scm.entity.dto.SkuDto;
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
 * @date 2024/6/18 15:28
 */
@Validated
@RestController
@RequestMapping("/scm/goodsPrice")
@RequiredArgsConstructor
@Api(tags = "商品调价")
public class GoodsPriceController {

    private final GoodsPriceBizService goodsPriceBizService;

    @ApiOperation("商品价格管理列表")
    @PostMapping("/searchGoodsPrice")
    public CommonPageResult<GoodsPriceSearchVo> searchGoodsPrice(@NotNull @Validated @RequestBody GoodsPriceDto dto) {
        return new CommonPageResult<>(goodsPriceBizService.searchGoodsPrice(dto));
    }

    @ApiOperation("商品价格管理列表导出")
    @PostMapping("/exportGoodsPrice")
    public CommonResult<Void> exportGoodsPrice(@NotNull @Validated @RequestBody GoodsPriceDto dto) {
        goodsPriceBizService.exportGoodsPrice(dto);
        return CommonResult.success();
    }

    @ApiOperation("商品价格日志列表")
    @PostMapping("/searchGoodsPriceItem")
    public CommonResult<ResultList<GoodsPriceItemVo>> searchGoodsPriceItem(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.successForList(goodsPriceBizService.searchGoodsPriceItem(dto));
    }


    @ApiOperation("批量调价")
    @PostMapping("/batchGoodsPrice")
    public CommonResult<Void> batchGoodsPrice(@NotNull @Validated @RequestBody GoodsPriceBatchDto dto) {
        goodsPriceBizService.batchGoodsPrice(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取商品价格信息")
    @PostMapping("/getGoodsPriceList")
    public CommonResult<ResultList<GoodsPriceGetListVo>> getGoodsPriceList(@NotNull @Validated @RequestBody GoodsPriceGetListDto dto) {
        return CommonResult.successForList(goodsPriceBizService.getGoodsPriceList(dto));
    }

    @ApiOperation("采购获取商品价格信息")
    @PostMapping("/getGoodsPricePurchaseList")
    public CommonResult<ResultList<GoodsPriceGetPurchaseListVo>> getGoodsPricePurchaseList(@NotNull @Validated @RequestBody GoodsPriceGetPurchaseListDto dto) {
        return CommonResult.successForList(goodsPriceBizService.getGoodsPricePurchaseList(dto));
    }


    @ApiOperation("商品价格日志管理列表")
    @PostMapping("/getGoodsPriceItemList")
    public CommonResult<ResultList<GoodsPriceItemSearchListVo>> getGoodsPriceItemList(@NotNull @Validated @RequestBody GoodsPriceItemSearchListDto dto) {
        return CommonResult.successForList(goodsPriceBizService.getGoodsPriceItemList(dto));
    }

    @ApiOperation("批量禁止商品价格")
    @PostMapping("/batchGoodsPriceItemClose")
    public CommonResult<Void> batchGoodsPriceItemClose(@NotNull @Validated @RequestBody GoodsPriceItemBatchIdDto dto) {
        goodsPriceBizService.batchGoodsPriceItemClose(dto);
        return CommonResult.success();
    }
}
