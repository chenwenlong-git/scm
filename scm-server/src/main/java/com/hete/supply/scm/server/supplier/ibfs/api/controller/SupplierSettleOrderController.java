package com.hete.supply.scm.server.supplier.ibfs.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.GetSettleOrderDetailDto;
import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.FinanceSettleOrderTransferDto;
import com.hete.supply.scm.server.scm.ibfs.entity.dto.SupplierInvalidateSettleOrderDto;
import com.hete.supply.scm.server.scm.ibfs.entity.vo.*;
import com.hete.supply.scm.server.scm.ibfs.service.biz.FinanceSettleOrderBizService;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.AddSettleOrderAccountDto;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.GenerateSettleOrderDto;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.SupplierSubmitSettleOrderDto;
import com.hete.supply.scm.server.supplier.ibfs.entity.dto.UpdateSettleOrderAccountDto;
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
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
@Validated
@RestController
@RequestMapping("/spm/settleOrder")
@RequiredArgsConstructor
@Api(tags = "SPM结算单")
public class SupplierSettleOrderController {

    private final FinanceSettleOrderBizService financeSettleOrderBizService;

    @PostMapping("/toDoList")
    @ApiOperation("获取待办结算单列表")
    public CommonPageResult<SettleOrderPageVo> getSpmPendingSettleOrders(@NotNull @RequestBody @Validated SearchSettleOrderDto searchDto) {
        return new CommonPageResult<>(financeSettleOrderBizService.getSpmPendingSettleOrders(searchDto));
    }

    @ApiOperation("获取供应商列表")
    @PostMapping("/getSupplierList")
    public CommonResult<ResultList<SupplierVo>> getSpmSupplierList(@NotNull @Validated @RequestBody SearchSettleOrderDto searchDto) {
        return CommonResult.successForList(financeSettleOrderBizService.getSpmSupplierList(searchDto));
    }

    @PostMapping("/search")
    @ApiOperation("分页搜索结算单")
    public CommonPageResult<SettleOrderPageVo> searchSettleOrders(@NotNull @Validated @RequestBody SearchSettleOrderDto searchDto) {
        return new CommonPageResult<>(financeSettleOrderBizService.searchSpmSupplierOrders(searchDto));
    }

    @PostMapping("/exportSettleOrders")
    @ApiOperation("导出结算单列表")
    public CommonResult<Void> exportSettleOrders(@NotNull @Validated @RequestBody SearchSettleOrderDto exportSettleOrderDto) {
        financeSettleOrderBizService.exportSpmSettleOrders(exportSettleOrderDto);
        return CommonResult.success();
    }

    @PostMapping("/getSettleOrderDetail")
    @ApiOperation("获取结算单详情")
    public CommonResult<SettleOrderDetailVo> getSettleOrderDetail(@NotNull @Validated @RequestBody GetSettleOrderDetailDto requestDto) {
        SettleOrderDetailVo detailVo = financeSettleOrderBizService.getSettleOrderDetail(requestDto);
        return CommonResult.success(detailVo);
    }

    @PostMapping("/exportSettleOrderDetail")
    @ApiOperation("导出结算单详情")
    public CommonResult<Void> exportSettleOrderDetails(@NotNull @Validated @RequestBody SearchSettleOrderDto dto) {
        financeSettleOrderBizService.exportSpmSettleOrderDetails(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取可结算单的供应商编码")
    @PostMapping("/generateRecoOrderSupplierCodeList")
    public CommonResult<ResultList<GenerateSettleOrderSupplierVo>> generateRecoOrderSupplierCodeList() {
        List<GenerateSettleOrderSupplierVo> generateRecoOrderSupplierCodeList
                = financeSettleOrderBizService.generateRecoOrderSupplierCodeList();
        return CommonResult.successForList(generateRecoOrderSupplierCodeList);
    }

    @ApiOperation("获取生成结算单的对账单列表")
    @PostMapping("/generateRecoOrderList")
    public CommonResult<ResultList<GenerateSettleOrderVo>> generateRecoOrderList(@NotNull @Validated @RequestBody GenerateSettleOrderDto dto) {
        List<GenerateSettleOrderVo> generateSettleOrderVos = financeSettleOrderBizService.generateRecoOrderList(dto);
        return CommonResult.successForList(generateSettleOrderVos);
    }

    @PostMapping("/createSettleOrder")
    @ApiOperation("创建结算单")
    public CommonResult<Void> createFinanceSettleOrder(@NotNull @Validated @RequestBody GenerateSettleOrderDto dto) {
        financeSettleOrderBizService.createFinanceSettleOrder(dto);
        return CommonResult.success();
    }

    @PostMapping("/submit")
    @ApiOperation("工厂提交结算单")
    public CommonResult<Void> submitSettleOrder(@NotNull @Validated @RequestBody SupplierSubmitSettleOrderDto submitSettleOrderDto) {
        financeSettleOrderBizService.submitSettleOrder(submitSettleOrderDto);
        return CommonResult.success();
    }

    @PostMapping("/invalidate")
    @ApiOperation("作废结算单")
    public CommonResult<Void> invalidateOrder(@NotNull @Validated @RequestBody SupplierInvalidateSettleOrderDto invalidateOrderDto) {
        financeSettleOrderBizService.invalidateOrder(invalidateOrderDto);
        return CommonResult.success();
    }

    @PostMapping("/addSettleOrderAccount")
    @ApiOperation("新增结算单收款账户")
    public CommonResult<Void> addSettleOrderAccount(@NotNull @Validated @RequestBody AddSettleOrderAccountDto dto) {
        financeSettleOrderBizService.addSettleOrderAccount(dto);
        return CommonResult.success();
    }

    @PostMapping("/updateSettleOrderAccount")
    @ApiOperation("更新结算单收款账户")
    public CommonResult<Void> updateSettleOrderAccount(@NotNull @Validated @RequestBody UpdateSettleOrderAccountDto dto) {
        financeSettleOrderBizService.updateSettleOrderAccount(dto);
        return CommonResult.success();
    }

    @ApiOperation("批量转交结算单")
    @PostMapping("/batchTransfer")
    public CommonResult<Void> batchTransfer(@NotNull @Validated @RequestBody FinanceSettleOrderTransferDto dto) {
        String curUserKey
                = ParamValidUtils.requireNotBlank(GlobalContext.getUserKey(), "用户信息不存在，请先登录");
        dto.setCurUserKey(curUserKey);
        financeSettleOrderBizService.batchTransfer(dto);
        return CommonResult.success();
    }
}
