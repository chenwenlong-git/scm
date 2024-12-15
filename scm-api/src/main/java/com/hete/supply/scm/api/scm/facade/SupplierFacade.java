package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.GetBySupplierCodeDto;
import com.hete.supply.scm.api.scm.entity.dto.PurchasePreOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplierSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.GetBySupplierCodeVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchasePreOrderVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuGetSuggestSupplierVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierExportVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

/**
 * @author ChenWenLong
 * @date 2022/11/19 10:45
 */
public interface SupplierFacade {

    /**
     * 通过供应商代码获取供应商名称
     *
     * @param dto 供应商代码使用请求
     * @return 供应商名称结果
     */
    CommonResult<ResultList<GetBySupplierCodeVo>> getBySupplierCode(GetBySupplierCodeDto dto);


    /**
     * 获取总数
     *
     * @param dto
     * @return
     */
    public CommonResult<Integer> getExportTotals(SupplierSearchDto dto);

    /**
     * 获取导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult<SupplierExportVo> getExportList(SupplierSearchDto dto);

    /**
     * 获取推荐供应商列表
     *
     * @param dto
     * @return
     */
    CommonResult<ResultList<SkuGetSuggestSupplierVo>> getSuggestSupplierList(SkuGetSuggestSupplierDto dto);

    /**
     * 获取采购预下单列表
     *
     * @return PurchasePreOrderVo
     */
    CommonResult<PurchasePreOrderVo> queryPurchasePreOrderList(PurchasePreOrderDto purchasePreOrderDto);

}
