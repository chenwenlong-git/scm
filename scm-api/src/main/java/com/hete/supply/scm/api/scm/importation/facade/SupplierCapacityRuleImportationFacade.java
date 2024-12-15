package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierCapacityRuleImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * 供应商产能导入类
 *
 * @author yanjiawei
 * Created on 2024/8/5.
 */
public interface SupplierCapacityRuleImportationFacade {
    /**
     * @Description 导入供应商产能
     * @author yanjiawei
     * @Date 2024/8/5 16:12
     */
    CommonResult<ImportationResultVo> importCapacityRule(ImportationReqDto<SupplierCapacityRuleImportationDto> importationReqDto);
}
