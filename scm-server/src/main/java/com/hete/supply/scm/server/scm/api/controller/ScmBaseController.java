package com.hete.supply.scm.server.scm.api.controller;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderNoListDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSimpleVo;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopSampleOrderBizService;
import com.hete.supply.scm.server.scm.entity.dto.SkuListDto;
import com.hete.supply.scm.server.scm.entity.vo.SkuVariantAttrVo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseChildNoDto;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseChildOrderSimpleVo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.service.base.SkuBaseService;
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
 * @date 2023/7/10 16:22
 */
@Validated
@RestController
@RequestMapping("/scm/base")
@RequiredArgsConstructor
@Api(tags = "基础能力接口")
public class ScmBaseController {
    private final SkuBaseService skuBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final DevelopSampleOrderBizService developSampleOrderBizService;


    @ApiOperation("次品处理列表")
    @PostMapping("/getSkuDetailBySkuList")
    public CommonResult<ResultList<SkuVariantAttrVo>> getSkuDetailBySkuList(@NotNull @Validated @RequestBody SkuListDto dto) {
        return CommonResult.successForList(skuBaseService.getSkuDetailBySkuList(dto));
    }

    @ApiOperation("根据采购单号获取对应的采购子单信息")
    @PostMapping("/getPurchaseSimpleVoByNo")
    public CommonResult<PurchaseChildOrderSimpleVo> getPurchaseSimpleVoByNo(@NotNull @Validated @RequestBody PurchaseChildNoDto dto) {
        return CommonResult.success(purchaseBaseService.getPurchaseSimpleVoByNo(dto));
    }

    @ApiOperation("根据样品单号获取对应的样品单信息")
    @PostMapping("/getSampleSimpleVoByNo")
    public CommonResult<DevelopSampleSimpleVo> getSampleSimpleVoByNo(@NotNull @Validated @RequestBody DevelopSampleOrderNoListDto dto) {
        return CommonResult.success(developSampleOrderBizService.getSampleSimpleVoByNo(dto));
    }
}
