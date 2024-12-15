package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.CostSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsCostDto;
import com.hete.supply.scm.api.scm.entity.vo.CostSkuItemVo;
import com.hete.supply.scm.api.scm.entity.vo.GoodsOfCostExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 商品成本
 *
 * @author ChenWenLong
 * @date 2024/2/28 15:42
 */
public interface CostFacade {

    /**
     * 商品成本列表统计(仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    CommonResult<Integer> getExportOneTotals(GoodsCostDto dto);

    /**
     * 商品成本列表导出(仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    CommonResult<ExportationListResultBo<GoodsOfCostExportVo>> getExportOneList(GoodsCostDto dto);

    /**
     * 商品成本列表统计(多仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    CommonResult<Integer> getExportManyTotals(GoodsCostDto dto);

    /**
     * 商品成本列表导出(多仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    CommonResult<ExportationListResultBo<GoodsOfCostExportVo>> getExportManyList(GoodsCostDto dto);

    /**
     * 根据sku获取成本信息
     *
     * @param dto
     * @return
     */
    CommonResult<ResultList<CostSkuItemVo>> getCostBySku(@NotNull @Valid CostSkuDto dto);
}
