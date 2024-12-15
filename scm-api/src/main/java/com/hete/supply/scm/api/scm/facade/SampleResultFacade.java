package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SampleChildOrderResultSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildResultExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @author ChenWenLong
 * @date 2023/5/15 16:40
 */
public interface SampleResultFacade {

    /**
     * 样品单选样结果导出
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getExportTotals(SampleChildOrderResultSearchDto dto);

    /**
     * 样品单选样结果导出
     *
     * @param dto
     * @return
     */
    CommonPageResult<SampleChildResultExportVo> getExportList(SampleChildOrderResultSearchDto dto);
}
