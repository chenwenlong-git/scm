package com.hete.supply.scm.server.scm.production.api.controller;

import com.hete.supply.scm.server.scm.production.entity.dto.DeleteAttributeRiskDto;
import com.hete.supply.scm.server.scm.production.entity.dto.UpdateAttributeRiskDto;
import com.hete.supply.scm.server.scm.production.entity.vo.AttributeRiskInfoVo;
import com.hete.supply.scm.server.scm.production.service.biz.AttributeBizService;
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
@Api(tags = "供应链风险属性管理")
@RequestMapping("/scm/attrRisk")
@RequiredArgsConstructor
public class AttributeRiskController {

    private final AttributeBizService bizService;

    @ApiOperation("更新属性风险信息")
    @PostMapping("/updateAttrRisk")
    public CommonResult<Void> updateAttrRisk(@NotNull @Validated @RequestBody UpdateAttributeRiskDto dto) {
        bizService.updateAttrRisk(dto);
        return CommonResult.success();
    }

    @ApiOperation("风险信息列表")
    @PostMapping("/attrRiskList")
    public CommonResult<ResultList<AttributeRiskInfoVo>> attrRiskList() {
        List<AttributeRiskInfoVo> attrRiskList = bizService.attrRiskList();
        return CommonResult.successForList(attrRiskList);
    }

    @ApiOperation("删除属性风险信息")
    @PostMapping("/deleteAttrRisk")
    public CommonResult<Void> deleteAttrRisk(@NotNull @RequestBody DeleteAttributeRiskDto dto) {
        bizService.deleteAttrRisk(dto);
        return CommonResult.success();
    }
}
