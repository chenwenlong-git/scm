package com.hete.supply.scm.server.scm.purchase.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildConfirmImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildEditImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildPlanConfirmImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.PurchaseChildImportationFacade;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseBizService;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseImportService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2022/12/19 23:05
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class PurchaseChildImportationFacadeImpl implements PurchaseChildImportationFacade {
    private final PurchaseBizService purchaseBizService;
    private final PurchaseImportService purchaseImportService;

    @Override
    public CommonResult<ImportationResultVo> importEditPurchaseChild(ImportationReqDto<PurchaseChildEditImportationDto> dto) {
        return ImportationUtil.importData(dto, purchaseBizService::importEditPurchaseChild);
    }

    @Override
    public CommonResult<ImportationResultVo> importPlanConfirmPurchaseChild(ImportationReqDto<PurchaseChildPlanConfirmImportationDto> dto) {
        return ImportationUtil.importData(dto, purchaseImportService::importPlanConfirmPurchaseChild);
    }

    @Override
    public CommonResult<ImportationResultVo> importPurchaseConfirmPurchaseChild(ImportationReqDto<PurchaseChildConfirmImportationDto> dto) {
        return ImportationUtil.importData(dto, purchaseImportService::importPurchaseConfirmPurchaseChild);

    }
}
