package com.hete.supply.scm.server.scm.supplier.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.InventoryRecordDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryRecordExportVo;
import com.hete.supply.scm.api.scm.facade.SupplierInventoryRecordFacade;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierInventoryBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/1/22 20:31
 */
@DubboService
@RequiredArgsConstructor
public class SupplierInventoryRecordFacadeImpl implements SupplierInventoryRecordFacade {
    private final SupplierInventoryBizService supplierInventoryBizService;


    @Override
    public CommonResult<Integer> getExportTotals(InventoryRecordDto dto) {
        return CommonResult.success(supplierInventoryBizService.getRecordExportTotals(dto));

    }

    @Override
    public CommonResult<ExportationListResultBo<SupplierInventoryRecordExportVo>> getExportList(InventoryRecordDto dto) {
        return supplierInventoryBizService.getRecordExportList(dto);
    }
}