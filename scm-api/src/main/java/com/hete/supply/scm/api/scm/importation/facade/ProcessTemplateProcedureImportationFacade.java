package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessTemplateProcedureImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @Author: RockyHuas
 * @date: 2023/04/04 11:17
 */
public interface ProcessTemplateProcedureImportationFacade {
    /**
     * 导入工序模版
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importation(ImportationReqDto<ProcessTemplateProcedureImportationDto.ImportationDetail> dto);
}
