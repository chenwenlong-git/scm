package com.hete.supply.scm.server.supplier.sample.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SampleDeliverSearchDto;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleDeliverBizService;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverIdAndVersionDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverNoListDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverOrderNoDto;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverDetailVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverPrintVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverVo;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
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
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 12:44
 */
@Validated
@RestController
@RequestMapping("/supplier/sample/deliver")
@RequiredArgsConstructor
@Api(tags = "供应商--样品发货管理")
public class SupplierSampleDeliverController {
    private final SampleDeliverBizService sampleDeliverBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("样品发货管理列表")
    @PostMapping("/searchDeliver")
    public CommonPageResult<SampleDeliverVo> searchDeliver(@NotNull @Validated @RequestBody SampleDeliverSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        return new CommonPageResult<>(sampleDeliverBizService.searchDeliver(dto));
    }

    @ApiOperation("发货管理详情")
    @PostMapping("/deliverDetail")
    public CommonResult<SampleDeliverDetailVo> deliverDetail(@NotNull @Validated @RequestBody SampleDeliverOrderNoDto dto) {

        return CommonResult.success(sampleDeliverBizService.deliverDetail(dto));
    }


    @ApiOperation("取消发货")
    @PostMapping("/cancelDeliver")
    public CommonResult<Void> cancelDeliver(@NotNull @Validated @RequestBody SampleDeliverIdAndVersionDto dto) {

        sampleDeliverBizService.cancelDeliver(dto);
        return CommonResult.success();
    }

    @ApiOperation("打印发货单")
    @PostMapping("/printDeliverOrder")
    public CommonResult<ResultList<SampleDeliverPrintVo>> printDeliverOrder(@NotNull @Validated @RequestBody SampleDeliverNoListDto dto) {

        return CommonResult.successForList(sampleDeliverBizService.printDeliverOrder(dto));
    }

}
