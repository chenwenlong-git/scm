package com.hete.supply.scm.server.supplier.develop.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleSearchVo;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierDevelopSampleSettleBizService;
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
 * @date 2023/8/2 10:32
 */
@Validated
@RestController
@RequestMapping("/supplier/developSampleSettleOrder")
@RequiredArgsConstructor
@Api(tags = "供应商-样品结算单")
public class SupplierDevelopSampleSettleController {
    private final SupplierDevelopSampleSettleBizService service;

    @ApiOperation("查询样品结算单列表")
    @PostMapping("/search")
    public CommonPageResult<DevelopSampleSettleSearchVo> search(@NotNull @Validated @RequestBody DevelopSampleSettleSearchDto dto) {
        return new CommonPageResult<>(service.search(dto));
    }

    @ApiOperation("查询样品结算单详情")
    @PostMapping("/detail")
    public CommonResult<DevelopSampleSettleOrderDetailVo> detail(@NotNull @Validated @RequestBody DevelopSampleSettleOrderDetailDto dto) {
        return CommonResult.success(service.detail(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/examine")
    public CommonResult<Void> examine(@NotNull @Validated @RequestBody DevelopSampleSettleOrderExamineDto dto) {
        service.examine(dto);
        return CommonResult.success();
    }

    @ApiOperation("样品结算单导出")
    @PostMapping("/export")
    public CommonResult<Void> export(@NotNull @Validated @RequestBody DevelopSampleSettleSearchDto dto) {
        service.export(dto);
        return CommonResult.success();
    }

}
