package com.hete.supply.scm.server.scm.adjust.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.GoodsPriceImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.GoodsPriceImportationFacade;
import com.hete.supply.scm.server.scm.adjust.service.biz.GoodsPriceBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2024/1/23 10:08
 */
@DubboService
@RequiredArgsConstructor
public class GoodsPriceImportationFacadeImpl implements GoodsPriceImportationFacade {
    private final GoodsPriceBizService goodsPriceBizService;

    @Override
    public CommonResult<ImportationResultVo> importGoodsPrice(ImportationReqDto<GoodsPriceImportationDto> dto) {
        return ImportationUtil.importData(dto, goodsPriceBizService::importGoodsPrice);
    }
}
