package com.hete.supply.scm.server.scm.sample.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.SampleParentImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.SampleParentImportationFacade;
import com.hete.supply.scm.server.scm.sample.service.biz.SampleBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2023/3/20 15:40
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class SampleParentImportationFacadeImpl implements SampleParentImportationFacade {
    private final SampleBizService sampleBizService;

    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<SampleParentImportationDto.ImportationDetail> dto) {
        return ImportationUtil.importData(dto, sampleBizService::importParentData);
    }
}
