package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessVo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
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
@Api(tags = "工序管理")
@RequestMapping("/scm/process")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessBizService processBizService;


    @ApiOperation("工序列表")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessVo> getByPage(@NotNull @Valid @RequestBody ProcessQueryDto dto) {
        return new CommonPageResult<>(processBizService.getByPage(dto));
    }

    @PostMapping("/createProcess")
    @ApiOperation("创建独立工序")
    public CommonResult<Void> createProcess(@NotNull @RequestBody @Valid ProcessCreateDto dto) {
        processBizService.createProcess(dto);
        return CommonResult.success();
    }

    @PostMapping("/createCompoundProcess")
    @ApiOperation("创建复合工序")
    public CommonResult<Void> createCompoundProcess(@NotNull @RequestBody @Valid CompoundProcessCreateDto dto) {
        processBizService.createCompoundProcess(dto);
        return CommonResult.success();
    }

    @PostMapping("/editProcess")
    @ApiOperation("编辑工序")
    public CommonResult<Void> editProcess(@NotNull @Valid @RequestBody ProcessEditDto dto) {
        processBizService.editProcess(dto);
        return CommonResult.success();
    }

    @ApiOperation("所有的工序列表")
    @PostMapping("/getAll")
    public CommonResult<ResultList<ProcessVo>> getAll() {
        return CommonResult.successForList(processBizService.getAll());
    }

    @PostMapping("/changeStatus")
    @ApiOperation("批量启用或禁用工序")
    public CommonResult<Boolean> changeStatus(@NotNull @Valid @RequestBody ProcessChangeStatusDto dto) {
        return CommonResult.success(processBizService.changeStatus(dto));
    }

    @ApiOperation("查询工序列表")
    @PostMapping("/getQueryList")
    public CommonResult<ResultList<ProcessVo>> getQueryList(@NotNull @Valid @RequestBody ProcessQueryListDto dto) {
        return CommonResult.successForList(processBizService.getQueryList(dto));
    }

    @PostMapping("/configCommissionRuleBatch")
    @ApiOperation("批量配置工序提成规则")
    public CommonResult<Void> createOrUpdateCommissionRules(@NotNull @Valid @RequestBody CreateOrUpdateCommissionRuleBatchDto dto) {
        processBizService.createOrUpdateCommissionRules(dto);
        return CommonResult.success();
    }
}
