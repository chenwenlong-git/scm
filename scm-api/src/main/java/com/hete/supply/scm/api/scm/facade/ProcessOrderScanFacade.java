package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.ProcessOrderScanQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanExportVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanMonthStatisticsExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @Author: RockyHuas
 * @date: 2022/11/28 11:28
 */
public interface ProcessOrderScanFacade {

    /**
     * 获取商品工序总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getExportTotals(ProcessOrderScanQueryByApiDto dto);

    /**
     * 获取商品工序导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<ProcessOrderScanExportVo> getExportList(ProcessOrderScanQueryByApiDto dto);

    /**
     * 获取月度扫码统计导出的总数。
     *
     * @param dto 包含查询参数的数据传输对象
     * @return 包含月度扫描统计导出总数的通用结果对象
     */
    CommonResult<Integer> getMonthScanStatisticsExportTotals(ProcessOrderScanQueryByApiDto dto);

    /**
     * 获取月度扫码统计导出列表。
     *
     * @param dto 包含查询参数的数据传输对象
     * @return 包含月度扫描统计导出列表的通用分页结果对象
     */
    CommonResult<ExportationListResultBo<ProcessOrderScanMonthStatisticsExportVo>> getMonthScanStatisticsExportList(ProcessOrderScanQueryByApiDto dto);
}
