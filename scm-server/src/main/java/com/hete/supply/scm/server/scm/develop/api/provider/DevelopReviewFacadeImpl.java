package com.hete.supply.scm.server.scm.develop.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.DevelopReviewSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopReviewOrderExportVo;
import com.hete.supply.scm.api.scm.facade.DevelopReviewFacade;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopExportBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2023/8/30 10:49
 */
@DubboService
@RequiredArgsConstructor
public class DevelopReviewFacadeImpl implements DevelopReviewFacade {
    private final DevelopExportBizService developExportBizService;


    @Override
    public CommonResult<Integer> getReviewExportTotals(DevelopReviewSearchDto dto) {
        return CommonResult.success(developExportBizService.getReviewExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<DevelopReviewOrderExportVo>> getReviewExportList(DevelopReviewSearchDto dto) {

        return developExportBizService.getReviewExportList(dto);
    }
}
