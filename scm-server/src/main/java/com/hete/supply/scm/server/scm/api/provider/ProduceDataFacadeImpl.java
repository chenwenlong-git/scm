package com.hete.supply.scm.server.scm.api.provider;

import com.alibaba.fastjson.JSON;
import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataAttrSkuDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSkuListDto;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataAttrSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuAttrVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataExportSkuProcessVo;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataVo;
import com.hete.supply.scm.api.scm.facade.ProduceDataFacade;
import com.hete.supply.scm.server.scm.service.biz.ProduceDataBizService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ChenWenLong
 * @date 2024/1/2 11:48
 */
@DubboService
@RequiredArgsConstructor
public class ProduceDataFacadeImpl implements ProduceDataFacade {
    private final ProduceDataBizService produceDataBizService;

    @Override
    public CommonResult<Integer> getSkuProcessExportTotals(ProduceDataSearchDto dto) {
        return CommonResult.success(produceDataBizService.getSkuProcessExportTotals(dto));
    }

    @Override
    public CommonResult<Integer> getSkuProcessExportTotals(PlmSkuSearchDto dto) {
        return getSkuProcessExportTotals(JSON.parseObject(JSON.toJSONString(dto), ProduceDataSearchDto.class));
    }

    @Override
    public CommonResult<ExportationListResultBo<ProduceDataExportSkuProcessVo>> getSkuProcessExportList(ProduceDataSearchDto dto) {
        return produceDataBizService.getSkuProcessExportList(dto);
    }

    @Override
    public CommonResult<ExportationListResultBo<ProduceDataExportSkuProcessVo>> getSkuProcessExportList(PlmSkuSearchDto dto) {
        return getSkuProcessExportList(JSON.parseObject(JSON.toJSONString(dto), ProduceDataSearchDto.class));
    }

    @Override
    public CommonResult<ResultList<ProduceDataVo>> getProduceDataListBySkuList(ProduceDataSkuListDto dto) {
        return CommonResult.successForList(produceDataBizService.getProduceDataListBySkuList(dto));
    }

    @Override
    public CommonResult<Integer> getSkuAttrExportTotals(ProduceDataSearchDto dto) {
        return CommonResult.success(produceDataBizService.getSkuAttrExportTotals(dto));
    }

    @Override
    public CommonResult<ExportationListResultBo<ProduceDataExportSkuAttrVo>> getSkuAttrExportList(ProduceDataSearchDto dto) {
        return produceDataBizService.getSkuAttrExportList(dto);
    }

    @Override
    public CommonPageResult<ProduceDataAttrSkuVo> getSkuAttrBySkuAndId(ProduceDataAttrSkuDto dto) {

        return produceDataBizService.getSkuAttrBySkuAndId(dto);
    }

}
