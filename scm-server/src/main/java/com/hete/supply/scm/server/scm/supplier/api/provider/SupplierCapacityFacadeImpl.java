package com.hete.supply.scm.server.scm.supplier.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.ExportSupplierRestDto;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityRuleExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierRestExportVo;
import com.hete.supply.scm.api.scm.facade.SupplierCapacityFacade;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierCapacityPageVo;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierCapacityBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/1/22 20:31
 */
@DubboService
@RequiredArgsConstructor
public class SupplierCapacityFacadeImpl implements SupplierCapacityFacade {
    private final SupplierCapacityBizService supplierCapacityBizService;

    //获取供应商停工时间导出条数
    @Override
    public CommonResult<Integer> getSupRestExportTotal(ExportSupplierRestDto dto) {
        return supplierCapacityBizService.getSupRestExportTotal(dto);
    }

    //供应商停工时间导出数据
    @Override
    public CommonResult<ExportationListResultBo<SupplierRestExportVo>> getSupRestExportList(ExportSupplierRestDto dto) {
        return supplierCapacityBizService.getSupRestExportList(dto);
    }

    //供应商产能规则导出条数
    @Override
    public CommonResult<Integer> getSupCapacityRuleExportTotal(SupCapacityPageDto dto) {
        return supplierCapacityBizService.getSupCapacityRuleExportTotal(dto);
    }

    //供应商产能规则导出数据
    @Override
    public CommonResult<ExportationListResultBo<SupplierCapacityRuleExportVo>> getSupCapacityRuleExportList(SupCapacityPageDto dto) {
        return supplierCapacityBizService.getSupCapacityRuleExportList(dto);
    }

    //供应商每日剩余产能导出条数
    @Override
    public CommonResult<Integer> getSupCapacityExportTotal(SupCapacityPageDto dto) {
        return supplierCapacityBizService.getSupCapacityExportTotal(dto);
    }

    //供应商每日剩余产能导出数据
    @Override
    public CommonResult<ExportationListResultBo<SupplierCapacityExportVo>> getSupCapacityExportList(SupCapacityPageDto dto) {
        return supplierCapacityBizService.getSupCapacityExportList(dto);
    }
}
