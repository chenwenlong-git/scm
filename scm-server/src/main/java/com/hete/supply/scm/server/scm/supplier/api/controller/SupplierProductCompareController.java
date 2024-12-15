package com.hete.supply.scm.server.scm.supplier.api.controller;

import com.hete.supply.scm.server.scm.entity.dto.SkuDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDetailDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierProductCompareEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductBindingBySkuVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductCompareDetailVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierProductComparePageVo;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierProductCompareBizService;
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
 * @date 2023/3/28 14:42
 */
@Validated
@RestController
@RequestMapping("/scm/supplierProductCompare")
@RequiredArgsConstructor
@Api(tags = "商品对照关系")
public class SupplierProductCompareController {
    private final SupplierProductCompareBizService service;

    @ApiOperation("查询商品对照关系列表")
    @PostMapping("/searchSupplierProductCompare")
    @Deprecated
    public CommonPageResult<SupplierProductComparePageVo> searchSupplierProductCompare(@NotNull @Validated @RequestBody SupplierProductCompareDto dto) {
        return new CommonPageResult<>(service.searchSupplierProductCompare(dto));
    }

    @ApiOperation("查询商品对照关系详情")
    @PostMapping("/getDetail")
    @Deprecated
    public CommonResult<SupplierProductCompareDetailVo> getDetail(@NotNull @Validated @RequestBody SupplierProductCompareDetailDto dto) {
        return CommonResult.success(service.getDetail(dto));
    }

    @ApiOperation("确认提交对照关系")
    @PostMapping("/edit")
    @Deprecated
    public CommonResult<Boolean> edit(@NotNull @Validated @RequestBody SupplierProductCompareEditDto dto) {
        return CommonResult.success(service.edit(dto));
    }

    @ApiOperation("通过sku获取绑定且开启供应商列表")
    @PostMapping("/getBindingSupplierBySkuList")
    public CommonResult<ResultList<SupplierProductBindingBySkuVo>> getBindingSupplierBySkuList(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.successForList(service.getBindingSupplierBySkuList(dto));
    }

}
