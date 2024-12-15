package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.dto.GoodsProcessQueryDto;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessExportVo;
import com.hete.supply.scm.api.scm.entity.vo.GoodsProcessNewExportVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 16:21
 */
public interface GoodsProcessFacade {

    /**
     * 获取商品工序总数
     *
     * @param dto
     * @return
     */
    CommonResult<Integer> getExportTotals(GoodsProcessQueryDto dto);

    /**
     * 获取商品工序导出列表
     *
     * @param dto
     * @return
     */
    CommonPageResult<GoodsProcessExportVo> getExportList(GoodsProcessQueryByApiDto dto);

    /**
     * 获取商品工序导出列表
     *
     * @param dto
     * @return
     */
    CommonResult<ExportationListResultBo<GoodsProcessNewExportVo>> getNewExportList(GoodsProcessQueryDto dto);
}
