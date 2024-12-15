package com.hete.supply.scm.server.scm.production.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.AttributeImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.InitAttributeImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupSkuCraftAttrImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupSkuMaterialAttrImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.AttributeImportationFacade;
import com.hete.supply.scm.server.scm.production.service.biz.AttributeBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/2/26 10:05
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class AttributeImportationFacadeImpl implements AttributeImportationFacade {

    private final AttributeBizService attributeBizService;

    @Override
    public CommonResult<ImportationResultVo> initAttr(ImportationReqDto<InitAttributeImportationDto> dto) {
        return ImportationUtil.importData(dto, attributeBizService::initAttr);
    }

    @Override
    public CommonResult<ImportationResultVo> importAttr(ImportationReqDto<AttributeImportationDto> dto) {
        return ImportationUtil.importData(dto, attributeBizService::importAttr);
    }

    @Override
    public CommonResult<ImportationResultVo> importSupSkuMaterialAttr(ImportationReqDto<SupSkuMaterialAttrImportationDto> dto) {
        return ImportationUtil.importData(dto, attributeBizService::importSupSkuMaterialAttr);
    }

    @Override
    public CommonResult<ImportationResultVo> importSupSkuCraftAttr(ImportationReqDto<SupSkuCraftAttrImportationDto> dto) {
        return ImportationUtil.importData(dto, attributeBizService::importSupSkuCraftAttr);
    }
}
