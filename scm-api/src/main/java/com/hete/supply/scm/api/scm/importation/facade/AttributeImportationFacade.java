package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.AttributeImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.InitAttributeImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupSkuCraftAttrImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupSkuMaterialAttrImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

public interface AttributeImportationFacade {
    CommonResult<ImportationResultVo> initAttr(ImportationReqDto<InitAttributeImportationDto> dto);

    CommonResult<ImportationResultVo> importAttr(ImportationReqDto<AttributeImportationDto> dto);

    CommonResult<ImportationResultVo> importSupSkuMaterialAttr(ImportationReqDto<SupSkuMaterialAttrImportationDto> dto);

    CommonResult<ImportationResultVo> importSupSkuCraftAttr(ImportationReqDto<SupSkuCraftAttrImportationDto> dto);
}
