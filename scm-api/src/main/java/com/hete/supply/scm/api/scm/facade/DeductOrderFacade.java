package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @Author: RockyHuas
 * @date: 2022/12/27 09:21
 */
public interface DeductOrderFacade {

    /**
     * 获取扣款单总数
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getExportTotals(DeductOrderQueryByApiDto dto);

    /**
     * 获取扣款单列表
     *
     * @param dto
     * @return
     */
    CommonPageResult<DeductOrderExportVo> getExportList(DeductOrderQueryByApiDto dto);

    /**
     * 供应商系统获取扣款单总数
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierExportTotals(DeductOrderQueryByApiDto dto);

    /**
     * 供应商系统获取扣款单列表
     *
     * @param dto
     * @return
     */
    CommonPageResult<DeductOrderExportVo> getSupplierExportList(DeductOrderQueryByApiDto dto);


    CommonResult<Integer> getExportSkuTotals(DeductOrderDto dto);

    CommonResult<ExportationListResultBo<DeductOrderExportSkuVo>> getExportSkuList(DeductOrderDto dto);

}
