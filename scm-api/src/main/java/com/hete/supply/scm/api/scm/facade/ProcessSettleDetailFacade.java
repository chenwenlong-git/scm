package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleDetailExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author yanjiawei
 * Created on 2023/12/18.
 */
public interface ProcessSettleDetailFacade {
    CommonResult<ExportationListResultBo<ProcessSettleDetailExportVo>> getExportProcessSettleDetailList(GetProcessSettleOrderDetailAndScanSettleDto dto);

    CommonResult<Integer> getProcessSettleDetailExportTotals(GetProcessSettleOrderDetailAndScanSettleDto dto);
}
