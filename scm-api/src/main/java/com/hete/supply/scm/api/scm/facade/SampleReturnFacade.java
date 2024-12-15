package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SampleReturnDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleReturnExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @author weiwenxin
 * @date 2022/12/18 15:44
 */
public interface SampleReturnFacade {
    CommonResult<Integer> getExportTotals(SampleReturnDto dto);

    CommonPageResult<SampleReturnExportVo> getExportList(SampleReturnDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierExportTotals(SampleReturnDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonPageResult<SampleReturnExportVo> getSupplierExportList(SampleReturnDto dto);
}
