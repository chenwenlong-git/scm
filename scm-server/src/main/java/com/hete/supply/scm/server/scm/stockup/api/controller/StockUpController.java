package com.hete.supply.scm.server.scm.stockup.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.StockUpSearchDto;
import com.hete.supply.scm.server.scm.stockup.entity.dto.StockIdAndVersionDto;
import com.hete.supply.scm.server.scm.stockup.entity.dto.StockUpCreateDto;
import com.hete.supply.scm.server.scm.stockup.entity.dto.StockUpFollowConfirmDto;
import com.hete.supply.scm.server.scm.stockup.entity.vo.StockUpSearchVo;
import com.hete.supply.scm.server.scm.stockup.service.biz.StockUpBizService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
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
 * @author weiwenxin
 * @date 2024/1/9 19:40
 */
@Validated
@RestController
@RequestMapping("/scm/stockup")
@RequiredArgsConstructor
@Api(tags = "备货管理")
public class StockUpController {
    private final StockUpBizService stockUpBizService;


    @ApiOperation("备货列表")
    @PostMapping("/searchStockUp")
    public CommonPageResult<StockUpSearchVo> searchStockUp(@NotNull @Validated @RequestBody StockUpSearchDto dto) {

        return new CommonPageResult<>(stockUpBizService.searchStockUp(dto));
    }

    @ApiOperation("创建备货单")
    @PostMapping("/createStockUp")
    public CommonResult<Void> createStockUp(@NotNull @Validated @RequestBody StockUpCreateDto dto) {
        stockUpBizService.createStockUp(dto);
        return CommonResult.success();
    }

    @ApiOperation("跟单确认")
    @PostMapping("/followConfirm")
    public CommonResult<Void> followConfirm(@NotNull @Validated @RequestBody StockUpFollowConfirmDto dto) {
        stockUpBizService.followConfirm(dto);
        return CommonResult.success();
    }

    @ApiOperation("完结备货单")
    @PostMapping("/finishStockUp")
    public CommonResult<Void> finishStockUp(@NotNull @Validated @RequestBody StockIdAndVersionDto dto) {
        stockUpBizService.finishStockUp(dto);
        return CommonResult.success();
    }

    @ApiOperation("取消备货单")
    @PostMapping("/cancelStockUp")
    public CommonResult<Void> cancelStockUp(@NotNull @Validated @RequestBody StockIdAndVersionDto dto) {
        stockUpBizService.cancelStockUp(dto);
        return CommonResult.success();
    }

    @ApiOperation("备货单导出")
    @PostMapping("/exportStockUp")
    public CommonResult<Void> exportStockUp(@NotNull @Validated @RequestBody StockUpSearchDto dto) {
        stockUpBizService.exportStockUp(dto, FileOperateBizType.SCM_STOCK_UP_EXPORT);
        return CommonResult.success();
    }

    @ApiOperation("备货单明细导出")
    @PostMapping("/exportStockUpItem")
    public CommonResult<Void> exportStockUpItem(@NotNull @Validated @RequestBody StockUpSearchDto dto) {
        stockUpBizService.exportStockUpItem(dto, FileOperateBizType.SCM_STOCK_UP_ITEM_EXPORT);
        return CommonResult.success();
    }
}
