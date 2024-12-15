package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.CostImportDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/2/26 09:49
 */
public interface CostImportationFacade {
    CommonResult<ImportationResultVo> importChangeMoData(ImportationReqDto<CostImportDto> importPlatSkuDto);

}
