package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseParentImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/19 22:04
 */
public interface PurchaseParentImportationFacade {
    CommonResult<ImportationResultVo> importParentData(ImportationReqDto<PurchaseParentImportationDto> dto);

}
