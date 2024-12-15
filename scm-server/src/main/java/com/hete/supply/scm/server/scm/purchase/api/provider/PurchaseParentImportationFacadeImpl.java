package com.hete.supply.scm.server.scm.purchase.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseParentImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.PurchaseParentImportationFacade;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseBizService;
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
public class PurchaseParentImportationFacadeImpl implements PurchaseParentImportationFacade {
    private final PurchaseBizService purchaseBizService;

    @Override
    public CommonResult<ImportationResultVo> importParentData(ImportationReqDto<PurchaseParentImportationDto> dto) {
        return ImportationUtil.importData(dto, purchaseBizService::importParentData);
    }


}
