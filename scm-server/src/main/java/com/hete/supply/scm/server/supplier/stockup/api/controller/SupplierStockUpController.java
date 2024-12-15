package com.hete.supply.scm.server.supplier.stockup.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.StockUpSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.StockUpOrderStatus;
import com.hete.supply.scm.server.scm.stockup.entity.vo.StockUpSearchVo;
import com.hete.supply.scm.server.scm.stockup.service.biz.StockUpBizService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.scm.server.supplier.stockup.entity.dto.StockUpAcceptDto;
import com.hete.supply.scm.server.supplier.stockup.entity.dto.StockUpReturnGoodsDto;
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
import java.util.Collections;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/12 13:50
 */
@Validated
@RestController
@RequestMapping("/supplier/stockup")
@RequiredArgsConstructor
@Api(tags = "备货管理")
public class SupplierStockUpController {
    private final StockUpBizService stockUpBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("备货列表")
    @PostMapping("/searchStockUp")
    public CommonPageResult<StockUpSearchVo> searchStockUp(@NotNull @Validated @RequestBody StockUpSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setNotInStockUpOrderStatusList(Collections.singletonList(StockUpOrderStatus.TO_BE_FOLLOW_CONFIRM));
        dto.setAuthSupplierCode(supplierCodeList);
        return new CommonPageResult<>(stockUpBizService.searchStockUp(dto));
    }

    @ApiOperation("备货单接单")
    @PostMapping("/acceptStockUp")
    public CommonResult<Void> acceptStockUp(@NotNull @Validated @RequestBody StockUpAcceptDto dto) {
        stockUpBizService.acceptStockUp(dto);
        return CommonResult.success();
    }

    @ApiOperation("备货单回货确认")
    @PostMapping("/returnGoodsStockUp")
    public CommonResult<Void> returnGoodsStockUp(@NotNull @Validated @RequestBody StockUpReturnGoodsDto dto) {
        stockUpBizService.returnGoodsStockUp(dto);
        return CommonResult.success();
    }

    @ApiOperation("备货单导出导出")
    @PostMapping("/exportStockUp")
    public CommonResult<Void> exportStockUp(@NotNull @Validated @RequestBody StockUpSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return CommonResult.success();
        }
        dto.setNotInStockUpOrderStatusList(Collections.singletonList(StockUpOrderStatus.TO_BE_FOLLOW_CONFIRM));
        dto.setAuthSupplierCode(supplierCodeList);
        stockUpBizService.exportStockUp(dto, FileOperateBizType.SPM_STOCK_UP_EXPORT);
        return CommonResult.success();
    }


    @ApiOperation("备货单明细导出")
    @PostMapping("/exportStockUpItem")
    public CommonResult<Void> exportStockUpItem(@NotNull @Validated @RequestBody StockUpSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return CommonResult.success();
        }
        dto.setNotInStockUpOrderStatusList(Collections.singletonList(StockUpOrderStatus.TO_BE_FOLLOW_CONFIRM));
        dto.setAuthSupplierCode(supplierCodeList);
        stockUpBizService.exportStockUp(dto, FileOperateBizType.SPM_STOCK_UP_ITEM_EXPORT);
        return CommonResult.success();
    }
}
