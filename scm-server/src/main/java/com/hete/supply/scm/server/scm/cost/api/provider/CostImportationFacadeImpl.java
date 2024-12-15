package com.hete.supply.scm.server.scm.cost.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.CostImportDto;
import com.hete.supply.scm.api.scm.importation.facade.CostImportationFacade;
import com.hete.supply.scm.server.scm.cost.service.biz.CostOfGoodsImportService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/2/26 10:05
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class CostImportationFacadeImpl implements CostImportationFacade {
    private final CostOfGoodsImportService costOfGoodsImportService;

    @Override
    public CommonResult<ImportationResultVo> importChangeMoData(ImportationReqDto<CostImportDto> dto) {
        return ImportationUtil.importData(dto, costOfGoodsImportService::importChangeMoData);
    }
}
