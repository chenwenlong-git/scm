package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.GetRepairOrderProcessResultInfoDto;
import com.hete.supply.scm.api.scm.entity.dto.GetRepairOrderStatusBatchDto;
import com.hete.supply.scm.api.scm.entity.dto.RepairOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderProcessResultVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderResultExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderStatusVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
public interface RepairOrderFacade {

    /**
     * 通过计划单号列表获取返修单状态信息列表
     *
     * @param dto 计划单号列表
     * @return 返修单状态信息列表
     */
    CommonResult<RepairOrderStatusVo> getRepairOrderStatusByPlanNos(GetRepairOrderStatusBatchDto dto);

    /**
     * 通过返修单号列表获取返修单加工结果信息列表
     *
     * @param dto 查询条件实体
     * @return 返修单状态信息列表
     */
    CommonResult<RepairOrderProcessResultVo> getRepairOrderProcessResultByRepairNo(GetRepairOrderProcessResultInfoDto dto);


    /**
     * 返修单导出
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/1/10 11:12
     */
    CommonResult<Integer> getExportTotals(RepairOrderSearchDto dto);

    /**
     * 返修单导出
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RepairOrderExportVo>>
     * @author ChenWenLong
     * @date 2024/1/10 11:12
     */
    CommonResult<ExportationListResultBo<RepairOrderExportVo>> getExportList(RepairOrderSearchDto dto);

    /**
     * 返修单结果导出
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/1/10 11:12
     */
    CommonResult<Integer> getResultExportTotals(RepairOrderSearchDto dto);

    /**
     * 返修单结果导出
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RepairOrderExportVo>>
     * @author ChenWenLong
     * @date 2024/1/10 11:12
     */
    CommonResult<ExportationListResultBo<RepairOrderResultExportVo>> getResultExportList(RepairOrderSearchDto dto);

}
