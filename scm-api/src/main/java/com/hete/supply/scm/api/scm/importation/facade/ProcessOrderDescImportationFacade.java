package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderDescImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author RockyHuas
 * @date 2023/2/5 14:48
 */
public interface ProcessOrderDescImportationFacade {
    /**
     * 导入加工描述
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importation(ImportationReqDto<ProcessOrderDescImportationDto.ImportationDetail> dto);

}
