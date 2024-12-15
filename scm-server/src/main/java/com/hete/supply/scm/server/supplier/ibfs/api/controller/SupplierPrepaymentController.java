package com.hete.supply.scm.server.supplier.ibfs.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.PrepaymentSearchDto;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.PrepaymentSearchVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SupplierVo;
import com.hete.supply.scm.server.scm.ibfs.service.biz.PrepaymentBizService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.support.api.exception.ParamIllegalException;
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
 * @date 2024/5/29 15:24
 */
@Validated
@RestController
@RequestMapping("/spm/prepayment")
@RequiredArgsConstructor
@Api(tags = "预付款单")
public class SupplierPrepaymentController {
    private final AuthBaseService authBaseService;
    private final PrepaymentBizService prepaymentBizService;

    @ApiOperation("获取供应商列表（spm）")
    @PostMapping("/getSupplierList")
    public CommonResult<ResultList<SupplierVo>> getSupplierList(@NotNull @Validated @RequestBody PrepaymentSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonResult<>();
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        return CommonResult.successForList(prepaymentBizService.getSupplierList(dto));
    }

    @ApiOperation("预付款单列表（spm）")
    @PostMapping("/searchPrepayment")
    public CommonPageResult<PrepaymentSearchVo> searchPrepayment(@NotNull @Validated @RequestBody PrepaymentSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        return new CommonPageResult<>(prepaymentBizService.searchPrepayment(dto));
    }

    @ApiOperation("预付款单导出（spm）")
    @PostMapping("/exportPrepaymentOrder")
    public CommonResult<Void> exportPrepaymentOrder(@NotNull @Validated @RequestBody PrepaymentSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new ParamIllegalException("当前账号没有配置供应商数据权限，无法进行导出操作！");
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        prepaymentBizService.exportSupplierPrepaymentOrder(dto);
        return CommonResult.success();
    }
}
