package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.vo.QcOrderDefectConfigExportVo;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author ChenWenLong
 * @date 2024/1/2 11:26
 */
public interface QcOrderDefectConfigFacade {


    /**
     * 导出总数
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/1/2 11:27
     */
    CommonResult<Integer> getQcDefectConfigExportTotals(ComPageDto dto);

    /**
     * 导出列表
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/1/2 11:27
     */
    CommonResult<ExportationListResultBo<QcOrderDefectConfigExportVo>> getQcDefectConfigExportList(ComPageDto dto);

}
