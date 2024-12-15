package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessExportVo;
import com.hete.supply.scm.server.scm.entity.vo.GoodsProcessVo;
import com.hete.supply.scm.server.scm.process.entity.dto.GoodsProcessBindDto;
import com.hete.supply.scm.server.scm.process.entity.dto.GoodsProcessUnBindDto;
import com.hete.supply.scm.server.scm.process.service.biz.GoodsProcessBizService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:23
 */
@RestController
@Api(tags = "商品工序管理")
@RequestMapping("/scm/goodsProcess")
@RequiredArgsConstructor
public class GoodsProcessController {

    private final GoodsProcessBizService goodsProcessBizService;

    @ApiOperation("商品工序列表")
    @PostMapping("/getByPage")
    public CommonPageResult<GoodsProcessVo> getByPage(@NotNull @Valid @RequestBody GoodsProcessQueryDto dto) {
        return new CommonPageResult<>(goodsProcessBizService.getByPage(dto));
    }

    @PostMapping("/bind")
    @ApiOperation("绑定工序")
    public CommonResult<Boolean> bind(@NotNull @Valid @RequestBody GoodsProcessBindDto dto) {
        return CommonResult.success(goodsProcessBizService.bind(dto));
    }

    @PostMapping("/unbind")
    @ApiOperation("解除工序")
    public CommonResult<Boolean> unbind(@NotNull @Valid @RequestBody GoodsProcessUnBindDto dto) {
        return CommonResult.success(goodsProcessBizService.unbind(dto));
    }


    @PostMapping("/getExportList")
    @ApiOperation("获取商品工序导出列表")
    public CommonPageResult<GoodsProcessExportVo> getExportList(@NotNull @Valid @RequestBody GoodsProcessQueryByApiDto dto) {
        return new CommonPageResult<>(goodsProcessBizService.getExportList(dto));
    }

    @ApiOperation("商品工序导出")
    @PostMapping("/exportGoodsProcess")
    public CommonResult<Void> exportGoodsProcess(@NotNull @Validated @RequestBody GoodsProcessQueryDto dto) {
        goodsProcessBizService.exportGoodsProcess(dto);
        return CommonResult.success();
    }
}
