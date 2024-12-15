package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.GetRepairOrderProcessResultInfoDto;
import com.hete.supply.scm.api.scm.entity.dto.GetRepairOrderStatusBatchDto;
import com.hete.supply.scm.api.scm.entity.dto.RepairOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderProcessResultVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderResultExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderStatusVo;
import com.hete.supply.scm.api.scm.facade.RepairOrderFacade;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2024/1/8 15:11
 */
@DubboService
@RequiredArgsConstructor
public class RepairOrderFacadeImpl implements RepairOrderFacade {
    private final RepairOrderBizService repairOrderBizService;

    /**
     * 提供PLM查询返修单状态
     *
     * @param dto:
     * @return CommonResult<RepairOrderStatusVo>
     * @author ChenWenLong
     * @date 2024/1/9 16:32
     */
    @Override
    public CommonResult<RepairOrderStatusVo> getRepairOrderStatusByPlanNos(GetRepairOrderStatusBatchDto dto) {
        return CommonResult.success(repairOrderBizService.getRepairOrderStatusByPlanNos(dto));
    }

    /**
     * 提供PLM查询返修单完成sku
     *
     * @param dto:
     * @return CommonResult<RepairOrderStatusVo>
     * @author ChenWenLong
     * @date 2024/1/9 16:32
     */
    @Override
    public CommonResult<RepairOrderProcessResultVo> getRepairOrderProcessResultByRepairNo(GetRepairOrderProcessResultInfoDto dto) {
        return CommonResult.success(repairOrderBizService.getRepairOrderProcessResultByRepairNo(dto));
    }

    @Override
    public CommonResult<Integer> getExportTotals(RepairOrderSearchDto dto) {
        return CommonResult.success(repairOrderBizService.getExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<RepairOrderExportVo>> getExportList(RepairOrderSearchDto dto) {
        return repairOrderBizService.getExportList(dto);
    }

    @Override
    public CommonResult<Integer> getResultExportTotals(RepairOrderSearchDto dto) {
        return CommonResult.success(repairOrderBizService.getResultExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<RepairOrderResultExportVo>> getResultExportList(RepairOrderSearchDto dto) {
        return repairOrderBizService.getResultExportList(dto);
    }
}
