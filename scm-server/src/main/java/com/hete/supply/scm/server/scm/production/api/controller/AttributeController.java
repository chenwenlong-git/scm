package com.hete.supply.scm.server.scm.production.api.controller;

import com.hete.supply.scm.server.scm.entity.vo.PlmCategoryInfoVo;
import com.hete.supply.scm.server.scm.production.entity.dto.*;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributeCategoryVo;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributeInfoVo;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributePageVo;
import com.hete.supply.scm.server.scm.production.service.biz.AttributeBizService;
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
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@RestController
@Api(tags = "供应链属性管理")
@RequestMapping("/scm/attr")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeBizService bizService;

    @PostMapping("/getPlmAllSecondCateList")
    @ApiOperation("获取pdc所有二级类目")
    public CommonResult<ResultList<PlmCategoryInfoVo>> getPlmAllSecondCateList() {
        List<PlmCategoryInfoVo> plmCategoryInfoList = bizService.getPlmAllSecondCateList();
        return CommonResult.successForList(plmCategoryInfoList);
    }

    @ApiOperation("获取次级属性分类列表")
    @PostMapping("/listChildAttrCategory")
    public CommonResult<ResultList<AttributeCategoryVo>> listChildAttrCategory() {
        List<AttributeCategoryVo> childAttributeCategoryList = bizService.listChildAttrCategory();
        return CommonResult.successForList(childAttributeCategoryList);
    }

    @ApiOperation("获取属性分类列表")
    @PostMapping("/listAttrCategory")
    public CommonResult<ResultList<AttributeCategoryVo>> listAttrCategory(@NotNull @Validated @RequestBody AttributeCategoryDto dto) {
        List<AttributeCategoryVo> attributeCategoryVoList = bizService.listAttrCategory(dto);
        return CommonResult.successForList(attributeCategoryVoList);
    }

    @ApiOperation("新增属性")
    @PostMapping("/addAttr")
    public CommonResult<Void> addAttr(@NotNull @Validated @RequestBody AddAttributeDto dto) {
        bizService.addAttr(dto);
        return CommonResult.success();
    }

    @ApiOperation("更新属性")
    @PostMapping("/updateAttr")
    public CommonResult<Void> updateAttr(@NotNull @Validated @RequestBody UpdateAttributeDto dto) {
        bizService.updateAttr(dto);
        return CommonResult.success();
    }

    @ApiOperation("分页查询")
    @PostMapping("/pageAttr")
    public CommonPageResult<AttributePageVo> pageAttr(@NotNull @Validated @RequestBody GetAttributePageDto dto) {
        return new CommonPageResult<>(bizService.pageAttr(dto));
    }

    @ApiOperation("列表查询")
    @PostMapping("/listAttr")
    public CommonResult<ResultList<AttributeInfoVo>> listAttr(@NotNull @Validated @RequestBody GetAttributeListDto dto) {
        List<AttributeInfoVo> attributeInfoVoList = bizService.listAttr(dto);
        return CommonResult.successForList(attributeInfoVoList);
    }

    @ApiOperation("属性详情")
    @PostMapping("/attrInfo")
    public CommonResult<AttributeInfoVo> attrInfo(@NotNull @Validated @RequestBody GetAttributeInfoDto dto) {
        AttributeInfoVo attributeInfoVo = bizService.attrInfo(dto);
        return CommonResult.success(attributeInfoVo);
    }
}
