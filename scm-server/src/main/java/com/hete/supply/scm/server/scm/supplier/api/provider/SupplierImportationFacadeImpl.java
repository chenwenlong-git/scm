package com.hete.supply.scm.server.scm.supplier.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierSubjectImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.SupplierImportationFacade;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 供应商导入
 *
 * @author ChenWenLong
 * @date 2022/12/26 11:38
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class SupplierImportationFacadeImpl implements SupplierImportationFacade {
    private final SupplierBizService supplierBizService;

    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<SupplierImportationDto.ImportationDetail> dto) {
        return ImportationUtil.importData(dto, supplierBizService::importData);
    }

    @Override
    public CommonResult<ImportationResultVo> importSupplierSubject(ImportationReqDto<SupplierSubjectImportationDto> dto) {
        return ImportationUtil.importData(dto, supplierBizService::importSupplierSubject);
    }


}
