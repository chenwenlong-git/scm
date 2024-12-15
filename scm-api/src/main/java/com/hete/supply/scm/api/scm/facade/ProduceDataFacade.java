package com.hete.supply.scm.api.scm.facade;

import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSkuListDto;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataAttrSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuAttrVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuProcessVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/1/2 11:26
 */
public interface ProduceDataFacade {


    /**
     * 原料工序导出总数
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/1/2 11:27
     */
    CommonResult<Integer> getSkuProcessExportTotals(ProduceDataSearchDto dto);


    CommonResult<Integer> getSkuProcessExportTotals(PlmSkuSearchDto dto);

    /**
     * 原料工序导出列表
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/1/2 11:27
     */
    CommonResult<ExportationListResultBo<ProduceDataExportSkuProcessVo>> getSkuProcessExportList(ProduceDataSearchDto dto);

    CommonResult<ExportationListResultBo<ProduceDataExportSkuProcessVo>> getSkuProcessExportList(PlmSkuSearchDto dto);

    /**
     * PLM获取生产资料的信息
     *
     * @param dto sku列表
     * @return 生产资料信息列表
     */
    CommonResult<ResultList<ProduceDataVo>> getProduceDataListBySkuList(ProduceDataSkuListDto dto);


    /**
     * 生产属性导出
     *
     * @param dto:
     * @return CommonResult<Integer>
     * @author ChenWenLong
     * @date 2024/6/27 10:16
     */
    CommonResult<Integer> getSkuAttrExportTotals(ProduceDataSearchDto dto);


    /**
     * 生产属性导出
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < ProduceDataExportSkuAttrVo>>
     * @author ChenWenLong
     * @date 2024/6/27 10:16
     */
    CommonResult<ExportationListResultBo<ProduceDataExportSkuAttrVo>> getSkuAttrExportList(ProduceDataSearchDto dto);


    /**
     * 根据属性id跟sku list查询属性值
     *
     * @param dto
     * @return
     */
    CommonPageResult<ProduceDataAttrSkuVo> getSkuAttrBySkuAndId(@NotNull @Valid ProduceDataAttrSkuDto dto);
}
