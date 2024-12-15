package com.hete.supply.scm.server.scm.qc.api.controller;

import com.hete.supply.scm.server.scm.qc.entity.dto.QcDefectConfigCreateDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcDefectConfigStatusDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcDefectConfigUpdateDto;
import com.hete.supply.scm.server.scm.qc.entity.vo.QcDefectConfigVo;
import com.hete.supply.scm.server.scm.qc.service.biz.QcOrderConfigBizService;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
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
 * @date 2023/10/13 10:13
 */
@Validated
@RestController
@RequestMapping("/scm/qcConfig")
@RequiredArgsConstructor
@Api(tags = "质检")
public class QcConfigController {
    private final QcOrderConfigBizService qcOrderConfigBizService;

    @ApiOperation("次品原因配置列表")
    @PostMapping("/qcDefectConfigList")
    public CommonResult<ResultList<QcDefectConfigVo>> qcDefectConfigList() {

        return CommonResult.successForList(qcOrderConfigBizService.qcDefectConfigList());
    }

    @ApiOperation("新增次品原因")
    @PostMapping("/createDefectConfig")
    public CommonResult<Void> createDefectConfig(@NotNull @Validated @RequestBody QcDefectConfigCreateDto dto) {
        qcOrderConfigBizService.createDefectConfig(dto);
        return CommonResult.success();
    }

    @ApiOperation("更新次品原因")
    @PostMapping("/updateDefectConfig")
    public CommonResult<Void> updateDefectConfig(@NotNull @Validated @RequestBody QcDefectConfigUpdateDto dto) {
        qcOrderConfigBizService.updateDefectConfig(dto);
        return CommonResult.success();
    }


    @ApiOperation("更新状态")
    @PostMapping("/updateDefectConfigStatus")
    public CommonResult<Void> updateDefectConfigStatus(@NotNull @Validated @RequestBody QcDefectConfigStatusDto dto) {
        qcOrderConfigBizService.updateDefectConfigStatus(dto);
        return CommonResult.success();
    }

    @ApiOperation("次品原因导出")
    @PostMapping("/exportQcDefectConfig")
    public CommonResult<Void> exportQcDefectConfig() {
        qcOrderConfigBizService.exportQcDefectConfig();
        return CommonResult.success();
    }


}
