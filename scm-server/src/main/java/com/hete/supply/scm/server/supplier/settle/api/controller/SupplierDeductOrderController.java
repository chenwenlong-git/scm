package com.hete.supply.scm.server.supplier.settle.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderVo;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierDeductOrderBizService;
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
 * @date 2022/11/1 17:47
 */
@Validated
@RestController
@RequestMapping("/supplier/deductOrder")
@RequiredArgsConstructor
@Api(tags = "供应商-扣款单")
public class SupplierDeductOrderController {
    private final SupplierDeductOrderBizService supplierDeductOrderBizService;

    @ApiOperation("查询扣款单列表")
    @PostMapping("/searchDeductOrder")
    public CommonPageResult<DeductOrderVo> searchDeductOrder(@NotNull @Validated @RequestBody DeductOrderDto dto) {
        return new CommonPageResult<>(supplierDeductOrderBizService.searchDeductOrder(dto));
    }

    @ApiOperation("查询扣款单详情")
    @PostMapping("/getDeductOrderDetail")
    public CommonResult<DeductOrderDetailVo> getDeductOrderDetail(@NotNull @Validated @RequestBody DeductOrderDetailDto dto) {
        return CommonResult.success(supplierDeductOrderBizService.getDeductOrderDetail(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/examine")
    public CommonResult<Boolean> examine(@NotNull @Validated @RequestBody DeductOrderUpdateDto dto) {
        return CommonResult.success(supplierDeductOrderBizService.examine(dto));
    }

    @ApiOperation("明细导出")
    @PostMapping("/exportSku")
    public CommonResult<Void> exportSku(@NotNull @Validated @RequestBody DeductOrderDto dto) {
        supplierDeductOrderBizService.exportSku(dto);
        return CommonResult.success();
    }

}
