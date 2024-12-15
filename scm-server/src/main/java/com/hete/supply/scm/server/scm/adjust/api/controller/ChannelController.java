package com.hete.supply.scm.server.scm.adjust.api.controller;

import com.hete.supply.scm.server.scm.adjust.entity.dto.ChannelSaveDto;
import com.hete.supply.scm.server.scm.adjust.entity.dto.ChannelSearchDto;
import com.hete.supply.scm.server.scm.adjust.entity.vo.ChannelVo;
import com.hete.supply.scm.server.scm.adjust.service.biz.ChannelBizService;
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
 * @author ChenWenLong
 * @date 2024/6/18 15:28
 */
@Validated
@RestController
@RequestMapping("/scm/channel")
@RequiredArgsConstructor
@Api(tags = "渠道")
public class ChannelController {

    private final ChannelBizService channelBizService;

    @ApiOperation("渠道设置列表")
    @PostMapping("/searchChannel")
    public CommonResult<ResultList<ChannelVo>> searchChannel(@NotNull @Validated @RequestBody ChannelSearchDto dto) {
        return CommonResult.successForList(channelBizService.searchChannel(dto));
    }

    @ApiOperation("渠道确认提交")
    @PostMapping("/saveChannel")
    public CommonResult<Void> saveChannel(@NotNull @Validated @RequestBody ChannelSaveDto dto) {
        channelBizService.saveChannel(dto);
        return CommonResult.success();
    }


}
