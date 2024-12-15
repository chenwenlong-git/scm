package com.hete.supply.scm.server.scm.sample.api.provider;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SamplePurchaseSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SampleSkuListDto;
import com.hete.supply.scm.api.scm.entity.dto.SpuDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleNoAndStatusVo;
import com.hete.supply.scm.api.scm.facade.SampleChildFacade;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleExportService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/12 00:34
 */
@DubboService
@RequiredArgsConstructor
public class SampleChildFacadeImpl implements SampleChildFacade {
    private final SampleExportService sampleExportService;
    private final SampleBaseService sampleBaseService;
    private final AuthBaseService authBaseService;

    @Override
    public CommonResult<Integer> getExportTotals(SamplePurchaseSearchDto dto) {
        return CommonResult.success(sampleExportService.getSampleChildExportTotals(dto));
    }

    @Override
    public CommonPageResult<SampleChildExportVo> getExportList(SamplePurchaseSearchDto dto) {
        return new CommonPageResult<>(sampleExportService.getSampleChildExportList(dto));
    }

    @Override
    public CommonResult<ResultList<SampleNoAndStatusVo>> getSampleNoAndStatusBySpu(SpuDto dto) {
        return CommonResult.successForList(sampleBaseService.getSampleNoAndStatusBySpu(dto));
    }

    @Override
    public CommonResult<ResultList<SampleInfoVo>> getSampleInfoListBySkuList(SampleSkuListDto dto) {
        return CommonResult.successForList(sampleBaseService.getSampleInfoBySkuList(dto));
    }

    @Override
    public CommonResult<Integer> getSupplierExportTotals(SamplePurchaseSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtils.isEmpty(dto.getSampleOrderStatusList())) {
            dto.setSampleOrderStatusList(SampleOrderStatus.getSupplierAllStatusList());
        }
        return this.getExportTotals(dto);
    }

    @Override
    public CommonPageResult<SampleChildExportVo> getSupplierExportList(SamplePurchaseSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtils.isEmpty(dto.getSampleOrderStatusList())) {
            dto.setSampleOrderStatusList(SampleOrderStatus.getSupplierAllStatusList());
        }
        return this.getExportList(dto);
    }
}
