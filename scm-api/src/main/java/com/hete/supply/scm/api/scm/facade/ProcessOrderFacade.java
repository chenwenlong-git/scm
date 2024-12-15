package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

/**
 * @Author: RockyHuas
 * @date: 2022/11/23 16:47
 */
public interface ProcessOrderFacade {

    /**
     * 获取总数(按单导出)
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getExportTotalsByOrder(ProcessOrderQueryByApiDto dto);

    /**
     * 获取列表(按单)
     *
     * @param dto
     * @return
     */
    CommonPageResult<ProcessOrderExportByOrderVo> getExportListByOrder(ProcessOrderQueryByApiDto dto);

    /**
     * 获取总数(按产品导出)
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getExportTotalsByItem(ProcessOrderQueryByApiDto dto);

    /**
     * 获取列表(按产品导出)
     *
     * @param dto
     * @return
     */
    CommonPageResult<ProcessOrderExportByItemVo> getExportListByItem(ProcessOrderQueryByApiDto dto);

    /**
     * 获取总数(按原料导出)
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getExportTotalsByMaterial(ProcessOrderQueryByApiDto dto);

    /**
     * 获取列表(按原料导出)
     *
     * @param dto
     * @return
     */
    CommonPageResult<ProcessOrderExportByMaterialVo> getExportListByMaterial(ProcessOrderQueryByApiDto dto);

    /**
     * 同步加工单状态
     *
     * @param dto
     * @return
     */
    CommonResult<Boolean> syncProcessOrderStatus(ProcessOrderSyncStatusDto dto);

    /**
     * 打印加工单
     *
     * @param dto
     * @return
     */
    CommonResult<ResultList<ProcessOrderPrintByWmsVo>> batchPrintByDeliveryNo(ProcessOrderBatchPrintByDeliveryNoDto dto);

    /**
     * 获取加工单缺货报表数据列表及导出信息。
     *
     * @param dto 加工单缺货报表查询参数 {@link InventoryShortageReportExportDto}
     * @return 包含报表数据列表及导出信息的通用结果对象 {@link CommonResult}
     */
    CommonResult<ExportationListResultBo<InventoryShortageReportExportVo>> getInventoryShortageReportData(InventoryShortageReportExportDto dto);

    /**
     * 获取加工单缺货报表数据总计。
     *
     * @param dto 加工单缺货报表查询参数 {@link InventoryShortageReportExportDto}
     * @return 包含报表数据总计的通用结果对象 {@link CommonResult}
     */
    CommonResult<Integer> getInventoryShortageReportTotals(InventoryShortageReportExportDto dto);


    /**
     * 查询SKU关联加工单是否存在
     *
     * @param skuExistenceRequestDto 包含SKU列表、创建开始时间和创建结束时间的请求DTO对象
     * @return 返回包含每个SKU关联的加工单是否存在的列表的结果对象
     */
    CommonResult<ResultList<SkuExistenceResponseDto>> checkSkuExistence(SkuExistenceRequestDto skuExistenceRequestDto);
}
