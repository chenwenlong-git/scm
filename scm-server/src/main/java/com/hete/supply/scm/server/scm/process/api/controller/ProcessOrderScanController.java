package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.ProcessOrderScanQueryByApiDto;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanByH5Vo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanStatListVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanStatNumVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanVo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderScanBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:23
 */
@RestController
@Api(tags = "加工扫码管理")
@RequestMapping("/scm/processOrderScan")
@RequiredArgsConstructor
public class ProcessOrderScanController {
    private final ProcessOrderScanBizService processOrderScanBizService;

    @ApiOperation("加工扫码列表")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessOrderScanVo> getByPage(@NotNull @Valid @RequestBody ProcessOrderScanQueryDto dto) {
        return new CommonPageResult<>(processOrderScanBizService.getByPage(dto));
    }

    @PostMapping("/getByProcessOrderNo")
    @ApiOperation("通过加工单号查询扫码信息")
    public CommonResult<ProcessOrderScanByH5Vo> getByProcessOrderNo(@NotNull @Valid @RequestBody ProcessOrderScanByNoDto dto) {
        return CommonResult.success(processOrderScanBizService.getByProcessOrderNo(dto));
    }

    @PostMapping("/confirmReceive")
    @ApiOperation("确认接货")
    public CommonResult<Boolean> confirmReceive(@NotNull @Valid @RequestBody ProcessOrderScanConfirmReceiveDto dto) {
        return CommonResult.success(processOrderScanBizService.confirmReceive(dto));
    }

    @PostMapping("/completeProcedure")
    @ApiOperation("完成工序")
    public CommonResult<Boolean> completeProcedure(@NotNull @Valid @RequestBody ProcessOrderScanCompleteProcedureDto dto) {
        return CommonResult.success(processOrderScanBizService.completeProcedure(dto));
    }

    @PostMapping("/removeByProcedureId")
    @ApiOperation("通过加工工序删除扫码记录")
    public CommonResult<Boolean> removeByProcedureId(@NotNull @Valid @RequestBody ProcessOrderScanRemoveByProcedureIdDto dto) {
        return CommonResult.success(processOrderScanBizService.removeByProcedureId(dto));
    }

    @PostMapping("/statNumByMonth")
    @ApiOperation("统计本月提成以及数量")
    public CommonResult<ProcessOrderScanStatNumVo> statNumByMonth() {
        return CommonResult.success(processOrderScanBizService.scanRecordDataStatistics());
    }

    @PostMapping("/statList")
    @ApiOperation("H5-个人中心-全部/本月扫码记录详情")
    public CommonPageResult<ProcessOrderScanStatListVo> statList(@NotNull @Valid @RequestBody ProcessOrderScanStatListDto dto) {
        return new CommonPageResult<>(processOrderScanBizService.statList(dto));
    }

    @ApiOperation("导出月度扫码加工明细统计")
    @PostMapping("/exportMonthStatistics")
    public CommonResult<Void> getMonthStatisticsExport(@NotNull @Valid @RequestBody ProcessOrderScanQueryByApiDto dto) {
        processOrderScanBizService.exportMonthStatistics(dto);
        return CommonResult.success();
    }

    @ApiOperation("更新工序提成(组合与非组合)")
    @PostMapping("/updateCommissionJob")
    public CommonResult<Void> updateCommissionJob() {
        processOrderScanBizService.updateProcessOrderScanJob("2024-02");
        return CommonResult.success();
    }
}
