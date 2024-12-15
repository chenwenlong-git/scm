package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.GoodsProcessImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @Author: RockyHuas
 * @date: 2022/12/13 17:17
 */
public interface GoodsProcessImportationFacade {
    /**
     * 导入商品工序
     *
     * @param dto
     * @return
     */
    CommonResult<ImportationResultVo> importation(ImportationReqDto<GoodsProcessImportationDto.ImportationDetail> dto);
}
