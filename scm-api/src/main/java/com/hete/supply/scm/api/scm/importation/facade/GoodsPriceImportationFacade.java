package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.GoodsPriceImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;


public interface GoodsPriceImportationFacade {
    /**
     * 导入商品价格
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importGoodsPrice(ImportationReqDto<GoodsPriceImportationDto> dto);
}
