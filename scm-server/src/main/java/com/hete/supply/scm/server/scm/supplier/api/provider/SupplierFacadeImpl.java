package com.hete.supply.scm.server.scm.supplier.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.GetBySupplierCodeDto;
import com.hete.supply.scm.api.scm.entity.dto.PurchasePreOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplierSearchDto;
import com.hete.supply.scm.api.scm.entity.vo.GetBySupplierCodeVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchasePreOrderVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuGetSuggestSupplierVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierExportVo;
import com.hete.supply.scm.api.scm.facade.SupplierFacade;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierConverter;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/19 11:25
 */
@DubboService
@RequiredArgsConstructor
public class SupplierFacadeImpl implements SupplierFacade {

    private final SupplierBaseService supplierBaseService;
    private final SupplierBizService supplierBizService;

    @Override
    public CommonResult<ResultList<GetBySupplierCodeVo>> getBySupplierCode(GetBySupplierCodeDto dto) {
        List<SupplierPo> supplierPos = supplierBaseService.getBySupplierCodeList(dto.getSupplierCodeList());
        List<GetBySupplierCodeVo> list = SupplierConverter.INSTANCE.poListToSupplierCodeVo(supplierPos);
        return CommonResult.successForList(list);
    }

    /**
     * 获取总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(SupplierSearchDto dto) {
        return CommonResult.success(supplierBaseService.getExportTotals(dto));
    }

    /**
     * 获取导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<SupplierExportVo> getExportList(SupplierSearchDto dto) {
        return new CommonPageResult<>(supplierBaseService.getExportList(dto));
    }

    /**
     * 获取推荐供应商列表
     *
     * @param dto:
     * @return CommonResult<ResultList < SkuGetSuggestSupplierVo>>
     * @author ChenWenLong
     * @date 2024/8/6 14:53
     */
    @Override
    public CommonResult<ResultList<SkuGetSuggestSupplierVo>> getSuggestSupplierList(SkuGetSuggestSupplierDto dto) {
        return CommonResult.successForList(supplierBizService.getDefaultSupplierList(dto));
    }

    @Override
    public CommonResult<PurchasePreOrderVo> queryPurchasePreOrderList(PurchasePreOrderDto dto) {
        return CommonResult.success(supplierBizService.queryPurchasePreOrderList(dto));
    }

}
