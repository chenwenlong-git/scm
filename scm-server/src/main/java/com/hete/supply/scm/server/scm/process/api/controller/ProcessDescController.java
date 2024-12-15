package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescEditDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescQueryDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescRemoveDto;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescVo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessDescBizService;
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
@Api(tags = "加工描述管理")
@RequestMapping("/scm/processDesc")
@RequiredArgsConstructor
public class ProcessDescController {

    private final ProcessDescBizService processDescBizService;

    @ApiOperation("描述列表")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessDescVo> getByPage(@NotNull @Valid @RequestBody ProcessDescQueryDto dto) {
        return new CommonPageResult<>(processDescBizService.getByPage(dto));
    }

    @PostMapping("/create")
    @ApiOperation("创建加工描述")
    public CommonResult<Void> create(@NotNull @Valid @RequestBody ProcessDescCreateDto dto) {
        processDescBizService.create(dto);
        return CommonResult.success();
    }

    @PostMapping("/edit")
    @ApiOperation("编辑加工描述")
    public CommonResult<Void> create(@NotNull @Valid @RequestBody ProcessDescEditDto dto) {
        processDescBizService.edit(dto);
        return CommonResult.success();
    }

    @PostMapping("/remove")
    @ApiOperation("删除加工描述")
    public CommonResult<Void> remove(@NotNull @Valid @RequestBody ProcessDescRemoveDto dto) {
        processDescBizService.remove(dto);
        return CommonResult.success();
    }
}
