package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleSettleOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

public interface DevelopSampleSettleOrderFacade {

    CommonResult<Integer> getExportTotals(DevelopSampleSettleSearchDto dto);

    /**
     * 导出列表
     *
     * @param dto:
     * @return CommonPageResult<DevelopSampleSettleOrderExportVo>
     * @author ChenWenLong
     * @date 2023/8/5 10:44
     */
    CommonResult<ExportationListResultBo<DevelopSampleSettleOrderExportVo>> getExportList(DevelopSampleSettleSearchDto dto);

}
