package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.SampleParentImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2023/3/20 15:42
 */
public interface SampleParentImportationFacade {
    CommonResult<ImportationResultVo> importation(ImportationReqDto<SampleParentImportationDto.ImportationDetail> dto);

}
