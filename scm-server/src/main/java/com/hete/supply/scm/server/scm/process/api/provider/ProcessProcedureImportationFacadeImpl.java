package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessProcedureImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.ProcessProcedureImportationFacade;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2023/2/5 18:28
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class ProcessProcedureImportationFacadeImpl implements ProcessProcedureImportationFacade {
    private final ProcessOrderBizService processOrderBizService;

    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<ProcessProcedureImportationDto.ImportationDetail> dto) {

        return ImportationUtil.importData(dto, processOrderBizService::importProcedureData);

    }
}
