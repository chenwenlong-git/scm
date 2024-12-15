package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.server.scm.entity.dto.UpdateProcessPlanDto;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPlanInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderPlanVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessProcedureEmployeePlanVo;
import com.hete.supply.scm.server.scm.process.service.base.ProcessPlanBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * @date 2023/7/29
 */
@Validated
@RestController
@RequestMapping("/scm/processOrderPlan")
@RequiredArgsConstructor
@Api(tags = "加工单排产池")
@Slf4j
public class ProcessOrderPlanController {
    private final ProcessPlanBizService processPlanBizService;

    @ApiOperation("排产池列表搜索")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessOrderPlanVo> getByPage(@NotNull @Valid @RequestBody ProcessOrderPlanQueryDto dto) {
        return new CommonPageResult<>(processPlanBizService.getByPage(dto));
    }

    @ApiOperation("排产池列表详情")
    @PostMapping("/info")
    public CommonResult<ProcessOrderPlanInfoVo> info(@NotNull @Valid @RequestBody ProcessOrderPlanQueryInfoDto dto) {
        return CommonResult.success(processPlanBizService.info(dto));
    }

    @ApiOperation("手动排产")
    @PostMapping("/doProcessPlan")
    public CommonResult<Boolean> doProcessPlan(@NotNull @Valid @RequestBody DoProcessPlanDto dto) {
        processPlanBizService.doProcessPlan(dto);
        return CommonResult.success(true);
    }

    @ApiOperation("修改排产")
    @PostMapping("/updateProcessPlan")
    public CommonResult<Boolean> updateProcessPlan(@NotNull @Valid @RequestBody UpdateProcessPlanDto dto) {
        processPlanBizService.updateProcessPlan(dto);
        return CommonResult.success(Boolean.TRUE);
    }

    @ApiOperation("取消排产")
    @PostMapping("/cancelProcessPlan")
    public CommonResult<Boolean> cancelProcessPlan(@NotNull @Valid @RequestBody CancelProcessPlanDto dto) {
        processPlanBizService.cancelProcessPlan(dto);
        return CommonResult.success(Boolean.TRUE);
    }

    @ApiOperation("自动排产")
    @PostMapping("/autoProcessPlan")
    public void autoProcessPlan() {
        processPlanBizService.executeProductionPlanTask();
    }

    @ApiOperation("获取员工排产计划")
    @PostMapping("/getProcessProcedureEmployeePlan")
    public CommonResult<ResultList<ProcessProcedureEmployeePlanVo>> getProcessProcedureEmployeePlan(@NotNull @Valid
                                                                                                    @RequestBody EmployeeScheduleDto dto) {
        List<ProcessProcedureEmployeePlanVo> vos = processPlanBizService.getProcessProcedureEmployeePlan(dto);
        return CommonResult.successForList(vos);
    }

}
