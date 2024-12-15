package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.DefectHandingSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.DefectHandingExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * 次品记录
 *
 * @author ChenWenLong
 * @date 2024/3/28 16:34
 */
public interface DefectHandingFacade {


    /**
     * 导出次品记录统计
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/3/28 16:52
     */
    CommonResult<Integer> getExportTotals(DefectHandingSearchDto dto);


    /**
     * 导出次品记录列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < DefectHandingExportVo>>
     * @author ChenWenLong
     * @date 2024/3/28 16:53
     */
    CommonResult<ExportationListResultBo<DefectHandingExportVo>> getExportList(DefectHandingSearchDto dto);


}
