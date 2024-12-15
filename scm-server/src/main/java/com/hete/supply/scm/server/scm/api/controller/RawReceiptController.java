package com.hete.supply.scm.server.scm.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawReceiptVo;
import com.hete.supply.scm.server.supplier.service.biz.SupplierRawReceiptBizService;
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

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/28 15:31
 */
@Validated
@RestController
@RequestMapping("/scm/rawReceipt")
@RequiredArgsConstructor
@Api(tags = "供应链-采购原料收货单")
public class RawReceiptController {
    private final SupplierRawReceiptBizService supplierRawReceiptBizService;

    @ApiOperation("查采购原料收货单列表")
    @PostMapping("/search")
    public CommonPageResult<PurchaseRawReceiptVo> search(@NotNull @RequestBody @Validated PurchaseRawReceiptSearchDto dto) {
        return new CommonPageResult<>(supplierRawReceiptBizService.search(dto));
    }

    @ApiOperation("采购原料收货导出")
    @PostMapping("/getNewRawReceiptExportList")
    public CommonResult<Void> getNewRawReceiptExportList(@NotNull @Validated @RequestBody PurchaseRawReceiptSearchDto dto) {
        supplierRawReceiptBizService.getScmNewRawReceiptExportList(dto);
        return CommonResult.success();
    }


}
