package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessTemplateProcedureImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.ProcessTemplateProcedureImportationFacade;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessTemplateBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: RockyHuas
 * @date: 2023/04/04 11:49
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class ProcessTemplateProcedureImportationFacadeImpl implements ProcessTemplateProcedureImportationFacade {
    private final ProcessTemplateBizService processTemplateBizService;

    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<ProcessTemplateProcedureImportationDto.ImportationDetail> dto) {
        return ImportationUtil.importData(dto, processTemplateBizService::importProcedureData);
    }


}
