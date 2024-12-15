package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessMaterialImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2023/2/5 14:48
 */
public interface ProcessMaterialImportationFacade {
    /**
     * 导入工序模版
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importation(ImportationReqDto<ProcessMaterialImportationDto.ImportationDetail> dto);

}
