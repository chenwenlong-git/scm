package com.hete.supply.scm.server.supplier.supplier.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.InventoryRecordDto;
import com.hete.supply.scm.api.scm.entity.dto.SearchInventoryDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.InventoryRecordVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SearchInventoryVo;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierInventoryBizService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeDto;
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
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/8 20:09
 */
@Validated
@RestController
@RequestMapping("/supplier/supplierInventory")
@RequiredArgsConstructor
@Api(tags = "供应商库存管理")
public class SpmSupplierInventoryController {
    private final AuthBaseService authBaseService;
    private final SupplierInventoryBizService supplierInventoryBizService;


    @ApiOperation("供应商库存列表")
    @PostMapping("/searchInventory")
    public CommonPageResult<SearchInventoryVo> searchInventory(@NotNull @Validated @RequestBody SearchInventoryDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        return new CommonPageResult<>(supplierInventoryBizService.searchInventory(dto));
    }

    @ApiOperation("供应商库存记录")
    @PostMapping("/searchInventoryRecord")
    public CommonPageResult<InventoryRecordVo> searchInventoryRecord(@NotNull @Validated @RequestBody InventoryRecordDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        return new CommonPageResult<>(supplierInventoryBizService.searchInventoryRecord(dto));
    }

    @ApiOperation("库存变更")
    @PostMapping("/inventoryChange")
    public CommonResult<Void> inventoryChange(@NotNull @Validated @RequestBody InventoryChangeDto dto) {
        supplierInventoryBizService.inventoryChange(dto);
        return CommonResult.success();
    }

    @ApiOperation("供应商库存导出")
    @PostMapping("/exportSupplierInventory")
    public CommonResult<Void> exportSupplierInventory(@NotNull @Validated @RequestBody SearchInventoryDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return CommonResult.success();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        supplierInventoryBizService.exportSupplierInventory(dto, FileOperateBizType.SPM_SUPPLIER_INVENTORY_EXPORT);
        return CommonResult.success();
    }

    @ApiOperation("供应商库存记录导出")
    @PostMapping("/exportSupplierInventoryRecord")
    public CommonResult<Void> exportSupplierInventoryRecord(@NotNull @Validated @RequestBody InventoryRecordDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return CommonResult.success();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        supplierInventoryBizService.exportSupplierInventoryRecord(dto, FileOperateBizType.SPM_SUPPLIER_INVENTORY_RECORD_EXPORT);
        return CommonResult.success();
    }
}
