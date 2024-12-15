package com.hete.supply.scm.server.scm.develop.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DevelopPricingOrderSearchDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPricingOrderDetailDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPricingOrderSubmitDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPricingOrderSubmitPricingDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingOrderDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingOrderSearchVo;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopPricingOrderBizService;
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
 * @date 2023/8/2 17:42
 */
@Validated
@RestController
@RequestMapping("/scm/developPricingOrder")
@RequiredArgsConstructor
@Api(tags = "核价单")
public class DevelopPricingOrderController {

    private final DevelopPricingOrderBizService developPricingOrderBizService;

    @ApiOperation("查询核价单列表")
    @PostMapping("/search")
    public CommonPageResult<DevelopPricingOrderSearchVo> search(@NotNull @Validated @RequestBody DevelopPricingOrderSearchDto dto) {
        return new CommonPageResult<>(developPricingOrderBizService.search(dto));
    }

    @ApiOperation("查询核价单详情")
    @PostMapping("/detail")
    public CommonResult<DevelopPricingOrderDetailVo> detail(@NotNull @Validated @RequestBody DevelopPricingOrderDetailDto dto) {
        return CommonResult.success(developPricingOrderBizService.detail(dto));
    }

    @ApiOperation("核价单列表导出")
    @PostMapping("/export")
    public CommonResult<Void> export(@NotNull @Validated @RequestBody DevelopPricingOrderSearchDto dto) {
        developPricingOrderBizService.export(dto);
        return CommonResult.success();
    }

    @ApiOperation("核价页面确认核价")
    @PostMapping("/submitPricing")
    public CommonResult<Void> submitPricing(@NotNull @Validated @RequestBody DevelopPricingOrderSubmitPricingDto dto) {
        developPricingOrderBizService.submitPricing(dto);
        return CommonResult.success();
    }

    @ApiOperation("提交核价")
    @PostMapping("/submit")
    public CommonResult<Void> submit(@NotNull @Validated @RequestBody DevelopPricingOrderSubmitDto dto) {
        developPricingOrderBizService.submit(dto);
        return CommonResult.success();
    }

}
