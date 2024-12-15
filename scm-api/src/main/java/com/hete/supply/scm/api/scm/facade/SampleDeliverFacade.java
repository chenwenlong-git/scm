package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SampleDeliverSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleDeliverExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/18 22:47
 */
public interface SampleDeliverFacade {
    CommonResult<Integer> getExportTotals(SampleDeliverSearchDto dto);

    CommonPageResult<SampleDeliverExportVo> getExportList(SampleDeliverSearchDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierExportTotals(SampleDeliverSearchDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonPageResult<SampleDeliverExportVo> getSupplierExportList(SampleDeliverSearchDto dto);
}
