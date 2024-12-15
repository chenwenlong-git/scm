package com.hete.supply.scm.server.supplier.ibfs.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderItemSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.RecoOrderItemSkuIdBatchDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.RecoOrderNoDto;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.*;
import com.hete.supply.scm.server.scm.ibfs.service.biz.RecoOrderBizService;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.SupplierConfirmRecoOrderDto;
import com.hete.supply.scm.server.supplier.ibfs.service.biz.SupplierRecoOrderBizService;
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
 * @author ChenWenLong
 * @date 2024/5/14 17:02
 */
@Validated
@RestController
@RequestMapping("/spm/recoOrder")
@RequiredArgsConstructor
@Api(tags = "供应商-财务对账单")
public class SupplierRecoOrderController {

    private final SupplierRecoOrderBizService supplierRecoOrderBizService;
    private final RecoOrderBizService recoOrderBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("对账单列表")
    @PostMapping("/searchRecoOrder")
    public CommonPageResult<RecoOrderSearchVo> searchRecoOrder(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        return new CommonPageResult<>(recoOrderBizService.searchRecoOrder(dto));
    }

    @ApiOperation("我的待办列表")
    @PostMapping("/toDoList")
    public CommonPageResult<RecoOrderSearchVo> toDoList(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        dto.setFinanceRecoOrderStatusList(FinanceRecoOrderStatus.todoSupplierListStatus());
        return new CommonPageResult<>(recoOrderBizService.searchRecoOrder(dto));
    }

    @ApiOperation("对账单导出")
    @PostMapping("/exportRecoOrder")
    public CommonResult<Void> exportRecoOrder(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new ParamIllegalException("当前账号没有配置供应商数据权限，无法进行导出操作！");
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        supplierRecoOrderBizService.exportRecoOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("对账单详情导出")
    @PostMapping("/exportRecoOrderItem")
    public CommonResult<Void> exportRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new ParamIllegalException("当前账号没有配置供应商数据权限，无法进行导出操作！");
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        supplierRecoOrderBizService.exportRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("对账单列表获取供应商")
    @PostMapping("/getRecoOrderSupplier")
    public CommonResult<ResultList<SupplierVo>> getRecoOrderSupplier(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonResult<>();
        }
        dto.setSpmAuthSupplierCode(supplierCodeList);
        return CommonResult.successForList(recoOrderBizService.getRecoOrderSupplier(dto));
    }

    @ApiOperation("对账单详情")
    @PostMapping("/recoOrderDetail")
    public CommonResult<RecoOrderDetailVo> recoOrderDetail(@NotNull @Validated @RequestBody RecoOrderNoDto dto) {
        return CommonResult.success(recoOrderBizService.recoOrderDetail(dto));
    }

    @ApiOperation("对账单详情获取详情单据列表")
    @PostMapping("/searchRecoOrderItem")
    public CommonPageResult<RecoOrderItemSearchVo> searchRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderItemSearchDto dto) {
        return new CommonPageResult<>(recoOrderBizService.searchRecoOrderItem(dto));
    }

    @ApiOperation("对账单详情获取详情单据汇总数据")
    @PostMapping("/getRecoOrderItemTotal")
    public CommonResult<RecoOrderItemTotalVo> getRecoOrderItemTotal(@NotNull @Validated @RequestBody RecoOrderItemSearchDto dto) {
        return CommonResult.success(recoOrderBizService.getRecoOrderItemTotal(dto));
    }

    @ApiOperation("批量备注")
    @PostMapping("/noteRecoOrderItem")
    public CommonResult<Void> noteRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderItemSkuIdBatchDto dto) {
        recoOrderBizService.noteRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("工厂确认")
    @PostMapping("/supplierConfirmRecoOrder")
    public CommonResult<Void> supplierConfirmRecoOrder(@NotNull @Validated @RequestBody SupplierConfirmRecoOrderDto dto) {
        supplierRecoOrderBizService.submitRecoOrder(dto);
        return CommonResult.success();
    }

}
