package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.DeductOrderImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author ChenWenLong
 * @date 2023/11/6 17:33
 */
public interface DeductOrderImportationFacade {
    /**
     * 导入生成扣款单
     *
     * @param importDto
     * @return
     */
    CommonResult<ImportationResultVo> importDeductOrder(ImportationReqDto<DeductOrderImportationDto> importDto);

}
