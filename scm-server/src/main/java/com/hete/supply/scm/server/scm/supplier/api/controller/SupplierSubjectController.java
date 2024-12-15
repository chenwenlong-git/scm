package com.hete.supply.scm.server.scm.supplier.api.controller;

import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierCodeListDto;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierSubjectDropDownVo;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierSubjectBizService;
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
 * @date 2024/5/16 17:33
 */
@Validated
@RestController
@RequestMapping("/scm/supplierSubject")
@RequiredArgsConstructor
@Api(tags = "供应商主体信息")
public class SupplierSubjectController {
    private final SupplierSubjectBizService supplierSubjectBizService;

    @ApiOperation("下拉获取供应商主体信息列表")
    @PostMapping("/getSupplierSubjectList")
    public CommonResult<ResultList<SupplierSubjectDropDownVo>> getSupplierSubjectList(@NotNull @Validated @RequestBody SupplierCodeListDto dto) {
        return CommonResult.successForList(supplierSubjectBizService.getSupplierSubjectList(dto));
    }

}
