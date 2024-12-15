package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.SkuCodeListDto;
import com.hete.supply.scm.api.scm.entity.vo.SkuProduceDataVo;
import com.hete.supply.scm.api.scm.entity.vo.SkuRelatedDataVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

/**
 * 提供PDC查询scm的sku信息
 *
 * @author ChenWenLong
 * @date 2023/10/12 14:35
 */
public interface SkuFacade {
    CommonResult<ResultList<SkuProduceDataVo>> getSkuProduceDataBySkuList(SkuCodeListDto dto);

    /**
     * plm通过sku获取采购或质检等信息
     *
     * @param dto:
     * @return CommonResult<ResultList < SkuRelatedDataVo>>
     * @author ChenWenLong
     * @date 2023/12/26 13:43
     */
    CommonResult<ResultList<SkuRelatedDataVo>> getSkuRelatedDataBySkuList(SkuCodeListDto dto);
}
