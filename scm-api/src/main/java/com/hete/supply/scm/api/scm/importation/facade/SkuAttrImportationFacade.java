package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.SkuAttrImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/11/1 15:56
 */
public interface SkuAttrImportationFacade {
    /**
     * 导入定价
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importSkuAttr(ImportationReqDto<SkuAttrImportationDto> dto);
}
