package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderCreateImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderPromiseDateImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/2/5 14:48
 */
public interface ProcessOrderImportationFacade {
    /**
     * 导入加工单
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importationProcess(@Valid @NotNull ImportationReqDto<ProcessOrderCreateImportationDto> dto);


    /**
     * 导入答交时间
     *
     * @param dto 包含加工单号和答交时间的DTO
     * @return 导入结果
     */
    CommonResult<ImportationResultVo> importPromiseDate(ImportationReqDto<ProcessOrderPromiseDateImportationDto> dto);
}
