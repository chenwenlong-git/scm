package com.hete.supply.scm.server.scm.ibfs.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.PrepaymentSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.PrepaymentExportVo;
import com.hete.supply.scm.api.scm.facade.FinancePrepaymentFacade;
import com.hete.supply.scm.server.scm.ibfs.service.biz.PrepaymentBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/5/25 16:19
 */
@DubboService
@RequiredArgsConstructor
public class FinancePrepaymentFacadeImpl implements FinancePrepaymentFacade {
    private final PrepaymentBizService prepaymentBizService;

    @Override
    public CommonResult<Integer> getExportTotals(PrepaymentSearchDto dto) {
        return CommonResult.success(prepaymentBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<PrepaymentExportVo>> getExportList(PrepaymentSearchDto dto) {
        return prepaymentBizService.getExportList(dto);
    }
}
