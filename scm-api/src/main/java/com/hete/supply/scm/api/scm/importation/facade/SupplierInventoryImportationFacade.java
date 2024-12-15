package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierInventoryImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/1/22 21:45
 */
public interface SupplierInventoryImportationFacade {
    CommonResult<ImportationResultVo> importSupplierInventory(ImportationReqDto<SupplierInventoryImportationDto> dto);

}
