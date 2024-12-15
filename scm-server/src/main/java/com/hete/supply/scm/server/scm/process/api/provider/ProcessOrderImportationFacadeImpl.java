package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderCreateImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessOrderPromiseDateImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.ProcessOrderImportationFacade;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderImportService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2023/2/5 14:49
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class ProcessOrderImportationFacadeImpl implements ProcessOrderImportationFacade {
    private final ProcessOrderImportService processOrderImportService;

    @Override
    public CommonResult<ImportationResultVo> importationProcess(ImportationReqDto<ProcessOrderCreateImportationDto> dto) {
        return ImportationUtil.importData(dto, processOrderImportService::importationProcess);
    }

    @Override
    public CommonResult<ImportationResultVo> importPromiseDate(ImportationReqDto<ProcessOrderPromiseDateImportationDto> dto) {
        return ImportationUtil.importData(dto, processOrderImportService::importPromiseDate);
    }
}
