package com.hete.supply.scm.server.scm.api.controller;

import com.hete.supply.scm.server.scm.entity.vo.TimeCommonVo;
import com.hete.supply.scm.server.scm.service.biz.TimeCommonBizService;
import com.hete.support.api.result.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanjiawei
 * @date 2023年09月14日 18:13
 */
@Validated
@RestController
@RequestMapping("/scm/time")
@RequiredArgsConstructor
@Api(tags = "通用时间接口")
public class TimeCommonController {

    private final TimeCommonBizService timeCommonBizService;

    @ApiOperation("获取系统当前时间")
    @PostMapping("/getCurrentDateTime")
    public CommonResult<TimeCommonVo> getCurrentDateTime() {
        return CommonResult.success(timeCommonBizService.getTimeCommon());
    }
}
