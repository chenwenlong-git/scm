package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierSubjectImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;


public interface SupplierImportationFacade {
    /**
     * 导入供应商
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importation(ImportationReqDto<SupplierImportationDto.ImportationDetail> dto);

    /**
     * 导入供应商主体信息
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importSupplierSubject(ImportationReqDto<SupplierSubjectImportationDto> dto);
}
