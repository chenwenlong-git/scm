package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.SkuCycleImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierProductImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;


/**
 * @author ChenWenLong
 */
public interface SupplierProductImportationFacade {
    /**
     * 导入供应商产品对照关系
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importation(ImportationReqDto<SupplierProductImportationDto.ImportationDetail> dto);

    /**
     * 导入sku生产周期
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> skuCycleImportation(ImportationReqDto<SkuCycleImportationDto> dto);
}
