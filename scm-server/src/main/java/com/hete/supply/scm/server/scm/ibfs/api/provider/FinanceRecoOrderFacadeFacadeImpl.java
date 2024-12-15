package com.hete.supply.scm.server.scm.ibfs.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportItemVo;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportVo;
import com.hete.supply.scm.api.scm.facade.FinanceRecoOrderFacade;
import com.hete.supply.scm.server.scm.ibfs.service.biz.RecoOrderBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2024/5/20 14:40
 */
@DubboService
@RequiredArgsConstructor
public class FinanceRecoOrderFacadeFacadeImpl implements FinanceRecoOrderFacade {
    private final RecoOrderBizService recoOrderBizService;

    @Override
    public CommonResult<Integer> getExportTotals(RecoOrderSearchDto dto) {
        return CommonResult.success(recoOrderBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<RecoOrderExportVo>> getExportList(RecoOrderSearchDto dto) {
        return recoOrderBizService.getExportList(dto);
    }

    @Override
    public CommonResult<Integer> getExportTotalsItem(RecoOrderSearchDto dto) {
        return CommonResult.success(recoOrderBizService.getExportTotalsItem(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<RecoOrderExportItemVo>> getExportListItem(RecoOrderSearchDto dto) {
        return recoOrderBizService.getExportListItem(dto);
    }
}
