package com.hete.supply.scm.server.scm.settle.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportVo;
import com.hete.supply.scm.api.scm.facade.DeductOrderFacade;
import com.hete.supply.scm.server.scm.settle.service.biz.DeductOrderBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: RockyHuas
 * @date: 2022/12/216 17:49
 */
@DubboService
@RequiredArgsConstructor
public class DeductOrderFacadeImpl implements DeductOrderFacade {
    private final DeductOrderBizService deductOrderBizService;

    /**
     * 获取扣款单总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(DeductOrderQueryByApiDto dto) {
        return CommonResult.success(deductOrderBizService.getExportTotals(dto));
    }


    /**
     * 获取扣款单导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<DeductOrderExportVo> getExportList(DeductOrderQueryByApiDto dto) {
        return new CommonPageResult<>(deductOrderBizService.getExportList(dto));
    }

    /**
     * 供应商系统获取扣款单总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getSupplierExportTotals(DeductOrderQueryByApiDto dto) {
        return CommonResult.success(deductOrderBizService.getSupplierExportTotals(dto));
    }


    /**
     * 供应商系统获取扣款单导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<DeductOrderExportVo> getSupplierExportList(DeductOrderQueryByApiDto dto) {
        return new CommonPageResult<>(deductOrderBizService.getSupplierExportList(dto));
    }


    @Override
    public CommonResult<Integer> getExportSkuTotals(DeductOrderDto dto) {
        return CommonResult.success(deductOrderBizService.getExportSkuTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<DeductOrderExportSkuVo>> getExportSkuList(DeductOrderDto dto) {
        return deductOrderBizService.getExportSkuList(dto);
    }
}
