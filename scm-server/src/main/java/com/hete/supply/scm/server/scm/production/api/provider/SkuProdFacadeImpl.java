package com.hete.supply.scm.server.scm.production.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuCompareExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuProdSkuExportVo;
import com.hete.supply.scm.api.scm.facade.SkuProdFacade;
import com.hete.supply.scm.server.scm.production.service.biz.SkuProdBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/10/21 18:28
 */
@DubboService
@RequiredArgsConstructor
public class SkuProdFacadeImpl implements SkuProdFacade {
    private final SkuProdBizService skuProdBizService;


    @Override
    public CommonResult<Integer> getExportTotals(PlmSkuSearchDto dto) {
        return CommonResult.success(skuProdBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<SkuProdSkuExportVo>> getExportList(PlmSkuSearchDto dto) {
        return skuProdBizService.getExportList(dto);
    }

    @Override
    public CommonResult<ExportationListResultBo<SkuProdSkuCompareExportVo>> getSkuCompareExportList(PlmSkuSearchDto dto) {
        return skuProdBizService.getSkuCompareExportList(dto);
    }
}
