package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.server.scm.entity.vo.ScheduleStatisticsVo;
import com.hete.supply.scm.server.scm.entity.vo.ScheduleTabVo;
import com.hete.supply.scm.server.scm.process.entity.dto.H5ProcessInfoDto;
import com.hete.supply.scm.server.scm.process.entity.dto.H5ProcessOrderProcedureScanDetailDto;
import com.hete.supply.scm.server.scm.process.entity.dto.H5WorkbenchPageDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderStartProcessingByH5Dto;
import com.hete.supply.scm.server.scm.process.entity.vo.H5ProcessOrderOperationScanVo;
import com.hete.supply.scm.server.scm.process.entity.vo.H5WorkbenchVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanDetailVo;
import com.hete.supply.scm.server.scm.process.service.biz.H5ProcessBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.core.holder.GlobalContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;

/**
 * @author yanjiawei
 * @date 2023年08月08日 23:59
 */
@RestController
@Api(tags = "加工单H5扫码管理")
@RequestMapping("/scm/h5/processOrderScan")
@RequiredArgsConstructor
public class H5ProcessController {
    private final H5ProcessBizService h5ProcessBizService;

    @PostMapping("/getH5ProcessList")
    @ApiOperation("获取H5页面工作台列表")
    public CommonResult<ResultList<H5ProcessOrderOperationScanVo>> getH5ProcessList() {
        return CommonResult.successForList(h5ProcessBizService.getH5ProcessList());
    }

    @PostMapping("/getH5ProcessListByUserKey")
    @ApiOperation("获取H5页面指定用户扫码记录")
    public CommonResult<ResultList<H5ProcessOrderOperationScanVo>> getH5ProcessListByUserKey(H5ProcessInfoDto h5ProcessInfoDto) {
        return CommonResult.successForList(h5ProcessBizService.getH5ProcessListByUserKey(h5ProcessInfoDto));
    }

    @PostMapping("/getH5ProcessOrderScanDetail")
    @ApiOperation("获取H5页面工作台详情")
    public CommonResult<ProcessOrderScanDetailVo> getProcessPlanDetail(@NotNull @Valid @RequestBody H5ProcessOrderProcedureScanDetailDto dto) {
        return CommonResult.success(h5ProcessBizService.getProcessPlanDetail(dto));
    }

    @PostMapping("/beginProcedure")
    @ApiOperation("开始加工")
    public CommonResult<Boolean> beginProcedure(@NotNull @Valid @RequestBody ProcessOrderStartProcessingByH5Dto dto) {
        return CommonResult.success(h5ProcessBizService.beginProcedure(dto));
    }

    @ApiOperation("获取员工排产计划tab页")
    @PostMapping("/tab")
    public CommonResult<ScheduleTabVo> getTab() {
        return CommonResult.success(h5ProcessBizService.getTab());
    }

    @ApiOperation(value = "获取员工排产计划统计项")
    @PostMapping("/statistics")
    public CommonResult<ScheduleStatisticsVo> getScheduleStatistics() {
        return CommonResult.success(h5ProcessBizService.getScheduleStatistics());
    }

    @ApiOperation(value = "获取当前用户")
    @PostMapping("/getCurrentUserKey")
    public CommonResult<ResultList<String>> getCurrentUserKey() {
        return CommonResult.successForList(Collections.singletonList(GlobalContext.getUserKey()));
    }

    @ApiOperation(value = "分页查询工作台")
    @PostMapping("/workbenchPage")
    public CommonPageResult<H5WorkbenchVo> workbenchPage(@NotNull @Valid @RequestBody H5WorkbenchPageDto dto) {
        return new CommonPageResult<>(h5ProcessBizService.getWorkbenchPage(dto));
    }
}
