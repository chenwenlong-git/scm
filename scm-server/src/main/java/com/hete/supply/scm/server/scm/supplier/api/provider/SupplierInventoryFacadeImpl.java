package com.hete.supply.scm.server.scm.supplier.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SearchInventoryDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryExportVo;
import com.hete.supply.scm.api.scm.facade.SupplierInventoryFacade;
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
public class SupplierInventoryFacadeImpl implements SupplierInventoryFacade {
    private final SupplierInventoryBizService supplierInventoryBizService;


    @Override
    public CommonResult<Integer> getExportTotals(SearchInventoryDto dto) {
        return CommonResult.success(supplierInventoryBizService.getExportTotals(dto));

    }

    @Override
    public CommonResult<ExportationListResultBo<SupplierInventoryExportVo>> getExportList(SearchInventoryDto dto) {
        return supplierInventoryBizService.getExportList(dto);
    }
}
