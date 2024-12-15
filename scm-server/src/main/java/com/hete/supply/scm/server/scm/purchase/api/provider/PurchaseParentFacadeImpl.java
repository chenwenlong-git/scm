package com.hete.supply.scm.server.scm.purchase.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseSearchNewDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseParentExportVo;
import com.hete.supply.scm.api.scm.facade.PurchaseParentFacade;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseExportService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/12 15:37
 */
@DubboService
@RequiredArgsConstructor
public class PurchaseParentFacadeImpl implements PurchaseParentFacade {
    private final PurchaseExportService purchaseExportService;

    @Override
    public CommonResult<Integer> exportPurchaseParentTotals(PurchaseSearchNewDto dto) {
        return CommonResult.success(purchaseExportService.exportPurchaseParentTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PurchaseParentExportVo>> exportPurchaseParent(PurchaseSearchNewDto dto) {
        return purchaseExportService.exportPurchaseParent(dto);
    }

}
