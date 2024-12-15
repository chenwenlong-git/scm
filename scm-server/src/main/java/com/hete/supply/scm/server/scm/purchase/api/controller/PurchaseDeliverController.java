package com.hete.supply.scm.server.scm.purchase.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierDeliverBizService;
import com.hete.support.api.result.CommonPageResult;
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
 * @date 2024/11/13 16:59
 */
@Validated
@RestController
@RequestMapping("/scm/purchase/deliver")
@RequiredArgsConstructor
@Api(tags = "供应链--发货管理")
public class PurchaseDeliverController {
    private final SupplierDeliverBizService supplierDeliverBizService;


    @ApiOperation("发货管理列表")
    @PostMapping("/deliverList")
    public CommonPageResult<PurchaseDeliverVo> deliverList(@NotNull @Validated @RequestBody PurchaseDeliverListDto dto) {


        return new CommonPageResult<>(supplierDeliverBizService.deliverList(dto));
    }

}
