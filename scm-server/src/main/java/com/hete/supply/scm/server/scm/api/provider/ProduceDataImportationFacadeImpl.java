package com.hete.supply.scm.server.scm.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrImportDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.*;
import com.hete.supply.scm.api.scm.importation.facade.ProduceDataImportationFacade;
import com.hete.supply.scm.server.scm.production.service.biz.SkuProdBizService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.biz.ProduceDataBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2024/1/23 10:08
 */
@DubboService
@RequiredArgsConstructor
public class ProduceDataImportationFacadeImpl implements ProduceDataImportationFacade {
    private final ProduceDataBaseService produceDataBaseService;
    private final ProduceDataBizService produceDataBizService;
    private final SkuProdBizService skuProdBizService;


    @Override
    public CommonResult<ImportationResultVo> importProduceDataItemRaw(ImportationReqDto<ProduceDataItemRawImportationDto> dto) {
        return ImportationUtil.importData(dto, produceDataBaseService::importProduceDataItemRaw);
    }

    @Override
    public CommonResult<ImportationResultVo> importProduceDataSpec(ImportationReqDto<ProduceDataSpecImportationDto> dto) {
        return ImportationUtil.importData(dto, produceDataBizService::importProduceDataSpec);
    }

    @Override
    public CommonResult<ImportationResultVo> importProduceDataItemSupplier(ImportationReqDto<ProduceDataItemSupplierImportationDto> dto) {
        return ImportationUtil.importData(dto, produceDataBizService::importProduceDataItemSupplier);
    }

    @Override
    public CommonResult<ImportationResultVo> importProduceDataAttr(ImportationReqDto<ProduceDataAttrImportationDto> dto) {
        return ImportationUtil.importData(dto, produceDataBizService::importProduceDataAttr);
    }

    @Override
    public CommonResult<ImportationResultVo> importProdDataAttr(ImportationReqDto<ProduceDataAttrImportDto> dto) {
        return ImportationUtil.importData(dto, skuProdBizService::importProdDataAttr);
    }

    @Override
    public CommonResult<ImportationResultVo> importProduceDataRawProcess(ImportationReqDto<ProduceDataRawProcessImportDto> dto) {
        return ImportationUtil.importData(dto, skuProdBizService::importProduceDataRawProcess);
    }
}
