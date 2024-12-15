package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.DevelopReviewSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopReviewOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2023/8/30 10:42
 */
public interface DevelopReviewFacade {
    CommonResult<Integer> getReviewExportTotals(DevelopReviewSearchDto dto);

    CommonResult<ExportationListResultBo<DevelopReviewOrderExportVo>> getReviewExportList(DevelopReviewSearchDto dto);

}
