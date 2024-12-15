package com.hete.supply.scm.server.supplier.sample.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SampleReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.SampleReturnExportVo;
import com.hete.supply.scm.api.scm.facade.SampleReturnFacade;
import com.hete.supply.scm.server.supplier.sample.service.biz.SampleReturnExportService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/18 15:53
 */
@DubboService
@RequiredArgsConstructor
public class SampleReturnFacadeImpl implements SampleReturnFacade {
    private final SampleReturnExportService sampleReturnExportService;
    private final AuthBaseService authBaseService;

    @Override
    public CommonResult<Integer> getExportTotals(SampleReturnDto dto) {
        return CommonResult.success(sampleReturnExportService.getExportTotals(dto));
    }

    @Override
    public CommonPageResult<SampleReturnExportVo> getExportList(SampleReturnDto dto) {
        final CommonPageResult.PageInfo<SampleReturnExportVo> exportList = sampleReturnExportService.getExportList(dto);
        exportList.getRecords().forEach(record -> record.setReturnOrderStatus(ReceiptOrderStatus.valueOf(record.getReturnOrderStatus()).getRemark()));
        return new CommonPageResult<>(exportList);
    }

    @Override
    public CommonResult<Integer> getSupplierExportTotals(SampleReturnDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportTotals(dto);
    }

    @Override
    public CommonPageResult<SampleReturnExportVo> getSupplierExportList(SampleReturnDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        return this.getExportList(dto);
    }

}
