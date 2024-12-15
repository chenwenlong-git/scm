package com.hete.supply.scm.server.scm.settle.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.ProcessSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleOrderExportVo;
import com.hete.supply.scm.api.scm.facade.ProcessSettleOrderFacade;
import com.hete.supply.scm.server.scm.settle.service.biz.ProcessSettleOrderBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
@RequiredArgsConstructor
public class ProcessSettleOrderFacadeImpl implements ProcessSettleOrderFacade {
    private final ProcessSettleOrderBizService processSettleOrderBizService;

    /**
     * 获取总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(ProcessSettleOrderDto dto) {
        return CommonResult.success(processSettleOrderBizService.getExportTotals(dto));
    }

    /**
     * 获取导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<ProcessSettleOrderExportVo> getExportList(ProcessSettleOrderDto dto) {
        return new CommonPageResult<>(processSettleOrderBizService.getExportList(dto));
    }
}
