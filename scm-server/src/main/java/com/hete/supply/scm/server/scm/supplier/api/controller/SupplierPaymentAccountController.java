package com.hete.supply.scm.server.scm.supplier.api.controller;

import com.hete.supply.scm.server.scm.supplier.entity.dto.*;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountDropDownVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountSearchVo;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierPaymentAccountBizService;
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
 * @date 2023/12/5 17:05
 */
@Validated
@RestController
@RequestMapping("/scm/supplierPaymentAccount")
@RequiredArgsConstructor
@Api(tags = "供应商收款账号管理")
public class SupplierPaymentAccountController {
    private final SupplierPaymentAccountBizService supplierPaymentAccountBizService;

    @ApiOperation("查询收款账号列表")
    @PostMapping("/search")
    public CommonPageResult<SupplierPaymentAccountSearchVo> search(@NotNull @Validated @RequestBody SupplierPaymentAccountSearchDto dto) {
        return new CommonPageResult<>(supplierPaymentAccountBizService.search(dto));
    }

    @ApiOperation("设置默认/取消默认")
    @PostMapping("/submitDefault")
    public CommonResult<Void> submitDefault(@NotNull @Validated @RequestBody SupplierPaymentAccountDefaultDto dto) {
        supplierPaymentAccountBizService.submitDefault(dto);
        return CommonResult.success();
    }

    @ApiOperation("提交弃用")
    @PostMapping("/submitDeprecated")
    public CommonResult<Void> submitDeprecated(@NotNull @Validated @RequestBody SupplierPaymentAccountIdAndVersionDto dto) {
        supplierPaymentAccountBizService.submitDeprecated(dto);
        return CommonResult.success();
    }

    @ApiOperation("编辑")
    @PostMapping("/edit")
    public CommonResult<Void> edit(@NotNull @Validated @RequestBody SupplierPaymentAccountEditDto dto) {
        supplierPaymentAccountBizService.edit(dto);
        return CommonResult.success();
    }

    @ApiOperation("创建")
    @PostMapping("/create")
    public CommonResult<Void> create(@NotNull @Validated @RequestBody SupplierPaymentAccountCreateDto dto) {
        supplierPaymentAccountBizService.create(dto);
        return CommonResult.success();
    }

    @ApiOperation("设置启用/禁用")
    @PostMapping("/openClose")
    public CommonResult<Void> openClose(@NotNull @Validated @RequestBody SupplierPaymentAccountOpenCloseDto dto) {
        supplierPaymentAccountBizService.openClose(dto);
        return CommonResult.success();
    }

    @ApiOperation("下拉获取供应商的生效收款账户信息")
    @PostMapping("/getSupplierPaymentAccountList")
    public CommonResult<ResultList<SupplierPaymentAccountDropDownVo>> getSupplierPaymentAccountList(@NotNull @Validated @RequestBody SpAcPyReqDto dto) {
        return CommonResult.successForList(supplierPaymentAccountBizService.getSupplierPaymentAccountList(dto));
    }
}
