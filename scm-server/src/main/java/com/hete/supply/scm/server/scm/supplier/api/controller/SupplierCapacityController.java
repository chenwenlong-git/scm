package com.hete.supply.scm.server.scm.supplier.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.ExportSupplierRestDto;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierCapacityPageVo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierCapacityBaseService;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierCapacityBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/8/6.
 */
@Validated
@RestController
@RequestMapping("/scm/supplierCapacity")
@RequiredArgsConstructor
@Api(tags = "供应商产能管理")
public class SupplierCapacityController {

    private final SupplierCapacityBizService bizService;

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public CommonPageResult<SupplierCapacityPageVo> page(@NotNull @RequestBody @Validated SupCapacityPageDto dto) {
        return new CommonPageResult<>(bizService.page(dto));
    }

    @ApiOperation("供应商产能导出")
    @PostMapping("/exportSupplierCapacity")
    public CommonResult<Void> exportSupplierCapacity(@NotNull @RequestBody @Validated SupCapacityPageDto dto) {
        bizService.exportSupplierCapacity(dto);
        return CommonResult.success();
    }

    @ApiOperation("查看假勤")
    @PostMapping("/exportSupplierRest")
    public CommonResult<Void> exportSupplierRest(@NotNull @RequestBody @Validated ExportSupplierRestDto dto) {
        bizService.exportSupplierRest(dto);
        return CommonResult.success();
    }
}
