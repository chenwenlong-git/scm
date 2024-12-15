package com.hete.supply.scm.server.scm.supplier.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.SkuCycleImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierProductImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.SupplierProductImportationFacade;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierProductCompareBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;


/**
 * @author ChenWenLong
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class SupplierProductImportationFacadeImpl implements SupplierProductImportationFacade {
    private final SupplierProductCompareBizService supplierProductCompareBizService;

    /**
     * 导入供应商产品对照关系
     *
     * @author ChenWenLong
     * @date 2023/5/25 17:41
     */
    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<SupplierProductImportationDto.ImportationDetail> dto) {
        return ImportationUtil.importData(dto, supplierProductCompareBizService::importData);
    }

    /**
     * 导入sku生产周期
     *
     * @author ChenWenLong
     * @date 2023/5/25 17:41
     */
    @Override
    public CommonResult<ImportationResultVo> skuCycleImportation(ImportationReqDto<SkuCycleImportationDto> dto) {
        return ImportationUtil.importData(dto, supplierProductCompareBizService::importCycleData);
    }

}
