package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.DevelopPricingOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopPricingOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

public interface DevelopPricingOrderFacade {

    CommonResult<Integer> getExportTotals(DevelopPricingOrderSearchDto dto);

    CommonResult<ExportationListResultBo<DevelopPricingOrderExportVo>> getExportList(DevelopPricingOrderSearchDto dto);

}
