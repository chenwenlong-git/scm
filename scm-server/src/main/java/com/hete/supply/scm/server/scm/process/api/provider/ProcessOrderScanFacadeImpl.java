package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.ProcessOrderScanQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanExportVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderScanMonthStatisticsExportVo;
import com.hete.supply.scm.api.scm.facade.ProcessOrderScanFacade;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderScanBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 17:49
 */
@DubboService
@RequiredArgsConstructor
public class ProcessOrderScanFacadeImpl implements ProcessOrderScanFacade {
    private final ProcessOrderScanBizService processOrderScanBizService;

    /**
     * 获取总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(ProcessOrderScanQueryByApiDto dto) {
        return CommonResult.success(processOrderScanBizService.getExportTotals(dto));
    }


    /**
     * 获取导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<ProcessOrderScanExportVo> getExportList(ProcessOrderScanQueryByApiDto dto) {
        return new CommonPageResult<>(processOrderScanBizService.getExportList(dto));
    }

    @Override
    public CommonResult<Integer> getMonthScanStatisticsExportTotals(ProcessOrderScanQueryByApiDto dto) {
        return CommonResult.success(processOrderScanBizService.getMonthScanStaticCount(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<ProcessOrderScanMonthStatisticsExportVo>> getMonthScanStatisticsExportList(ProcessOrderScanQueryByApiDto dto) {
        return processOrderScanBizService.getMonthStatisticsExportList(dto);
    }
}
