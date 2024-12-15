package com.hete.supply.scm.server.scm.sample.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleReceiptDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleReceiptNoDto;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptDetailVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptSearchVo;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleReceiptBizService;
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
 * @date 2022/11/2 22:48
 */
@Validated
@RestController
@RequestMapping("/scm/sample/receipt")
@RequiredArgsConstructor
@Api(tags = "样品收货")
public class SampleReceiptController {
    private final SampleReceiptBizService sampleReceiptBizService;

    @ApiOperation("样品收货列表")
    @PostMapping("/searchSampleReceipt")
    public CommonPageResult<SampleReceiptSearchVo> searchSampleReceipt(@NotNull @Validated @RequestBody SampleReceiptSearchDto dto) {

        return new CommonPageResult<>(sampleReceiptBizService.searchSampleReceipt(dto));
    }

    @ApiOperation("样品收货详情")
    @PostMapping("/sampleReceiptDetail")
    public CommonResult<SampleReceiptDetailVo> sampleReceiptDetail(@NotNull @Validated @RequestBody SampleReceiptNoDto dto) {

        return CommonResult.success(sampleReceiptBizService.sampleReceiptDetail(dto));
    }


    @ApiOperation("确认收货")
    @PostMapping("/confirmReceipt")
    public CommonResult<Void> confirmReceipt(@NotNull @Validated @RequestBody SampleReceiptDto dto) {
        sampleReceiptBizService.confirmReceipt(dto);
        return CommonResult.success();
    }

}
