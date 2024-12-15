package com.hete.supply.scm.server.scm.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.DeductOrderImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.DeductOrderImportationFacade;
import com.hete.supply.scm.server.scm.service.biz.DeductOrderImportService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2023/11/6 18:07
 */
@DubboService
@RequiredArgsConstructor
public class DeductOrderImportationFacadeImpl implements DeductOrderImportationFacade {
    private final DeductOrderImportService deductOrderImportService;


    @Override
    public CommonResult<ImportationResultVo> importDeductOrder(ImportationReqDto<DeductOrderImportationDto> dto) {
        return ImportationUtil.importData(dto, deductOrderImportService::importDeductOrder);
    }
}
