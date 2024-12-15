package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

public interface DevelopSampleOrderFacade {

    CommonResult<Integer> getExportTotals(DevelopSampleOrderSearchDto dto);

    CommonResult<ExportationListResultBo<DevelopSampleOrderExportVo>> getExportList(DevelopSampleOrderSearchDto dto);

}
