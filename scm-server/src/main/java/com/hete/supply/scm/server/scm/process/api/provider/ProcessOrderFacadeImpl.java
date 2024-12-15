package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.supply.scm.api.scm.facade.ProcessOrderFacade;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.supply.scm.server.scm.service.biz.ProcessOrderExportBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: RockyHuas
 * @date: 2022/11/23 16:56
 */
@DubboService
@RequiredArgsConstructor
public class ProcessOrderFacadeImpl implements ProcessOrderFacade {
    private final ProcessOrderBizService processOrderBizService;
    private final ProcessOrderExportBizService exportBizService;

    /**
     * 获取总数(按单导出)
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotalsByOrder(ProcessOrderQueryByApiDto dto) {
        return CommonResult.success(processOrderBizService.getExportTotalsByOrder(dto));
    }


    /**
     * 获取列表(按单)
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<ProcessOrderExportByOrderVo> getExportListByOrder(ProcessOrderQueryByApiDto dto) {
        return new CommonPageResult<>(processOrderBizService.getExportListByOrder(dto));
    }

    /**
     * 获取总数(按sku导出)
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotalsByItem(ProcessOrderQueryByApiDto dto) {
        return CommonResult.success(processOrderBizService.getExportTotalsByItem(dto));
    }


    /**
     * 获取列表(按sku)
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<ProcessOrderExportByItemVo> getExportListByItem(ProcessOrderQueryByApiDto dto) {
        return new CommonPageResult<>(processOrderBizService.getExportListByItem(dto));
    }

    /**
     * 获取总数(按原料导出)
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotalsByMaterial(ProcessOrderQueryByApiDto dto) {
        return CommonResult.success(processOrderBizService.getExportTotalsByMaterial(dto));
    }


    /**
     * 获取列表(按原料)
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<ProcessOrderExportByMaterialVo> getExportListByMaterial(ProcessOrderQueryByApiDto dto) {
        return new CommonPageResult<>(processOrderBizService.getExportListByMaterial(dto));
    }

    /**
     * 同步加工单状态
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Boolean> syncProcessOrderStatus(ProcessOrderSyncStatusDto dto) {
        return CommonResult.success(processOrderBizService.syncProcessOrderStatus(dto));
    }


    /**
     * 同步加工单状态
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<ResultList<ProcessOrderPrintByWmsVo>> batchPrintByDeliveryNo(ProcessOrderBatchPrintByDeliveryNoDto dto) {
        return CommonResult.successForList(processOrderBizService.batchPrintByDeliveryNo(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<InventoryShortageReportExportVo>> getInventoryShortageReportData(InventoryShortageReportExportDto dto) {
        return exportBizService.getInventoryShortageReportData(dto);
    }

    @Override
    public CommonResult<Integer> getInventoryShortageReportTotals(InventoryShortageReportExportDto dto) {
        return exportBizService.getInventoryShortageReportTotals(dto);
    }

    @Override
    public CommonResult<ResultList<SkuExistenceResponseDto>> checkSkuExistence(SkuExistenceRequestDto dto) {
        return CommonResult.successForList(processOrderBizService.checkSkuExistence(dto));
    }


}
