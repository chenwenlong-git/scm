package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateDetailByTemplateVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateVo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessTemplateBizService;
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
 * @date: 2022/11/2 14:23
 */
@RestController
@Api(tags = "工序模版管理")
@RequestMapping("/scm/processTemplate")
@RequiredArgsConstructor
public class ProcessTemplateController {
    private final ProcessTemplateBizService processTemplateBizService;

    @ApiOperation("模版列表")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessTemplateVo> getByPage(@NotNull @Valid @RequestBody ProcessTemplateQueryDto dto) {
        return new CommonPageResult<>(processTemplateBizService.getByPage(dto));
    }

    @ApiOperation("通过sku或者工序模版名称查询列表")
    @PostMapping("/getByTemplate")
    public CommonResult<ResultList<ProcessTemplateDetailByTemplateVo>> getByTemplate(@NotNull @RequestBody @Valid ProcessTemplateQueryByTemplateDto dto) {
        return CommonResult.successForList(processTemplateBizService.getByTemplate(dto));
    }

    @PostMapping("/create")
    @ApiOperation("创建模版")
    public CommonResult<Long> create(@NotNull @Valid @RequestBody ProcessTemplateCreateDto dto) {
        return CommonResult.success(processTemplateBizService.create(dto));
    }

    @PostMapping("/edit")
    @ApiOperation("编辑工序模版")
    public CommonResult<Boolean> create(@NotNull @Valid @RequestBody ProcessTemplateEditDto dto) {
        return CommonResult.success(processTemplateBizService.edit(dto));
    }

    @PostMapping("/detail")
    @ApiOperation("工序模版详情")
    public CommonResult<ProcessTemplateDetailVo> detail(@NotNull @Valid @RequestBody ProcessTemplateDetailDto dto) {
        return CommonResult.success(processTemplateBizService.detail(dto));
    }

    @PostMapping("/remove")
    @ApiOperation("删除工序模版")
    public CommonResult<Boolean> remove(@NotNull @Valid @RequestBody ProcessTemplateRemoveDto dto) {
        return CommonResult.success(processTemplateBizService.remove(dto));
    }
}
