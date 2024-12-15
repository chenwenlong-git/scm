package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuCompareExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2024/5/25 16:17
 */
public interface SkuProdFacade {

    CommonResult<Integer> getExportTotals(PlmSkuSearchDto dto);


    CommonResult<ExportationListResultBo<SkuProdSkuExportVo>> getExportList(PlmSkuSearchDto dto);

    CommonResult<ExportationListResultBo<SkuProdSkuCompareExportVo>> getSkuCompareExportList(PlmSkuSearchDto dto);
}
