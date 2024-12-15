package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.GoodsPriceDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsPriceExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

public interface GoodsPriceFacade {


    CommonResult<Integer> getExportTotals(GoodsPriceDto dto);


    CommonResult<ExportationListResultBo<GoodsPriceExportVo>> getExportList(GoodsPriceDto dto);


}
