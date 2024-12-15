package com.hete.supply.scm.server.scm.settle.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettlePayAddDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettlePayDelDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleSearchVo;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopSampleSettleBizService;
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
 * @date 2023/08/1 13:39
 */
@Validated
@RestController
@RequestMapping("/scm/developSampleSettleOrder")
@RequiredArgsConstructor
@Api(tags = "样品结算单")
public class DevelopSampleSettleOrderController {
    private final DevelopSampleSettleBizService service;

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

    @ApiOperation("提交支付信息")
    @PostMapping("/addDevelopSampleSettleOrderPay")
    public CommonResult<Void> addDevelopSampleSettleOrderPay(@NotNull @Validated @RequestBody DevelopSampleSettlePayAddDto dto) {
        service.addDevelopSampleSettleOrderPay(dto);
        return CommonResult.success();
    }

    @ApiOperation("删除支付信息")
    @PostMapping("/delDevelopSampleSettleOrderPay")
    public CommonResult<Void> delDevelopSampleSettleOrderPay(@NotNull @Validated @RequestBody DevelopSampleSettlePayDelDto dto) {
        service.delDevelopSampleSettleOrderPay(dto);
        return CommonResult.success();
    }

    @ApiOperation("样品结算单导出")
    @PostMapping("/export")
    public CommonResult<Void> export(@NotNull @Validated @RequestBody DevelopSampleSettleSearchDto dto) {
        service.export(dto);
        return CommonResult.success();
    }

}
