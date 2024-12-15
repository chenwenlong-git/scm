package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.CostCoefficientUpdateRequestDto;
import com.hete.supply.scm.api.scm.entity.dto.CostCoefficientsRequestDto;
import com.hete.supply.scm.server.scm.process.entity.vo.CostCoefficientsVo;
import com.hete.supply.scm.server.scm.process.entity.vo.LatestCostCoefficientsVo;
import com.hete.supply.scm.server.scm.process.service.biz.CostCoefficientsBizService;
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
 * @author yanjiawei
 * Created on 2024/2/20.
 */
@RestController
@Api(tags = "固定成本管理")
@RequestMapping("/scm/costCoefficients")
@RequiredArgsConstructor
public class CostCoefficientsController {

    private final CostCoefficientsBizService costCoefficientsService;

    @ApiOperation("固定成本系数列表")
    @PostMapping("/getByPage")
    public CommonPageResult<CostCoefficientsVo> getByPage(@NotNull @Valid @RequestBody CostCoefficientsRequestDto request) {
        return new CommonPageResult<>(costCoefficientsService.getByPage(request));
    }

    @ApiOperation("更新固定成本系数")
    @PostMapping("/updateCoefficient")
    public CommonResult<Void> updateCoefficient(@NotNull @Valid @RequestBody CostCoefficientUpdateRequestDto dto) {
        String globUpdateCoefficientKey = costCoefficientsService.getGlobUpdateCoefficientKey();
        dto.setKey(globUpdateCoefficientKey);
        costCoefficientsService.updateCoefficient(dto);
        return CommonResult.success();
    }

    @ApiOperation("获取当前月最新系数")
    @PostMapping("/getLatestCoefficient")
    public CommonResult<LatestCostCoefficientsVo> getLatestCoefficient() {
        LatestCostCoefficientsVo latestCostCoefficientsVo = costCoefficientsService.LatestCostCoefficientsVo();
        return CommonResult.success(latestCostCoefficientsVo);
    }

    @ApiOperation("更新固定成本系数")
    @PostMapping("/updateCoefficientJob")
    public CommonResult<Void> updateCoefficientJob() {
        String globUpdateCoefficientKey = costCoefficientsService.getGlobUpdateCoefficientKey();
        costCoefficientsService.updateCoefficient(globUpdateCoefficientKey, "2024-04-16 00:00:00");
        return CommonResult.success();
    }
}