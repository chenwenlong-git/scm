package com.hete.supply.scm.server.supplier.sample.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SamplePurchaseSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildIdAndVersionDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildNoDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleChildNoListDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleSupplyRawDto;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SamplePurchaseDetailVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SamplePurchasePrintVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SamplePurchaseSearchVo;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleBizService;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchasePriceDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReceiveDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnRawDto;
import com.hete.supply.scm.server.supplier.sample.service.biz.SupplierSampleBizService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
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
 * @author weiwenxin
 * @date 2022/11/1
 */
@Validated
@RestController
@RequestMapping("/supplier/sample")
@RequiredArgsConstructor
@Api(tags = "供应商--样品采购需求")
public class SupplierSampleController {
    private final SampleBizService sampleBizService;
    private final SupplierSampleBizService supplierSampleBizService;
    private final AuthBaseService authBaseService;

    @ApiOperation("样品采购列表")
    @PostMapping("/searchSample")
    public CommonPageResult<SamplePurchaseSearchVo> searchSample(@NotNull @Validated @RequestBody SamplePurchaseSearchDto dto) {
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getSampleOrderStatusList())) {
            dto.setSampleOrderStatusList(SampleOrderStatus.getSupplierAllStatusList());
        }
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);

        return new CommonPageResult<>(sampleBizService.searchSamplePurchase(dto));
    }

    @ApiOperation("确认接单")
    @PostMapping("/receiveOrder")
    public CommonResult<Void> receiveOrder(@NotNull @Validated @RequestBody SampleReceiveDto dto) {
        supplierSampleBizService.receiveOrder(dto);
        return CommonResult.success();
    }

    @ApiOperation("母单终结状态判断")
    @PostMapping("/isSampleEndStatus")
    public CommonResult<SampleParentOrderPo> isSampleEndStatus(@NotNull @Validated @RequestBody SampleReceiveDto dto) {
        return CommonResult.success(supplierSampleBizService.isSampleEndStatus(dto));
    }

    @ApiOperation("确认打版")
    @PostMapping("/typesetting")
    public CommonResult<Void> typesetting(@NotNull @Validated @RequestBody SampleChildIdAndVersionDto dto) {
        supplierSampleBizService.typesetting(dto);
        return CommonResult.success();
    }

    @ApiOperation("确认发货")
    @PostMapping("/deliver")
    public CommonResult<Void> deliver(@NotNull @Validated @RequestBody SampleDeliverDto dto) {
        supplierSampleBizService.deliver(dto);
        return CommonResult.success();
    }

    @ApiOperation("样品采购详情页")
    @PostMapping("/samplePurchaseDetail")
    public CommonResult<SamplePurchaseDetailVo> samplePurchaseDetail(@NotNull @Validated @RequestBody SampleChildNoDto dto) {
        // 供应商可以看到的状态限制
        if (CollectionUtils.isEmpty(dto.getSampleOrderStatusList())) {
            dto.setSampleOrderStatusList(SampleOrderStatus.getSupplierAllStatusList());
        }
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonResult<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);

        return CommonResult.success(sampleBizService.samplePurchaseDetail(dto));
    }

    @ApiOperation("批量打印样品采购")
    @PostMapping("/batchPrintSamplePurchase")
    public CommonResult<ResultList<SamplePurchasePrintVo>> batchPrintSamplePurchase(@NotNull @Validated @RequestBody SampleChildNoListDto dto) {
        return CommonResult.successForList(sampleBizService.batchPrintSamplePurchase(dto));
    }


    @ApiOperation("修改价格")
    @PostMapping("/updatePrice")
    public CommonResult<Void> updatePrice(@NotNull @Validated @RequestBody PurchasePriceDto dto) {
        supplierSampleBizService.updatePrice(dto);
        return CommonResult.success();
    }

    @ApiOperation("补充原料")
    @PostMapping("/supplyRaw")
    public CommonResult<Void> supplyRaw(@NotNull @Validated @RequestBody SampleSupplyRawDto dto) {
        supplierSampleBizService.supplyRaw(dto);
        return CommonResult.success();
    }

    @ApiOperation("归还原料")
    @PostMapping("/returnRaw")
    public CommonResult<Void> returnRaw(@NotNull @Validated @RequestBody SampleReturnRawDto dto) {
        supplierSampleBizService.returnRaw(dto);
        return CommonResult.success();
    }
}
