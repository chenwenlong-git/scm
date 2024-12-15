package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SearchSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.FinanceSettleOrderItemExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author yanjiawei
 * Created on 2024/5/26.
 */
public interface FinanceSettleOrderFacade {


    CommonResult<Integer> getExportSettleItemTotalCount(SearchSettleOrderDto dto);


    CommonResult<ExportationListResultBo<FinanceSettleOrderItemExportVo>> getExportSettleItemList(SearchSettleOrderDto dto);

    CommonResult<Integer> getExportSettleOrderTotalCount(SearchSettleOrderDto dto);

    CommonResult<ExportationListResultBo<FinanceSettleOrderExportVo>> getExportSettleList(SearchSettleOrderDto dto);
}
