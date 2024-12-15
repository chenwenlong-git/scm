package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.GoodsProcessImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.GoodsProcessImportationFacade;
import com.hete.supply.scm.server.scm.process.service.biz.GoodsProcessBizService;
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
public class GoodsProcessImportationFacadeImpl implements GoodsProcessImportationFacade {
    private final GoodsProcessBizService goodsProcessBizService;

    @Override
    public CommonResult<ImportationResultVo> importation(ImportationReqDto<GoodsProcessImportationDto.ImportationDetail> dto) {
        return ImportationUtil.importData(dto, goodsProcessBizService::importData);
    }


}
