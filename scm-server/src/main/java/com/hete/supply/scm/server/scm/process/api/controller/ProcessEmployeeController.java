package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessEmployeeQueryRequestDto;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessEmployeeVo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessEmployeeBizService;
import com.hete.support.api.result.CommonPageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * Created on 2024/1/11.
 */
@RestController
@Api(tags = "加工部员工管理")
@RequestMapping("/scm/processing/employee")
@RequiredArgsConstructor
public class ProcessEmployeeController {

    private final ProcessEmployeeBizService processEmployeeBizService;

    @ApiOperation("员工信息列表")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessEmployeeVo> getByPage(@NotNull @RequestBody ProcessEmployeeQueryRequestDto request) {
        return new CommonPageResult<>(processEmployeeBizService.getByPage(request));
    }
}
