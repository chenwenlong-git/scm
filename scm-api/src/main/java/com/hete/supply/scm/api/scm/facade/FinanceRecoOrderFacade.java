package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.RecoOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportItemVo;
import com.hete.supply.scm.api.scm.entity.vo.RecoOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

public interface FinanceRecoOrderFacade {

    /**
     * 对账单导出统计
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/5/20 14:48
     */
    CommonResult<Integer> getExportTotals(RecoOrderSearchDto dto);

    /**
     * 对账单导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RecoOrderExportVo>>
     * @author ChenWenLong
     * @date 2024/5/20 14:48
     */
    CommonResult<ExportationListResultBo<RecoOrderExportVo>> getExportList(RecoOrderSearchDto dto);

    /**
     * 对账单详情导出统计
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/5/20 14:48
     */
    CommonResult<Integer> getExportTotalsItem(RecoOrderSearchDto dto);

    /**
     * 对账单详情导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RecoOrderExportItemVo>>
     * @author ChenWenLong
     * @date 2024/5/20 14:48
     */
    CommonResult<ExportationListResultBo<RecoOrderExportItemVo>> getExportListItem(RecoOrderSearchDto dto);

}
