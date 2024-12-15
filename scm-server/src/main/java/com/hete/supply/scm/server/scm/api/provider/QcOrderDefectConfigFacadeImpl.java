package com.hete.supply.scm.server.scm.api.provider;

import com.hete.supply.scm.api.scm.entity.vo.QcOrderDefectConfigExportVo;
import com.hete.supply.scm.api.scm.facade.QcOrderDefectConfigFacade;
import com.hete.supply.scm.server.scm.qc.service.biz.QcOrderConfigBizService;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2024/1/2 11:48
 */
@DubboService
@RequiredArgsConstructor
public class QcOrderDefectConfigFacadeImpl implements QcOrderDefectConfigFacade {
    private final QcOrderConfigBizService qcOrderConfigBizService;

    @Override
    public CommonResult<Integer> getQcDefectConfigExportTotals(ComPageDto dto) {
        return CommonResult.success(qcOrderConfigBizService.getQcDefectConfigExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<QcOrderDefectConfigExportVo>> getQcDefectConfigExportList(ComPageDto dto) {
        return qcOrderConfigBizService.getQcDefectConfigExportList(dto);
    }

}
