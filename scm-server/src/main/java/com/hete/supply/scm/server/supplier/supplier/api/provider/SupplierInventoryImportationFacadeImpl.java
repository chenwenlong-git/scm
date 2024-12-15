package com.hete.supply.scm.server.supplier.supplier.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierInventoryImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.SupplierInventoryImportationFacade;
import com.hete.supply.scm.server.supplier.supplier.service.biz.SupplierInventoryImportService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/1/22 21:47
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class SupplierInventoryImportationFacadeImpl implements SupplierInventoryImportationFacade {
    private final SupplierInventoryImportService supplierInventoryImportService;

    @Override
    public CommonResult<ImportationResultVo> importSupplierInventory(ImportationReqDto<SupplierInventoryImportationDto> dto) {
        return ImportationUtil.importData(dto, supplierInventoryImportService::importSupplierInventory);
    }
}
