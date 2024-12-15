package com.hete.supply.scm.server.scm.api.controller;

import com.hete.supply.scm.server.scm.entity.dto.PlmCreateDto;
import com.hete.supply.scm.server.scm.service.base.SkuBaseService;
import com.hete.supply.scm.server.scm.service.biz.SkuBizService;
import com.hete.support.api.result.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author yanjiawei
 * @date 2023年06月26日 18:25
 */
@RestController
@Api(tags = "plm商品信息管理")
@RequestMapping("/scm/plmSku")
@RequiredArgsConstructor
public class PlmSkuController {

    private final SkuBizService skuBizService;

    @PostMapping("/syncPlmSku")
    @ApiOperation("同步plm商品信息")
    public CommonResult<Void> syncSku(@NotNull @RequestBody @Valid PlmCreateDto plmCreateDto) {
        skuBizService.syncSku(plmCreateDto);
        return CommonResult.success();
    }
}
