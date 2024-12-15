package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierRestImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * 供应商停工时间导入类
 *
 * @author yanjiawei
 * Created on 2024/8/5.
 */
public interface SupplierRestImportationFacade {

    /**
     * @Description 导入供应商停工时间
     * @author yanjiawei
     * @Date 2024/8/5 18:16
     */
    CommonResult<ImportationResultVo> importRestTime(ImportationReqDto<SupplierRestImportationDto> importationReqDto);
}
