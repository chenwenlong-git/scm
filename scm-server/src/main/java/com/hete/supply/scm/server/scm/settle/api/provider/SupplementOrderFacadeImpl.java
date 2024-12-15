package com.hete.supply.scm.server.scm.settle.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportVo;
import com.hete.supply.scm.api.scm.facade.SupplementOrderFacade;
import com.hete.supply.scm.server.scm.settle.service.biz.SupplementOrderBizService;
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
public class SupplementOrderFacadeImpl implements SupplementOrderFacade {
    private final SupplementOrderBizService supplementOrderBizService;

    /**
     * 获取补款单总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(SupplementOrderQueryByApiDto dto) {
        return CommonResult.success(supplementOrderBizService.getExportTotals(dto));
    }


    /**
     * 获取补款单导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<SupplementOrderExportVo> getExportList(SupplementOrderQueryByApiDto dto) {
        return new CommonPageResult<>(supplementOrderBizService.getExportList(dto));
    }

    /**
     * 供应商系统获取补款单总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getSupplierExportTotals(SupplementOrderQueryByApiDto dto) {
        return CommonResult.success(supplementOrderBizService.getSupplierExportTotals(dto));
    }


    /**
     * 供应商系统获取补款单导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<SupplementOrderExportVo> getSupplierExportList(SupplementOrderQueryByApiDto dto) {
        return new CommonPageResult<>(supplementOrderBizService.getSupplierExportList(dto));
    }

    @Override
    public CommonResult<Integer> getExportSkuTotals(SupplementOrderDto dto) {
        return CommonResult.success(supplementOrderBizService.getExportSkuTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<SupplementOrderExportSkuVo>> getExportSkuList(SupplementOrderDto dto) {
        return supplementOrderBizService.getExportSkuList(dto);
    }
}
