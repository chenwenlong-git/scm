package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @Author: RockyHuas
 * @date: 2022/11/28 16:21
 */
public interface SupplementOrderFacade {

    /**
     * 获取补款单总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getExportTotals(SupplementOrderQueryByApiDto dto);

    /**
     * 获取补款单列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<SupplementOrderExportVo> getExportList(SupplementOrderQueryByApiDto dto);

    /**
     * 供应商系统获取补款单总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getSupplierExportTotals(SupplementOrderQueryByApiDto dto);

    /**
     * 供应商系统获取补款单列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<SupplementOrderExportVo> getSupplierExportList(SupplementOrderQueryByApiDto dto);

    public CommonResult<Integer> getExportSkuTotals(SupplementOrderDto dto);

    public CommonResult<ExportationListResultBo<SupplementOrderExportSkuVo>> getExportSkuList(SupplementOrderDto dto);

}
