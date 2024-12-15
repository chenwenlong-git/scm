package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.ProcessMaterialImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.ProcessMaterialImportationFacade;
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
 * @date 2023/2/5 17:46
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class ProcessMaterialImportationFacadeImpl implements ProcessMaterialImportationFacade {
    private final ProcessOrderBizService processOrderBizService;

    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<ProcessMaterialImportationDto.ImportationDetail> dto) {

        return ImportationUtil.importData(dto, processOrderBizService::importMaterialData);

    }
}
