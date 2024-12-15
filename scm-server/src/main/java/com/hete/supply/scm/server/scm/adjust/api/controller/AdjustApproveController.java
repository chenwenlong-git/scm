package com.hete.supply.scm.server.scm.adjust.api.controller;

import com.hete.supply.scm.server.scm.adjust.config.ScmAdjustProp;
import com.hete.supply.scm.server.scm.adjust.entity.dto.AdjustApproveSearchDto;
import com.hete.supply.scm.server.scm.adjust.entity.dto.AdjustPriceApproveNoDto;
import com.hete.supply.scm.server.scm.adjust.entity.vo.AdjustApproveVo;
import com.hete.supply.scm.server.scm.adjust.service.biz.AdjustApproveBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.holder.GlobalContext;
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
 * @date 2024/6/13 14:27
 */
@Validated
@RestController
@RequestMapping("/scm/adjust")
@RequiredArgsConstructor
@Api(tags = "调价审批")
public class AdjustApproveController {
    private final AdjustApproveBizService adjustApproveBizService;
    private final ScmAdjustProp scmAdjustProp;

    @ApiOperation("调价审批列表")
    @PostMapping("/searchAdjustApprove")
    public CommonPageResult<AdjustApproveVo> searchAdjustApprove(@NotNull @Validated @RequestBody AdjustApproveSearchDto dto) {
        final String currentUser = GlobalContext.getUserKey();

        if (!scmAdjustProp.getWhitelist().contains(currentUser)) {
            dto.setDataUser(currentUser);
        }

        return new CommonPageResult<>(adjustApproveBizService.searchAdjustApprove(dto));
    }

    @ApiOperation("调价审批待办列表")
    @PostMapping("/todoList")
    public CommonPageResult<AdjustApproveVo> todoList(@NotNull @Validated @RequestBody AdjustApproveSearchDto dto) {
        dto.setCtrlUser(GlobalContext.getUserKey());
        return new CommonPageResult<>(adjustApproveBizService.searchAdjustApprove(dto));
    }

    @ApiOperation("审批同意")
    @PostMapping("/approveWorkFlow")
    public CommonResult<Void> approveWorkFlow(@NotNull @Validated @RequestBody AdjustPriceApproveNoDto dto) {
        adjustApproveBizService.approveWorkFlow(dto);
        return CommonResult.success();
    }

    @ApiOperation("审批拒绝")
    @PostMapping("/rejectWorkFlow")
    public CommonResult<Void> rejectWorkFlow(@NotNull @Validated @RequestBody AdjustPriceApproveNoDto dto) {
        adjustApproveBizService.rejectWorkFlow(dto);
        return CommonResult.success();
    }
}
