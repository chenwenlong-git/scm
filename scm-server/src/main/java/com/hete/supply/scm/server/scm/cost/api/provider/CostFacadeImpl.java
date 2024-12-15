package com.hete.supply.scm.server.scm.cost.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.CostSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsCostDto;
import com.hete.supply.scm.api.scm.entity.vo.CostSkuItemVo;
import com.hete.supply.scm.api.scm.entity.vo.GoodsOfCostExportVo;
import com.hete.supply.scm.api.scm.facade.CostFacade;
import com.hete.supply.scm.server.scm.cost.service.base.CostBaseService;
import com.hete.supply.scm.server.scm.cost.service.biz.CostBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/2/28 15:50
 */
@DubboService
@RequiredArgsConstructor
@Validated
public class CostFacadeImpl implements CostFacade {
    private final CostBizService costBizService;
    private final CostBaseService costBaseService;

    /**
     * 商品成本列表统计(仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    @Override
    public CommonResult<Integer> getExportOneTotals(GoodsCostDto dto) {
        return CommonResult.success(costBizService.getExportTotals(dto));
    }

    /**
     * 商品成本列表导出(仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    @Override
    public CommonResult<ExportationListResultBo<GoodsOfCostExportVo>> getExportOneList(GoodsCostDto dto) {
        return costBizService.getExportList(dto);
    }

    /**
     * 商品成本列表统计(多仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    @Override
    public CommonResult<Integer> getExportManyTotals(GoodsCostDto dto) {
        return CommonResult.success(costBizService.getExportTotals(dto));
    }

    /**
     * 商品成本列表导出(多仓库)
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/2/28 15:52
     */
    @Override
    public CommonResult<ExportationListResultBo<GoodsOfCostExportVo>> getExportManyList(GoodsCostDto dto) {
        return costBizService.getExportList(dto);
    }

    @Override
    public CommonResult<ResultList<CostSkuItemVo>> getCostBySku(@NotNull @Valid CostSkuDto dto) {
        return CommonResult.successForList(costBaseService.getCostBySku(dto));
    }
}
