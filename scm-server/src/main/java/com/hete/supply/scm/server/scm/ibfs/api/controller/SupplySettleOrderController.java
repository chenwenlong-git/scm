package com.hete.supply.scm.server.scm.ibfs.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.GetSettleOrderDetailDto;
import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.FinanceSettleOrderTransferDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.GetSettlementRiskMsgDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.SettleOrderApproveDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.SupplierConfirmOrderDto;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SettleOrderDetailVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SettleOrderPageVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SettlementRiskMsgVo;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.SupplierVo;
import com.hete.supply.scm.server.scm.ibfs.service.biz.FinanceSettleOrderBizService;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.AddPaymentRecordDto;
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
 * @author yanjiawei
 * Created on 2024/5/23.
 */
@Validated
@RestController
@RequestMapping("/scm/settleOrder")
@RequiredArgsConstructor
@Api(tags = "SCM结算单")
public class SupplySettleOrderController {

    private final FinanceSettleOrderBizService bizService;

    @PostMapping("/toDoList")
    @ApiOperation("获取待办结算单列表")
    public CommonPageResult<SettleOrderPageVo> getScmPendingSettleOrders(@NotNull @RequestBody @Validated SearchSettleOrderDto searchDto) {
        return new CommonPageResult<>(bizService.getScmPendingSettleOrders(searchDto));
    }

    @ApiOperation("获取供应商列表")
    @PostMapping("/getSupplierList")
    public CommonResult<ResultList<SupplierVo>> getScmSupplierList(@NotNull @RequestBody @Validated SearchSettleOrderDto searchDto) {
        return CommonResult.successForList(bizService.getScmSupplierList(searchDto));
    }

    @PostMapping("/search")
    @ApiOperation("分页搜索结算单")
    public CommonPageResult<SettleOrderPageVo> searchSettleOrders(@NotNull @RequestBody @Validated SearchSettleOrderDto dto) {
        return new CommonPageResult<>(bizService.searchScmSupplierOrders(dto));
    }

    @PostMapping("/exportSettleOrders")
    @ApiOperation("导出结算单列表")
    public CommonResult<Void> exportSettleOrders(@NotNull @RequestBody @Validated SearchSettleOrderDto dto) {
        bizService.exportScmSettleOrders(dto);
        return CommonResult.success();
    }

    @PostMapping("/getSettleOrderDetail")
    @ApiOperation("获取结算单详情")
    public CommonResult<SettleOrderDetailVo> getSettleOrderDetail(@NotNull @RequestBody @Validated GetSettleOrderDetailDto dto) {
        SettleOrderDetailVo detailVo = bizService.getSettleOrderDetail(dto);
        return CommonResult.success(detailVo);
    }

    @PostMapping("/exportSettleOrderDetail")
    @ApiOperation("导出结算单详情")
    public CommonResult<Void> exportSettleOrderDetails(@NotNull @RequestBody @Validated SearchSettleOrderDto dto) {
        bizService.exportScmSettleOrderDetails(dto);
        return CommonResult.success();
    }

    @PostMapping("/followerConfirm")
    @ApiOperation("跟单确认")
    public CommonResult<Void> followerConfirm(@NotNull @RequestBody @Validated SupplierConfirmOrderDto dto) {
        bizService.followerConfirm(dto);
        return CommonResult.success();
    }

    @PostMapping("/addPayment")
    @ApiOperation("新增付款记录")
    public CommonResult<Void> addPaymentRecord(@NotNull @Validated @RequestBody AddPaymentRecordDto dto) {
        bizService.addPaymentRecord(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量转交结算单")
    @PostMapping("/batchTransfer")
    public CommonResult<Void> batchTransfer(@NotNull @Validated @RequestBody FinanceSettleOrderTransferDto dto) {
        String curUserKey = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        dto.setCurUserKey(curUserKey);
        bizService.batchTransfer(dto);
        return CommonResult.success();
    }

    @ApiOperation("结算单财务风险信息")
    @PostMapping("/settlementRiskMsg")
    public CommonResult<SettlementRiskMsgVo> settlementRiskMsg(@NotNull @Validated @RequestBody GetSettlementRiskMsgDto dto) {
        return CommonResult.success(bizService.settlementRiskMsg(dto));
    }

    @ApiOperation("审批同意")
    @PostMapping("/approveWorkFlow")
    public CommonResult<Void> approveWorkFlow(@NotNull @Validated @RequestBody SettleOrderApproveDto dto) {
        bizService.approveWorkFlow(dto);
        return CommonResult.success();
    }

    @ApiOperation("审批拒绝")
    @PostMapping("/rejectWorkFlow")
    public CommonResult<Void> rejectWorkFlow(@NotNull @Validated @RequestBody SettleOrderApproveDto dto) {
        bizService.rejectWorkFlow(dto);
        return CommonResult.success();
    }
}
