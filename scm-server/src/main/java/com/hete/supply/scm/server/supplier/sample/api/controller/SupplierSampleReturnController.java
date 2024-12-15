package com.hete.supply.scm.server.supplier.sample.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SampleReturnDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnConfirmDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnNoDto;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnDetailVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnVo;
import com.hete.supply.scm.server.supplier.sample.service.biz.SampleReturnBizService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
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
 * @date 2022/11/3 09:37
 */
@Validated
@RestController
@RequestMapping("/supplier/sample/return")
@RequiredArgsConstructor
@Api(tags = "供应商--样品退货管理")
public class SupplierSampleReturnController {
    private final SampleReturnBizService sampleReturnBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("样品退货管理列表")
    @PostMapping("/sampleReturnList")
    public CommonPageResult<SampleReturnVo> searchProductPurchase(@NotNull @Validated @RequestBody SampleReturnDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        return new CommonPageResult<>(sampleReturnBizService.searchProductPurchase(dto));
    }

    @ApiOperation("样品退货管理详情")
    @PostMapping("/sampleReturnDetail")
    public CommonResult<SampleReturnDetailVo> sampleReturnDetail(@NotNull @Validated @RequestBody SampleReturnNoDto dto) {
        return CommonResult.success(sampleReturnBizService.sampleReturnDetail(dto));
    }

    @ApiOperation("确认收货")
    @PostMapping("/sampleReturnConfirm")
    public CommonResult<Void> sampleReturnConfirm(@NotNull @Validated @RequestBody SampleReturnConfirmDto dto) {
        sampleReturnBizService.sampleReturnConfirm(dto);
        return CommonResult.success();
    }

}
