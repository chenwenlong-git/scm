package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderSkuExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

public interface PurchaseSettleOrderFacade {

    /**
     * 获取采购结算总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getExportTotals(PurchaseSettleOrderSearchDto dto);

    /**
     * 获取采购结算导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<PurchaseSettleOrderExportVo> getExportList(PurchaseSettleOrderSearchDto dto);

    /**
     * 获取采购结算SKU导出列表
     *
     * @param dto
     * @return
     */
    public CommonResult<ResultList<PurchaseSettleOrderSkuExportVo>> getExportSkuList(PurchaseSettleOrderSearchDto dto);

    /**
     * 获取供应商采购结算总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getSupplierExportTotals(PurchaseSettleOrderSearchDto dto);

    /**
     * 获取供应商采购结算导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<PurchaseSettleOrderExportVo> getSupplierExportList(PurchaseSettleOrderSearchDto dto);
}
