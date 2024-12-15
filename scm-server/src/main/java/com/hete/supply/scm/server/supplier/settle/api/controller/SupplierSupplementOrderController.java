package com.hete.supply.scm.server.supplier.settle.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderVo;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierSupplementOrderBizService;
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
 * @date 2022/11/2 09:29
 */
@Validated
@RestController
@RequestMapping("/supplier/supplementOrder")
@RequiredArgsConstructor
@Api(tags = "供应商-补款单")
public class SupplierSupplementOrderController {
    private final SupplierSupplementOrderBizService supplierSupplementOrderBizService;

    @ApiOperation("查询补款单列表")
    @PostMapping("/searchSupplementOrder")
    public CommonPageResult<SupplementOrderVo> searchSupplementOrder(@NotNull @Validated @RequestBody SupplementOrderDto dto) {
        return new CommonPageResult<>(supplierSupplementOrderBizService.searchSupplementOrder(dto));
    }

    @ApiOperation("查询补款单详情")
    @PostMapping("/getSupplementOrderDetail")
    public CommonResult<SupplementOrderDetailVo> getSupplementOrderDetail(@NotNull @Validated @RequestBody SupplementOrderDetailDto dto) {
        return CommonResult.success(supplierSupplementOrderBizService.getSupplementOrderDetail(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/examine")
    public CommonResult<Boolean> examine(@NotNull @Validated @RequestBody SupplementOrderUpdateDto dto) {
        return CommonResult.success(supplierSupplementOrderBizService.examine(dto));
    }

    @ApiOperation("明细导出")
    @PostMapping("/exportSku")
    public CommonResult<Void> exportSku(@NotNull @Validated @RequestBody SupplementOrderDto dto) {
        supplierSupplementOrderBizService.exportSku(dto);
        return CommonResult.success();
    }

}
