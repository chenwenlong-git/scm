package com.hete.supply.scm.server.supplier.develop.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopChildNoDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPamphletOrderRawDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopChildDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopChildOrderStatusListVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopChildSearchVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPamphletOrderRawDetailVo;
import com.hete.supply.scm.server.supplier.develop.entity.dto.DevelopPamphletCompleteDto;
import com.hete.supply.scm.server.supplier.develop.entity.dto.DevelopPamphletExamineDto;
import com.hete.supply.scm.server.supplier.develop.service.biz.SupplierDevelopChildBizService;
import com.hete.supply.scm.server.supplier.entity.dto.PamphletReturnRawDto;
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
 * @date 2023/8/6 10:55
 */
@Validated
@RestController
@RequestMapping("/supplier/develop")
@RequiredArgsConstructor
@Api(tags = "供应商-开发子单")
public class SupplierDevelopChildController {
    private final SupplierDevelopChildBizService service;

    @ApiOperation("开发子单列表")
    @PostMapping("/searchDevelopChild")
    public CommonPageResult<DevelopChildSearchVo> searchDevelopChild(@NotNull @Validated @RequestBody DevelopChildSearchDto dto) {
        return new CommonPageResult<>(service.searchDevelopChild(dto));
    }

    @ApiOperation("开发子单详情页")
    @PostMapping("/developChildDetail")
    public CommonResult<DevelopChildDetailVo> developChildDetail(@NotNull @Validated @RequestBody DevelopChildNoDto dto) {
        return CommonResult.success(service.developChildDetail(dto));
    }

    @ApiOperation("获取原料归还列表")
    @PostMapping("/getDevelopPamphletOrderRaw")
    public CommonResult<DevelopPamphletOrderRawDetailVo> getDevelopPamphletOrderRaw(@NotNull @Validated @RequestBody DevelopPamphletOrderRawDto dto) {
        return CommonResult.success(service.getDevelopPamphletOrderRaw(dto));
    }

    @ApiOperation("归还原料")
    @PostMapping("/returnRaw")
    public CommonResult<Void> returnRaw(@NotNull @Validated @RequestBody PamphletReturnRawDto dto) {
        service.returnRaw(dto);
        return CommonResult.success();
    }

    @ApiOperation("版单打版审批")
    @PostMapping("/pamphletExamine")
    public CommonResult<Void> pamphletExamine(@NotNull @Validated @RequestBody DevelopPamphletExamineDto dto) {
        service.pamphletExamine(dto);
        return CommonResult.success();
    }

    @ApiOperation("版单打版完成")
    @PostMapping("/pamphletComplete")
    public CommonResult<Void> pamphletComplete(@NotNull @Validated @RequestBody DevelopPamphletCompleteDto dto) {
        service.pamphletComplete(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取开发子单状态栏信息")
    @PostMapping("/developChildOrderStatus")
    public CommonResult<DevelopChildOrderStatusListVo> developChildOrderStatus() {
        return CommonResult.success(service.developChildOrderStatus());
    }


}
