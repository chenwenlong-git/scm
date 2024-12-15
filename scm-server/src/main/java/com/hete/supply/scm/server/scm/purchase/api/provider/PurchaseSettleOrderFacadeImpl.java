package com.hete.supply.scm.server.scm.purchase.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderSkuExportVo;
import com.hete.supply.scm.api.scm.facade.PurchaseSettleOrderFacade;
import com.hete.supply.scm.server.scm.settle.service.biz.PurchaseSettleOrderBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
@RequiredArgsConstructor
public class PurchaseSettleOrderFacadeImpl implements PurchaseSettleOrderFacade {
    private final PurchaseSettleOrderBizService purchaseSettleOrderBizService;

    /**
     * 获取总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(PurchaseSettleOrderSearchDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.getExportTotals(dto));
    }

    /**
     * 获取导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<PurchaseSettleOrderExportVo> getExportList(PurchaseSettleOrderSearchDto dto) {
        return new CommonPageResult<>(purchaseSettleOrderBizService.getExportList(dto));
    }

    /**
     * 获取SKU导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<ResultList<PurchaseSettleOrderSkuExportVo>> getExportSkuList(PurchaseSettleOrderSearchDto dto) {
        return CommonResult.successForList(purchaseSettleOrderBizService.getExportSkuList(dto));
    }

    /**
     * 供应商系统获取总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getSupplierExportTotals(PurchaseSettleOrderSearchDto dto) {
        return CommonResult.success(purchaseSettleOrderBizService.getSupplierExportTotals(dto));
    }

    /**
     * 供应商系统获取导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<PurchaseSettleOrderExportVo> getSupplierExportList(PurchaseSettleOrderSearchDto dto) {
        return new CommonPageResult<>(purchaseSettleOrderBizService.getSupplierExportList(dto));
    }
}
