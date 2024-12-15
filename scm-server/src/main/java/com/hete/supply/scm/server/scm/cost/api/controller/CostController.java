package com.hete.supply.scm.server.scm.cost.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.CostSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsCostDto;
import com.hete.supply.scm.api.scm.entity.vo.CostSkuItemVo;
import com.hete.supply.scm.server.scm.cost.entity.dto.CostCalRawPriceDto;
import com.hete.supply.scm.server.scm.cost.entity.dto.CostOfGoodsIdDto;
import com.hete.supply.scm.server.scm.cost.entity.dto.CostOfGoodsLogsPageDto;
import com.hete.supply.scm.server.scm.cost.entity.vo.CostCalRawPriceVo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostDetailVo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostLogItemVo;
import com.hete.supply.scm.server.scm.cost.entity.vo.GoodsCostVo;
import com.hete.supply.scm.server.scm.cost.service.base.CostBaseService;
import com.hete.supply.scm.server.scm.cost.service.biz.CostBizService;
import com.hete.supply.scm.server.scm.cost.service.biz.CostInitBizService;
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
 * @date 2024/2/20 11:19
 */
@Validated
@RestController
@RequestMapping("/scm/cost")
@RequiredArgsConstructor
@Api(tags = "成本管理")
public class CostController {
    private final CostBizService costBizService;
    private final CostInitBizService costInitBizService;
    private final CostBaseService costBaseService;


    @ApiOperation("商品成本管理列表")
    @PostMapping("/searchGoodsOfCost")
    public CommonPageResult<GoodsCostVo> searchGoodsOfCost(@NotNull @Validated @RequestBody GoodsCostDto dto) {
        return new CommonPageResult<>(costBizService.searchGoodsOfCost(dto));
    }

    @ApiOperation("商品成本详情")
    @PostMapping("/goodsOfCostDetails")
    public CommonResult<GoodsCostDetailVo> goodsOfCostDetails(@NotNull @Validated @RequestBody CostOfGoodsIdDto dto) {
        return CommonResult.success(costBizService.goodsOfCostDetails(dto));
    }

    @ApiOperation("商品成本变更记录")
    @PostMapping("/goodsOfCostDayLogs")
    public CommonPageResult<GoodsCostLogItemVo> goodsOfCostDayLogs(@NotNull @Validated @RequestBody CostOfGoodsLogsPageDto dto) {
        return new CommonPageResult<>(costBizService.goodsOfCostDayLogs(dto));
    }

    @ApiOperation("商品成本管理列表导出(单仓)")
    @PostMapping("/goodsOfCostOneExport")
    public CommonResult<Void> goodsOfCostOneExport(@NotNull @Validated @RequestBody GoodsCostDto dto) {
        costBizService.goodsOfCostOneExport(dto);
        return CommonResult.success();
    }

    @ApiOperation("商品成本管理列表导出(多仓)")
    @PostMapping("/goodsOfCostManyExport")
    public CommonResult<Void> goodsOfCostManyExport(@NotNull @Validated @RequestBody GoodsCostDto dto) {
        costBizService.goodsOfCostManyExport(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据原料计算我司原料单价")
    @PostMapping("/calculateRawPrice")
    public CommonResult<CostCalRawPriceVo> calculateRawPrice(@NotNull @Validated @RequestBody CostCalRawPriceDto dto) {
        return CommonResult.success(costBizService.calculateRawPrice(dto));
    }

    @PostMapping("/getCostBySku")
    public CommonResult<ResultList<CostSkuItemVo>> getCostBySku(@NotNull @Validated @RequestBody CostSkuDto dto) {
        return CommonResult.successForList(costBaseService.getCostBySku(dto));
    }
}
