package com.hete.supply.scm.server.supplier.sample.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SampleDeliverSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.SampleDeliverExportVo;
import com.hete.supply.scm.api.scm.facade.SampleDeliverFacade;
import com.hete.supply.scm.server.supplier.sample.service.biz.SampleDeliverExportBizService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/18 23:30
 */
@DubboService
@RequiredArgsConstructor
public class SampleDeliverFacadeImpl implements SampleDeliverFacade {
    private final SampleDeliverExportBizService sampleDeliverExportBizService;
    private final AuthBaseService authBaseService;

    @Override
    public CommonResult<Integer> getExportTotals(SampleDeliverSearchDto dto) {
        return CommonResult.success(sampleDeliverExportBizService.getExportTotals(dto));
    }

    @Override
    public CommonPageResult<SampleDeliverExportVo> getExportList(SampleDeliverSearchDto dto) {
        final CommonPageResult.PageInfo<SampleDeliverExportVo> exportList = sampleDeliverExportBizService.getExportList(dto);
        exportList.getRecords().forEach(record -> record.setSampleDeliverOrderStatus(SampleDeliverOrderStatus.valueOf(record.getSampleDeliverOrderStatus()).getName()));
        return new CommonPageResult<>(exportList);
    }

    @Override
    public CommonResult<Integer> getSupplierExportTotals(SampleDeliverSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportTotals(dto);
    }

    @Override
    public CommonPageResult<SampleDeliverExportVo> getSupplierExportList(SampleDeliverSearchDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportList(dto);
    }
}
