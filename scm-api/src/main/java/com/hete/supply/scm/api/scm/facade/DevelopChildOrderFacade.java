package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopChildOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

public interface DevelopChildOrderFacade {

    CommonResult<Integer> getExportTotals(DevelopChildSearchDto dto);

    CommonResult<ExportationListResultBo<DevelopChildOrderExportVo>> getExportList(DevelopChildSearchDto dto);

}
