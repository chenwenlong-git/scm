package com.hete.supply.scm.server.scm.supplier.api.controller;

import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWareSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWarehouseBindDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWarehouseEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierWareSearchVo;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierWarehouseBizService;
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
 * @date 2024/1/10 09:52
 */
@Validated
@RestController
@RequestMapping("/scm/warehouse")
@RequiredArgsConstructor
@Api(tags = "供应商仓库管理")
public class SupplierWarehouseController {
    private final SupplierWarehouseBizService supplierWarehouseBizService;


    @ApiOperation("供应商仓库列表")
    @PostMapping("/searchSupplierWarehouse")
    public CommonPageResult<SupplierWareSearchVo> searchSupplierWarehouse(@NotNull @Validated @RequestBody SupplierWareSearchDto dto) {
        return new CommonPageResult<>(supplierWarehouseBizService.searchSupplierWarehouse(dto));
    }

    @ApiOperation("编辑供应商仓库")
    @PostMapping("/editSupplierWarehouse")
    public CommonResult<Void> editSupplierWarehouse(@NotNull @Validated @RequestBody SupplierWarehouseEditDto dto) {
        supplierWarehouseBizService.editSupplierWarehouse(dto);
        return CommonResult.success();
    }

    @ApiOperation("绑定供应商虚拟仓库")
    @PostMapping("/bindSupplierWarehouse")
    public CommonResult<Void> bindSupplierWarehouse(@NotNull @Validated @RequestBody SupplierWarehouseBindDto dto) {
        supplierWarehouseBizService.bindSupplierWarehouse(dto);
        return CommonResult.success();
    }
}
