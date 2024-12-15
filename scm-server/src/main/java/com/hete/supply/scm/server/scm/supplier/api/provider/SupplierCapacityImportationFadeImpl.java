package com.hete.supply.scm.server.scm.supplier.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierCapacityImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierCapacityRuleImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.SupplierCapacityImportationFacade;
import com.hete.supply.scm.api.scm.importation.facade.SupplierCapacityRuleImportationFacade;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierCapacityBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author yanjiawei
 * Created on 2024/8/5.
 */
@DubboService
@RequiredArgsConstructor
public class SupplierCapacityImportationFadeImpl implements SupplierCapacityImportationFacade {
    private final SupplierCapacityBizService supplierCapacityBizService;

    @Override
    public CommonResult<ImportationResultVo> importCapacity(ImportationReqDto<SupplierCapacityImportationDto> importationReqDto) {
        return ImportationUtil.importData(importationReqDto, supplierCapacityBizService::importCapacity);
    }
}
