package com.hete.supply.scm.server.scm.ibfs.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PrepaymentSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.config.ScmFinanceProp;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.*;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.*;
import com.hete.supply.scm.server.scm.ibfs.service.biz.PrepaymentBizService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.core.holder.GlobalContext;
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
 * @date 2024/5/10 16:52
 */
@Validated
@RestController
@RequestMapping("/scm/prepayment")
@RequiredArgsConstructor
@Api(tags = "预付款单")
public class PrepaymentController {
    private final PrepaymentBizService prepaymentBizService;
    private final SupplierBaseService supplierBaseService;
    private final ScmFinanceProp scmFinanceProp;

    @ApiOperation("获取供应商列表")
    @PostMapping("/getSupplierList")
    public CommonResult<ResultList<SupplierVo>> getSupplierList(@NotNull @Validated @RequestBody PrepaymentSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(supplierBaseService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }

        return CommonResult.successForList(prepaymentBizService.getSupplierList(dto));
    }

    @ApiOperation("我的待办列表")
    @PostMapping("/toDoList")
    public CommonPageResult<PrepaymentSearchVo> toDoList(@NotNull @Validated @RequestBody PrepaymentSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(supplierBaseService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }

        dto.setPrepaymentOrderStatusList(PrepaymentOrderStatus.todoListStatus());
        dto.setApproveUser(currentUser);
        return new CommonPageResult<>(prepaymentBizService.searchPrepayment(dto));
    }

    @ApiOperation("预付款单列表")
    @PostMapping("/searchPrepayment")
    public CommonPageResult<PrepaymentSearchVo> searchPrepayment(@NotNull @Validated @RequestBody PrepaymentSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(supplierBaseService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }
        return new CommonPageResult<>(prepaymentBizService.searchPrepayment(dto));
    }

    @ApiOperation("预付款单导出")
    @PostMapping("/exportPrepaymentOrder")
    public CommonResult<Void> exportPrepaymentOrder(@NotNull @Validated @RequestBody PrepaymentSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(supplierBaseService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }
        prepaymentBizService.exportPrepaymentOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("新建预付款单")
    @PostMapping("/addPrepayment")
    public CommonResult<Void> addPrepayment(@NotNull @Validated @RequestBody PrepaymentAddDto dto) {
        prepaymentBizService.addPrepayment(dto);
        return CommonResult.success();
    }

    @ApiOperation("提交审批")
    @PostMapping("/submitForApproval")
    public CommonResult<Void> submitForApproval(@NotNull @Validated @RequestBody PrepaymentIdAndVersionDto dto) {
        prepaymentBizService.submitForApproval(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取本月货款")
    @PostMapping("/getMonthGoodsPayment")
    public CommonResult<MonthGoodsPaymentVo> getMonthGoodsPayment(@NotNull @Validated @RequestBody SupplierCodeDto dto) {
        return CommonResult.success(prepaymentBizService.getMonthGoodsPayment(dto));
    }

    @ApiOperation("批量转交预付款单")
    @PostMapping("/batchTransferPrepayment")
    public CommonResult<Void> batchTransferPrepayment(@NotNull @Validated @RequestBody PrepaymentTransferDto dto) {
        prepaymentBizService.batchTransferPrepayment(dto);
        return CommonResult.success();
    }

    @ApiOperation("预付款单详情页")
    @PostMapping("/prepaymentDetail")
    public CommonResult<PrepaymentDetailVo> prepaymentDetail(@NotNull @Validated @RequestBody PrepaymentNoDto dto) {
        return CommonResult.success(prepaymentBizService.prepaymentDetail(dto));
    }

    @ApiOperation("作废预付款单")
    @PostMapping("/cancelPrepayment")
    public CommonResult<Void> cancelPrepayment(@NotNull @Validated @RequestBody PrepaymentIdAndVersionDto dto) {
        prepaymentBizService.cancelPrepayment(dto);
        return CommonResult.success();
    }

    @ApiOperation("新增收款账户")
    @PostMapping("/addPrepaymentReceive")
    public CommonResult<Void> addPrepaymentReceive(@NotNull @Validated @RequestBody PrepaymentReceiveDto dto) {
        prepaymentBizService.addPrepaymentReceive(dto);
        return CommonResult.success();
    }

    @ApiOperation("新建付款记录")
    @PostMapping("/addPayment")
    public CommonResult<Void> addPayment(@NotNull @Validated @RequestBody PaymentAddDto dto) {
        prepaymentBizService.addPayment(dto);
        return CommonResult.success();
    }

    @ApiOperation("预付款财务风险信息")
    @PostMapping("/prepaymentRiskMsg")
    public CommonResult<PrepaymentRiskMsgVo> prepaymentRiskMsg(@NotNull @Validated @RequestBody PrepaymentNoDto dto) {
        return CommonResult.success(prepaymentBizService.prepaymentRiskMsg(dto));
    }

    @ApiOperation("审批同意")
    @PostMapping("/approveWorkFlow")
    public CommonResult<Void> approveWorkFlow(@NotNull @Validated @RequestBody PrepaymentApproveDto dto) {
        prepaymentBizService.approveWorkFlow(dto);
        return CommonResult.success();
    }

    @ApiOperation("审批拒绝")
    @PostMapping("/rejectWorkFlow")
    public CommonResult<Void> rejectWorkFlow(@NotNull @Validated @RequestBody PrepaymentApproveDto dto) {
        prepaymentBizService.rejectWorkFlow(dto);
        return CommonResult.success();
    }

    @ApiOperation("付款完成")
    @PostMapping("/fullPayment")
    public CommonResult<Void> fullPayment(@NotNull @Validated @RequestBody PrepaymentFullPaymentDto dto) {
        prepaymentBizService.fullPayment(dto);
        return CommonResult.success();
    }

}
