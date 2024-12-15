package com.hete.supply.scm.server.scm.process.api.provider;

import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessExportVo;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessNewExportVo;
import com.hete.supply.scm.api.scm.facade.GoodsProcessFacade;
import com.hete.supply.scm.server.scm.process.service.biz.GoodProcessExportService;
import com.hete.supply.scm.server.scm.process.service.biz.GoodsProcessBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 17:49
 */
@DubboService
@RequiredArgsConstructor
public class GoodsProcessFacadeImpl implements GoodsProcessFacade {
    private final GoodsProcessBizService goodsProcessBizService;
    private final GoodProcessExportService goodProcessExportService;

    /**
     * 获取商品工序总数
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult<Integer> getExportTotals(GoodsProcessQueryDto dto) {
        return CommonResult.success(goodsProcessBizService.getExportTotals(dto));
    }

    /**
     * 获取商品工序导出列表
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageResult<GoodsProcessExportVo> getExportList(GoodsProcessQueryByApiDto dto) {
        return new CommonPageResult<>(goodsProcessBizService.getExportList(dto));
    }


    @Override
    public CommonResult<ExportationListResultBo<GoodsProcessNewExportVo>> getNewExportList(GoodsProcessQueryDto dto) {
        return goodProcessExportService.getNewExportList(dto);
    }
}
