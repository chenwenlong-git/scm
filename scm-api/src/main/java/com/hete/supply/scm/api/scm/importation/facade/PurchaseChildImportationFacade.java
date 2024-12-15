package com.hete.supply.scm.api.scm.importation.facade;

import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildConfirmImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildEditImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildPlanConfirmImportationDto;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/19 22:04
 */
public interface PurchaseChildImportationFacade {
    /**
     * 导入修改子单数据
     *
     * @param importPlatSkuDto
     * @return
     */
    CommonResult<ImportationResultVo> importEditPurchaseChild(ImportationReqDto<PurchaseChildEditImportationDto> importPlatSkuDto);

    /**
     * 导入批量计划确认采购子单
     *
     * @param importPlatSkuDto
     * @return
     */
    CommonResult<ImportationResultVo> importPlanConfirmPurchaseChild(ImportationReqDto<PurchaseChildPlanConfirmImportationDto> importPlatSkuDto);

    /**
     * 导入批量采购确认采购子单(大货确认/加工确认)
     *
     * @param importPlatSkuDto
     * @return
     */
    CommonResult<ImportationResultVo> importPurchaseConfirmPurchaseChild(ImportationReqDto<PurchaseChildConfirmImportationDto> importPlatSkuDto);

}
