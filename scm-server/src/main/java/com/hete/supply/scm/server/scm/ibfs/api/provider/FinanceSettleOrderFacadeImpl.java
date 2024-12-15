package com.hete.supply.scm.server.scm.ibfs.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderItemExportVo;
import com.hete.supply.scm.api.scm.facade.FinanceSettleOrderFacade;
import com.hete.supply.scm.server.scm.ibfs.service.biz.FinanceSettleOrderBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author yanjiawei
 * Created on 2024/5/26.
 */
@DubboService
@RequiredArgsConstructor
public class FinanceSettleOrderFacadeImpl implements FinanceSettleOrderFacade {
    private final FinanceSettleOrderBizService financeSettleOrderBizService;

    @Override
    public CommonResult<Integer> getExportSettleItemTotalCount(SearchSettleOrderDto dto) {
        return CommonResult.success(financeSettleOrderBizService.getExportSettleItemTotalCount(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<FinanceSettleOrderItemExportVo>> getExportSettleItemList(SearchSettleOrderDto dto) {
        return financeSettleOrderBizService.getExportSettleItemList(dto);
    }

    @Override
    public CommonResult<Integer> getExportSettleOrderTotalCount(SearchSettleOrderDto dto) {
        return CommonResult.success(financeSettleOrderBizService.getExportSettleOrderTotalCount(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<FinanceSettleOrderExportVo>> getExportSettleList(SearchSettleOrderDto dto) {
        return financeSettleOrderBizService.getExportSettleList(dto);
    }
}
