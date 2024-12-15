package com.hete.supply.scm.server.scm.stockup.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.StockUpSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.StockUpExportVo;
import com.hete.supply.scm.api.scm.entity.vo.StockUpItemExportVo;
import com.hete.supply.scm.api.scm.facade.StockUpFacade;
import com.hete.supply.scm.server.scm.stockup.service.biz.StockUpBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/1/22 18:32
 */
@DubboService
@RequiredArgsConstructor
public class StockUpFacadeImpl implements StockUpFacade {
    private final StockUpBizService stockUpBizService;

    @Override
    public CommonResult<Integer> getExportTotals(StockUpSearchDto dto) {
        return CommonResult.success(stockUpBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<StockUpExportVo>> getExportList(StockUpSearchDto dto) {
        return stockUpBizService.getExportList(dto);
    }

    @Override
    public CommonResult<Integer> getItemExportTotals(StockUpSearchDto dto) {
        return CommonResult.success(stockUpBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<StockUpItemExportVo>> getItemExportList(StockUpSearchDto dto) {
        return stockUpBizService.getItemExportList(dto);
    }
}
