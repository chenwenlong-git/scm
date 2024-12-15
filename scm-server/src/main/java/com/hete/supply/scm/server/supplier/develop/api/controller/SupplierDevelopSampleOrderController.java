package com.hete.supply.scm.server.supplier.develop.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderNoListDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderSearchVo;
import com.hete.supply.scm.server.supplier.develop.service.biz.SupplierDevelopSampleOrderBizService;
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
 * @author ChenWenLong
 * @date 2023/8/3 16:31
 */
@Validated
@RestController
@RequestMapping("/supplier/developSampleOrder")
@RequiredArgsConstructor
@Api(tags = "供应商-开发样品单")
public class SupplierDevelopSampleOrderController {
    private final SupplierDevelopSampleOrderBizService service;

    @ApiOperation("开发样品单列表")
    @PostMapping("/search")
    public CommonPageResult<DevelopSampleOrderSearchVo> search(@NotNull @Validated @RequestBody DevelopSampleOrderSearchDto dto) {
        return new CommonPageResult<>(service.search(dto));
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public CommonResult<Void> export(@NotNull @Validated @RequestBody DevelopSampleOrderSearchDto dto) {
        service.export(dto);
        return CommonResult.success();
    }

    @ApiOperation("退样签收")
    @PostMapping("/sign")
    public CommonResult<Void> sign(@NotNull @Validated @RequestBody DevelopSampleOrderNoListDto dto) {
        service.sign(dto);
        return CommonResult.success();
    }
}
