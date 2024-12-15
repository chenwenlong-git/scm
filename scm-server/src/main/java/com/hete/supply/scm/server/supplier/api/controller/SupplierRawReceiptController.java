package com.hete.supply.scm.server.supplier.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseRawReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseRawCommitDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseRawReceiptOrderNoDto;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawReceiptDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawReceiptVo;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.scm.server.supplier.service.biz.SupplierRawReceiptBizService;
import com.hete.support.api.exception.ParamIllegalException;
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
import java.util.Arrays;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/28 15:31
 */
@Validated
@RestController
@RequestMapping("/supplier/rawReceipt")
@RequiredArgsConstructor
@Api(tags = "供应商-采购原料收货单")
public class SupplierRawReceiptController {
    private final SupplierRawReceiptBizService supplierRawReceiptBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("查采购原料收货单列表")
    @PostMapping("/search")
    public CommonPageResult<PurchaseRawReceiptVo> search(@NotNull @RequestBody @Validated PurchaseRawReceiptSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        // spm列表只展示待收货、已收货的原料收货单
        if (CollectionUtils.isEmpty(dto.getReceiptOrderStatusList())) {
            dto.setReceiptOrderStatusList(Arrays.asList(ReceiptOrderStatus.WAIT_RECEIVE, ReceiptOrderStatus.RECEIPTED));
        }
        return new CommonPageResult<>(supplierRawReceiptBizService.search(dto));
    }

    @ApiOperation("采购原料收货单详情页")
    @PostMapping("/detail")
    public CommonResult<PurchaseRawReceiptDetailVo> detail(@NotNull @Validated @RequestBody PurchaseRawReceiptOrderNoDto dto) {
        return CommonResult.success(supplierRawReceiptBizService.detail(dto));
    }

    @ApiOperation("h5采购原料收货单详情页(兼容收货单号或运单号查询)")
    @PostMapping("/h5detail")
    public CommonResult<PurchaseRawReceiptDetailVo> h5detail(@NotNull @Validated @RequestBody PurchaseRawReceiptOrderNoDto dto) {
        return CommonResult.success(supplierRawReceiptBizService.h5detail(dto));
    }

    @ApiOperation("确认提交")
    @PostMapping("/commit")
    public CommonResult<Void> commit(@NotNull @Validated @RequestBody PurchaseRawCommitDto dto) {
        supplierRawReceiptBizService.commit(dto);
        return CommonResult.success();
    }

    @ApiOperation("采购原料收货导出")
    @PostMapping("/getNewRawReceiptExportList")
    public CommonResult<Void> getNewRawReceiptExportList(@NotNull @Validated @RequestBody PurchaseRawReceiptSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new ParamIllegalException("导出失败，当前用户没有供应商数据权限！");
        }
        dto.setAuthSupplierCode(supplierCodeList);
        supplierRawReceiptBizService.getNewRawReceiptExportList(dto);
        return CommonResult.success();
    }

}
