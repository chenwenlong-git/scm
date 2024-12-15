package com.hete.supply.scm.server.scm.settle.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessSettleOrderDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.GetSettleOrderItemCompleteUserDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.ProcessSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.ProcessSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.ProcessSettleOrderItemDto;
import com.hete.supply.scm.server.scm.settle.entity.vo.*;
import com.hete.supply.scm.server.scm.settle.service.base.ProcessSettleOrderBaseService;
import com.hete.supply.scm.server.scm.settle.service.biz.ProcessSettleOrderBizService;
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
 * @date 2022/11/1 15:55
 */
@Validated
@RestController
@RequestMapping("/scm/processSettleOrder")
@RequiredArgsConstructor
@Api(tags = "加工结算单")
public class ProcessSettleOrderController {
    private final ProcessSettleOrderBizService processSettleOrderBizService;
    private final ProcessSettleOrderBaseService processSettleOrderBaseService;

    @ApiOperation("查询加工结算单列表")
    @PostMapping("/searchProcessSettleOrder")
    public CommonPageResult<ProcessSettleOrderVo> searchProcessSettleOrder(@NotNull @Validated @RequestBody ProcessSettleOrderDto dto) {
        return new CommonPageResult<>(processSettleOrderBizService.searchProcessSettleOrder(dto));
    }

    @ApiOperation("查询加工结算单详情")
    @PostMapping("/getProcessSettleOrderDetail")
    public CommonResult<ProcessSettleOrderDetailVo> getProcessSettleOrderDetail(@NotNull @Validated @RequestBody ProcessSettleOrderDetailDto dto) {
        return CommonResult.success(processSettleOrderBizService.getProcessSettleOrderDetail(dto));
    }

    @ApiOperation("查询工序扫码记录明细")
    @PostMapping("/getSettleProcessOrderScan")
    public CommonResult<SettleProcessOrderScanVo> getSettleProcessOrderScan(@NotNull @Validated @RequestBody ProcessSettleOrderItemDto dto) {
        return CommonResult.success(processSettleOrderBizService.getSettleProcessOrderScan(dto));
    }

    @ApiOperation("查询补款单")
    @PostMapping("/getSettleSupplementOrder")
    public CommonResult<SettleSupplementOrderVo> getSettleSupplementOrder(@NotNull @Validated @RequestBody ProcessSettleOrderItemDto dto) {
        return CommonResult.success(processSettleOrderBizService.getSettleSupplementOrder(dto));
    }

    @ApiOperation("查询扣款单")
    @PostMapping("/getSettleDeductOrder")
    public CommonResult<SettleDeductOrderVo> getSettleDeductOrder(@NotNull @Validated @RequestBody ProcessSettleOrderItemDto dto) {
        return CommonResult.success(processSettleOrderBizService.getSettleDeductOrder(dto));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@NotNull @Validated @RequestBody ProcessSettleOrderDetailDto dto) {
        return CommonResult.success(processSettleOrderBizService.update(dto));
    }

    @ApiOperation("审核")
    @PostMapping("/examine")
    public CommonResult<Boolean> examine(@NotNull @Validated @RequestBody ProcessSettleOrderExamineDto dto) {
        return CommonResult.success(processSettleOrderBizService.examine(dto));
    }

    @ApiOperation("结算单详情分页搜索")
    @PostMapping("/getSettleOrderScanDetailPage")
    public CommonPageResult<ProcessSettleOrderDetailAndScanSettleVo> getSettleOrderScanDetailPage(@Validated @RequestBody GetProcessSettleOrderDetailAndScanSettleDto dto) {
        return CommonPageResult.success(processSettleOrderBizService.getSettleOrderScanDetailPage(dto));
    }

    @ApiOperation("导出结算单明细与扫码结算明细")
    @PostMapping("/exportSettleOrderScanDetail")
    public CommonResult<Void> exportSettleOrderScanDetail(@RequestBody GetProcessSettleOrderDetailAndScanSettleDto dto) {
        processSettleOrderBizService.exportSettleOrderScanDetail(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取结算单明细所有完成人信息")
    @PostMapping("/getSettleOrderItemCompleteUsers")
    public CommonResult<SettleOrderItemCompleteUserVo> getSettleOrderItemCompleteUsers(@NotNull @Validated @RequestBody GetSettleOrderItemCompleteUserDto dto) {
        return CommonPageResult.success(processSettleOrderBizService.getSettleOrderItemCompleteUsers(dto));
    }

    @ApiOperation("创建结算明细")
    @PostMapping("/countProcessSettleOrder")
    public CommonResult<Void> countProcessSettleOrder() {
        processSettleOrderBaseService.countProcessSettleOrder(null, null, "2023-12");
        return CommonPageResult.success();
    }
}
