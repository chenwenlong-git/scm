package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.ExportSupplierRestDto;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityRuleExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierRestExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;

/**
 * @author yanjiawei
 * Created on 2024/8/6.
 */
public interface SupplierCapacityFacade {

    //供应商停工时间导出条数
    CommonResult<Integer> getSupRestExportTotal(ExportSupplierRestDto dto);

    //供应商停工时间导出数据
    CommonResult<ExportationListResultBo<SupplierRestExportVo>> getSupRestExportList(ExportSupplierRestDto dto);

    //供应商产能规则导出条数
    CommonResult<Integer> getSupCapacityRuleExportTotal(SupCapacityPageDto dto);

    //供应商产能规则导出数据
    CommonResult<ExportationListResultBo<SupplierCapacityRuleExportVo>> getSupCapacityRuleExportList(SupCapacityPageDto dto);

    //供应商每日剩余产能导出条数
    CommonResult<Integer> getSupCapacityExportTotal(SupCapacityPageDto dto);

    //供应商每日剩余产能导出数据
    CommonResult<ExportationListResultBo<SupplierCapacityExportVo>> getSupCapacityExportList(SupCapacityPageDto dto);
}
