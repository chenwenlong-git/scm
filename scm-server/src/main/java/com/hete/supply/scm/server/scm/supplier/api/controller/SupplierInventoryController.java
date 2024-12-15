package com.hete.supply.scm.server.scm.supplier.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.ApproveInventoryChangeRecordDto;
import com.hete.supply.scm.api.scm.entity.dto.InventoryRecordDto;
import com.hete.supply.scm.api.scm.entity.dto.SearchInventoryDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierAndSkuDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.InventoryRecordVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SearchInventoryVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierSkuInventoryVo;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierInventoryBizService;
import com.hete.supply.scm.server.scm.supplier.service.init.SupplierInitService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
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
 * @date 2024/1/8 20:09
 */
@Validated
@RestController
@RequestMapping("/scm/supplierInventory")
@RequiredArgsConstructor
@Api(tags = "供应商库存管理")
public class SupplierInventoryController {

    private final SupplierInventoryBizService supplierInventoryBizService;
    private final SupplierInitService supplierInitService;


    @ApiOperation("供应商库存列表")
    @PostMapping("/searchInventory")
    public CommonPageResult<SearchInventoryVo> searchInventory(@NotNull @Validated @RequestBody SearchInventoryDto dto) {
        return new CommonPageResult<>(supplierInventoryBizService.searchInventory(dto));
    }

    @ApiOperation("供应商库存记录")
    @PostMapping("/searchInventoryRecord")
    public CommonPageResult<InventoryRecordVo> searchInventoryRecord(@NotNull @Validated @RequestBody InventoryRecordDto dto) {

        return new CommonPageResult<>(supplierInventoryBizService.searchInventoryRecord(dto));
    }

    @ApiOperation("根据供应商以及sku查询供应商库存")
    @PostMapping("/getInventoryBySupplierAndSku")
    public CommonResult<ResultList<SupplierSkuInventoryVo>> getInventoryBySupplierAndSku(@NotNull @Validated @RequestBody SupplierAndSkuDto dto) {

        return CommonResult.successForList(supplierInventoryBizService.getInventoryBySupplierAndSku(dto));
    }


    @ApiOperation("初始化供应商库存")
    @PostMapping("/initSupplierInventory")
    public CommonResult<Void> initSupplierInventory() {
        supplierInitService.initSupplierInventory();
        return CommonResult.success();
    }

    @ApiOperation("供应商库存导出")
    @PostMapping("/exportSupplierInventory")
    public CommonResult<Void> exportSupplierInventory(@NotNull @Validated @RequestBody SearchInventoryDto dto) {
        supplierInventoryBizService.exportSupplierInventory(dto, FileOperateBizType.SCM_SUPPLIER_INVENTORY_EXPORT);
        return CommonResult.success();
    }

    @ApiOperation("供应商库存记录导出")
    @PostMapping("/exportSupplierInventoryRecord")
    public CommonResult<Void> exportSupplierInventoryRecord(@NotNull @Validated @RequestBody InventoryRecordDto dto) {
        supplierInventoryBizService.exportSupplierInventoryRecord(dto, FileOperateBizType.SCM_SUPPLIER_INVENTORY_RECORD_EXPORT);
        return CommonResult.success();
    }

    @ApiOperation("审核库存变更记录")
    @PostMapping("/approveInventoryChangeRecord")
    public CommonResult<Void> approveInventoryChangeRecord(@NotNull @Validated @RequestBody ApproveInventoryChangeRecordDto dto) {
        supplierInventoryBizService.approveInventoryChangeRecord(dto);
        return CommonResult.success();
    }
}
