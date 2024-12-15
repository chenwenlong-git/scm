package com.hete.supply.scm.server.scm.api.controller;

import com.hete.supply.plm.api.basedata.entity.vo.PlmAttributeVo;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.entity.dto.AttributeNameListDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrPriceDelDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrPriceEditDto;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrPricePageDto;
import com.hete.supply.scm.server.scm.entity.vo.SkuAttrPricePageVo;
import com.hete.supply.scm.server.scm.service.biz.SkuAttrPriceBizService;
import com.hete.support.api.result.CommonPageResult;
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
 * @date 2024/9/10 09:24
 */
@Validated
@RestController
@RequestMapping("/scm/sku")
@RequiredArgsConstructor
@Api(tags = "sku定价接口")
public class SkuAttrPriceController {
    private final SkuAttrPriceBizService skuAttrPriceBizService;
    private final PlmRemoteService plmRemoteService;


    @ApiOperation("sku定价列表")
    @PostMapping("/skuAttrPriceList")
    public CommonPageResult<SkuAttrPricePageVo> skuAttrPriceList(@NotNull @Validated @RequestBody SkuAttrPricePageDto dto) {
        return new CommonPageResult<>(skuAttrPriceBizService.skuAttrPriceList(dto));
    }

    @ApiOperation("删除定价记录")
    @PostMapping("/delSkuAttrPrice")
    public CommonResult<Void> delSkuAttrPrice(@NotNull @Validated @RequestBody SkuAttrPriceDelDto dto) {
        skuAttrPriceBizService.delSkuAttrPrice(dto);
        return CommonResult.success();
    }

    @ApiOperation("编辑定价记录")
    @PostMapping("/editSkuAttrPrice")
    public CommonResult<Void> editSkuAttrPrice(@NotNull @Validated @RequestBody SkuAttrPriceEditDto dto) {
        skuAttrPriceBizService.editSkuAttrPrice(dto);
        return CommonResult.success();
    }


    @ApiOperation("获取plm属性名对应的下拉属性值")
    @PostMapping("/getAttrListByName")
    public CommonResult<ResultList<PlmAttributeVo>> getAttrListByName(@NotNull @Validated @RequestBody AttributeNameListDto dto) {
        return CommonResult.successForList(plmRemoteService.getAttrListByName(dto.getAttributeNameList()));
    }
}
