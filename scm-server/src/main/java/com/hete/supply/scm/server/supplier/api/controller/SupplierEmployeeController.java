package com.hete.supply.scm.server.supplier.api.controller;

import com.hete.supply.scm.server.supplier.entity.dto.*;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeGradeSearchVo;
import com.hete.supply.scm.server.supplier.entity.vo.EmployeeSearchVo;
import com.hete.supply.scm.server.supplier.service.biz.EmployeeBizService;
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
 * @author weiwenxin
 * @date 2023/7/24 23:34
 */
@Validated
@RestController
@RequestMapping("/supplier/employee")
@RequiredArgsConstructor
@Api(tags = "供应商--员工管理")
public class SupplierEmployeeController {

    private final EmployeeBizService employeeBizService;

    @ApiOperation("职级管理列表")
    @PostMapping("/searchEmployeeGrade")
    public CommonPageResult<EmployeeGradeSearchVo> searchEmployeeGrade(@NotNull @Validated @RequestBody EmployeeGradeSearchDto dto) {
        return new CommonPageResult<>(employeeBizService.searchEmployeeGrade(dto));
    }

    @ApiOperation("添加职级")
    @PostMapping("/createEmployeeGrade")
    public CommonResult<Void> createEmployeeGrade(@NotNull @Validated @RequestBody EmployGradeCreateDto dto) {
        employeeBizService.createEmployeeGrade(dto);
        return CommonResult.success();
    }

    @ApiOperation("编辑职级")
    @PostMapping("/editEmployeeGrade")
    public CommonResult<Void> editEmployeeGrade(@NotNull @Validated @RequestBody EmployGradeEditDto dto) {
        employeeBizService.editEmployeeGrade(dto);
        return CommonResult.success();
    }

    @ApiOperation("员工管理列表")
    @PostMapping("/searchEmployee")
    public CommonPageResult<EmployeeSearchVo> searchEmployee(@NotNull @Validated @RequestBody EmployeeSearchDto dto) {
        return new CommonPageResult<>(employeeBizService.searchEmployee(dto));
    }

    @ApiOperation("编辑员工信息")
    @PostMapping("/editEmployee")
    public CommonResult<Void> editEmployee(@NotNull @Validated @RequestBody EmployEditDto dto) {
        employeeBizService.editEmployee(dto);
        return CommonResult.success();
    }
}
