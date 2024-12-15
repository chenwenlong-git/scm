package com.hete.supply.scm.server.scm.ibfs.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.RecoOrderItemSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.server.scm.ibfs.config.ScmFinanceProp;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.*;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.*;
import com.hete.supply.scm.server.scm.ibfs.service.biz.RecoOrderBizService;
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
 * @author ChenWenLong
 * @date 2024/5/13 13:54
 */
@Validated
@RestController
@RequestMapping("/scm/recoOrder")
@RequiredArgsConstructor
@Api(tags = "财务对账单")
public class RecoOrderController {

    private final RecoOrderBizService recoOrderBizService;
    private final ScmFinanceProp scmFinanceProp;

    @ApiOperation("对账单列表")
    @PostMapping("/searchRecoOrder")
    public CommonPageResult<RecoOrderSearchVo> searchRecoOrder(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(recoOrderBizService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }
        return new CommonPageResult<>(recoOrderBizService.searchRecoOrder(dto));
    }

    @ApiOperation("我的待办列表")
    @PostMapping("/toDoList")
    public CommonPageResult<RecoOrderSearchVo> toDoList(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(recoOrderBizService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }
        dto.setFinanceRecoOrderStatusList(FinanceRecoOrderStatus.todoListStatus());
        dto.setApproveUser(currentUser);
        return new CommonPageResult<>(recoOrderBizService.searchRecoOrder(dto));
    }

    @ApiOperation("对账单导出")
    @PostMapping("/exportRecoOrder")
    public CommonResult<Void> exportRecoOrder(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(recoOrderBizService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }
        recoOrderBizService.exportRecoOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("对账单详情导出")
    @PostMapping("/exportRecoOrderItem")
    public CommonResult<Void> exportRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(recoOrderBizService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }
        recoOrderBizService.exportRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("对账单列表获取供应商")
    @PostMapping("/getRecoOrderSupplier")
    public CommonResult<ResultList<SupplierVo>> getRecoOrderSupplier(@NotNull @Validated @RequestBody RecoOrderSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();
        // 如果不在白名单 设置供应商权限与当前登录人
        if (!scmFinanceProp.getWhitelist().contains(currentUser)) {
            // 供应商数据权限
            dto.setAuthSupplierCode(recoOrderBizService.getSupplierCodeListByFollower(currentUser));
            dto.setCtrlUser(currentUser);
        }
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

    @ApiOperation("确认条目")
    @PostMapping("/confirmRecoOrderItem")
    public CommonResult<Void> confirmRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderItemSkuIdAndVersionDto dto) {
        recoOrderBizService.confirmRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("确认选中条目")
    @PostMapping("/confirmAllRecoOrderItem")
    public CommonResult<Void> confirmAllRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderItemSkuIdListDto dto) {
        recoOrderBizService.confirmAllRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("删除条目")
    @PostMapping("/delRecoOrderItem")
    public CommonResult<Void> delRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderDelItemSkuDto dto) {
        recoOrderBizService.delRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量备注")
    @PostMapping("/noteRecoOrderItem")
    public CommonResult<Void> noteRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderItemSkuIdBatchDto dto) {
        recoOrderBizService.noteRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("新建款项条目")
    @PostMapping("/createRecoOrderItem")
    public CommonResult<Void> createRecoOrderItem(@NotNull @Validated @RequestBody RecoOrderItemCreateDto dto) {
        recoOrderBizService.createRecoOrderItem(dto);
        return CommonResult.success();
    }

    @ApiOperation("作废")
    @PostMapping("/cancelRecoOrder")
    public CommonResult<Void> cancelRecoOrder(@NotNull @Validated @RequestBody RecoOrderNoAndVersionDto dto) {
        recoOrderBizService.cancelRecoOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量转交")
    @PostMapping("/batchTransferRecoOrder")
    public CommonResult<Void> batchTransferRecoOrder(@NotNull @Validated @RequestBody RecoOrderTransferDto dto) {
        recoOrderBizService.batchTransferRecoOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("跟单提交")
    @PostMapping("/submitRecoOrder")
    public CommonResult<Void> submitRecoOrder(@NotNull @Validated @RequestBody RecoOrderSubmitDto dto) {
        recoOrderBizService.submitRecoOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("跟单确认")
    @PostMapping("/confirmRecoOrder")
    public CommonResult<Void> confirmRecoOrder(@NotNull @Validated @RequestBody RecoOrderConfirmDto dto) {
        recoOrderBizService.confirmRecoOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取款项类型关联收单类型")
    @PostMapping("/getCollectOrderType")
    public CommonResult<ResultList<FinanceRecoFundTypeVo>> getCollectOrderType(@NotNull @Validated @RequestBody FinanceRecoFundTypeDto dto) {
        return CommonResult.successForList(recoOrderBizService.getCollectOrderType(dto));
    }

    @ApiOperation("审批同意")
    @PostMapping("/approveWorkFlow")
    public CommonResult<Void> approveWorkFlow(@NotNull @Validated @RequestBody RecoOrderApproveDto dto) {
        recoOrderBizService.approveWorkFlow(dto);
        return CommonResult.success();
    }

    @ApiOperation("审批拒绝")
    @PostMapping("/rejectWorkFlow")
    public CommonResult<Void> rejectWorkFlow(@NotNull @Validated @RequestBody RecoOrderApproveDto dto) {
        recoOrderBizService.rejectWorkFlow(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取对账单的单据详情信息")
    @PostMapping("/getRecoOrderItemSkuDetail")
    public CommonResult<RecoOrderItemSkuDetailVo> getRecoOrderItemSkuDetail(@NotNull @Validated @RequestBody RecoOrderItemSkuIdDto dto) {
        return CommonResult.success(recoOrderBizService.getRecoOrderItemSkuDetail(dto));
    }

}
