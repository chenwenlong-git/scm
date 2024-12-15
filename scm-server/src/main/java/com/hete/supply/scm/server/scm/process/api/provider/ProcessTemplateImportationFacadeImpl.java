package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessTemplateImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.ProcessTemplateImportationFacade;
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
 * @date: 2022/11/21 17:49
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class ProcessTemplateImportationFacadeImpl implements ProcessTemplateImportationFacade {
    private final ProcessTemplateBizService processTemplateBizService;

    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<ProcessTemplateImportationDto.ImportationDetail> dto) {
        return ImportationUtil.importData(dto, processTemplateBizService::importData);
    }


}
