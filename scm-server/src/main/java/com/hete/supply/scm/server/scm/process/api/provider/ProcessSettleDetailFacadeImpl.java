package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleDetailExportVo;
import com.hete.supply.scm.api.scm.facade.ProcessSettleDetailFacade;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessSettleDetailExportBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author yanjiawei
 * Created on 2023/12/18.
 */
@DubboService
@RequiredArgsConstructor
public class ProcessSettleDetailFacadeImpl implements ProcessSettleDetailFacade {

    private final ProcessSettleDetailExportBizService exportBizService;

    @Override
    public CommonResult<ExportationListResultBo<ProcessSettleDetailExportVo>> getExportProcessSettleDetailList(GetProcessSettleOrderDetailAndScanSettleDto dto) {
        return exportBizService.getExportProcessSettleDetailList(dto);
    }

    @Override
    public CommonResult<Integer> getProcessSettleDetailExportTotals(GetProcessSettleOrderDetailAndScanSettleDto dto) {
        return exportBizService.getProcessSettleDetailExportTotals(dto);
    }
}
