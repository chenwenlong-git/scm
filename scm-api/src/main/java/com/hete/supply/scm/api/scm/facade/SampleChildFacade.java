package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SamplePurchaseSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SampleSkuListDto;
import com.hete.supply.scm.api.scm.entity.dto.SpuDto;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleNoAndStatusVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

/**
 * @author weiwenxin
 * @date 2022/12/12 00:21
 */
public interface SampleChildFacade {
    CommonResult<Integer> getExportTotals(SamplePurchaseSearchDto dto);

    CommonPageResult<SampleChildExportVo> getExportList(SamplePurchaseSearchDto dto);

    CommonResult<ResultList<SampleNoAndStatusVo>> getSampleNoAndStatusBySpu(SpuDto dto);

    CommonResult<ResultList<SampleInfoVo>> getSampleInfoListBySkuList(SampleSkuListDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getSupplierExportTotals(SamplePurchaseSearchDto dto);

    /**
     * 获取供应商系统导出
     *
     * @param dto
     * @return
     */
    CommonPageResult<SampleChildExportVo> getSupplierExportList(SamplePurchaseSearchDto dto);
}
