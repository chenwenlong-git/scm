package com.hete.supply.scm.server.scm.develop.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleNoDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderNoListDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderSubmitHandleDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.SampleOrderPriceDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopAndSampleNoVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderSearchVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.SampleOrderPriceVo;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopSampleOrderBizService;
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
 * @author ChenWenLong
 * @date 2023/08/1 13:39
 */
@Validated
@RestController
@RequestMapping("/scm/developSampleOrder")
@RequiredArgsConstructor
@Api(tags = "开发样品单")
public class DevelopSampleOrderController {
    private final DevelopSampleOrderBizService developSampleOrderBizService;

    @ApiOperation("开发样品单列表")
    @PostMapping("/search")
    public CommonPageResult<DevelopSampleOrderSearchVo> search(@NotNull @Validated @RequestBody DevelopSampleOrderSearchDto dto) {
        return new CommonPageResult<>(developSampleOrderBizService.search(dto));
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    public CommonResult<Void> export(@NotNull @Validated @RequestBody DevelopSampleOrderSearchDto dto) {
        developSampleOrderBizService.export(dto);
        return CommonResult.success();
    }

    @ApiOperation("确认处理")
    @PostMapping("/submitHandle")
    public CommonResult<Void> submitHandle(@NotNull @Validated @RequestBody DevelopSampleOrderSubmitHandleDto dto) {
        developSampleOrderBizService.submitHandle(dto);
        return CommonResult.success();
    }

    @ApiOperation("签收样品")
    @PostMapping("/signSample")
    public CommonResult<Void> signSample(@NotNull @Validated @RequestBody DevelopSampleOrderNoListDto dto) {
        developSampleOrderBizService.signSample(dto);
        return CommonResult.success();
    }

    @ApiOperation("寄送样品")
    @PostMapping("/sendSample")
    public CommonResult<Void> sendSample(@NotNull @Validated @RequestBody DevelopSampleOrderNoListDto dto) {
        developSampleOrderBizService.sendSample(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取样品单号")
    @PostMapping("/getSampleOrderByNo")
    public CommonResult<ResultList<DevelopAndSampleNoVo>> getSampleOrderByNo(@NotNull @Validated @RequestBody DevelopSampleNoDto dto) {
        return CommonResult.successForList(developSampleOrderBizService.getSampleOrderByNo(dto));
    }

    @ApiOperation("获取样品单大货价格和样品价格")
    @PostMapping("/getSampleOrderPrice")
    public CommonResult<ResultList<SampleOrderPriceVo>> getSampleOrderPrice(@NotNull @Validated @RequestBody SampleOrderPriceDto dto) {
        return CommonResult.successForList(developSampleOrderBizService.getSampleOrderPrice(dto));
    }

}
