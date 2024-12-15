package com.hete.supply.scm.server.scm.stockup.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.StockUpImportDto;
import com.hete.supply.scm.api.scm.importation.facade.StockUpImportationFacade;
import com.hete.supply.scm.server.scm.stockup.service.biz.StockUpBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/1/29 10:25
 */
@Slf4j
@DubboService
@RequiredArgsConstructor
public class StockUpImportationFacadeImpl implements StockUpImportationFacade {
    private final StockUpBizService stockUpBizService;


    @Override
    public CommonResult<ImportationResultVo> importCreateStockUp(ImportationReqDto<StockUpImportDto> dto) {
        return ImportationUtil.importData(dto, stockUpBizService::importCreateStockUp);


    }
}
