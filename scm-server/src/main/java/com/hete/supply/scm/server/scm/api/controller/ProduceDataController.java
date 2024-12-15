package com.hete.supply.scm.server.scm.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSkuListDto;
import com.hete.supply.scm.api.scm.entity.dto.SampleSkuListDto;
import com.hete.supply.scm.api.scm.entity.vo.ProduceDataVo;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.vo.SkuBomListVo;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.biz.ProduceDataBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/08/22 14:42
 */
@Validated
@RestController
@RequestMapping("/scm/produceData")
@RequiredArgsConstructor
@Api(tags = "生产信息管理")
public class ProduceDataController {
    private final ProduceDataBizService produceDataBizService;
    private final ProduceDataBaseService produceDataBaseService;

    @ApiOperation("生产信息列表")
    @PostMapping("/search")
    public CommonPageResult<ProduceDataSearchVo> search(@NotNull @Validated @RequestBody ProduceDataSearchDto dto) {
        return new CommonPageResult<>(produceDataBizService.search(dto));
    }

    @ApiOperation("生产信息详情")
    @PostMapping("/detail")
    public CommonResult<ProduceDataDetailVo> detail(@NotNull @Validated @RequestBody ProduceDataSkuDto dto) {
        return CommonResult.success(produceDataBizService.detail(dto));
    }

    @ApiOperation("生产信息保存创建")
    @PostMapping("/save")
    public CommonResult<Void> save(@NotNull @Validated @RequestBody ProduceDataSaveDto dto) {
        produceDataBizService.save(dto);
        return CommonResult.success();
    }

    @ApiOperation("SPU的主图编辑")
    @PostMapping("/editSpuPicture")
    public CommonResult<Void> editSpuPicture(@NotNull @Validated @RequestBody ProduceDataSpuDto dto) {
        produceDataBizService.editSpuPicture(dto);
        return CommonResult.success();
    }

    @ApiOperation("采购单详情根据sku获取bom信息")
    @PostMapping("/getDetailBySkuList")
    public CommonResult<ResultList<ProduceDataSkuDetailVo>> getDetailBySkuList(@NotNull @Validated @RequestBody SkuListDto dto) {
        return CommonResult.successForList(produceDataBizService.getDetailBySkuList(dto));
    }

    @ApiOperation("上传产品规格书")
    @PostMapping("/updateLoadProductFile")
    public CommonResult<Void> updateLoadProductFile(@NotNull @Validated @RequestBody ProduceUploadFileDto dto) {
        produceDataBizService.updateLoadProductFile(dto);
        return CommonResult.success();
    }

    @ApiOperation("查看产品规格书")
    @PostMapping("/getLoadProductFile")
    public CommonResult<ResultList<ProduceDataSpecVo>> getLoadProductFile(@NotNull @Validated @RequestBody SkuDto dto) {
        return CommonResult.successForList(produceDataBizService.getLoadProductFile(dto));
    }


    @ApiOperation("批量查看产品规格书信息")
    @PostMapping("/getBatchLoadProductFile")
    public CommonResult<ResultList<ProduceDataSpecBatchVo>> getBatchLoadProductFile(@NotNull @Validated @RequestBody SkuListDto dto) {
        return CommonResult.successForList(produceDataBizService.getBatchLoadProductFile(dto));
    }

    @ApiOperation("原料工序导出")
    @PostMapping("/exportSkuProcess")
    public CommonResult<Void> exportSkuProcess(@NotNull @Validated @RequestBody ProduceDataSearchDto dto) {
        produceDataBizService.exportSkuProcess(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据sku获取bom列表")
    @PostMapping("/getBomListBySkuList")
    public CommonResult<ResultList<SkuBomListVo>> getBomListBySkuList(@NotNull @Validated @RequestBody SampleSkuListDto dto) {
        return CommonResult.successForList(produceDataBaseService.getBomListBySkuList(dto));
    }

    @ApiOperation("生产属性导出")
    @PostMapping("/exportSkuAttr")
    public CommonResult<Void> exportSkuAttr(@NotNull @Validated @RequestBody ProduceDataSearchDto dto) {
        produceDataBizService.exportSkuAttr(dto);
        return CommonResult.success();
    }

    @ApiOperation("生产信息头部信息保存")
    @PostMapping("/topSave")
    public CommonResult<Void> topSave(@NotNull @Validated @RequestBody ProduceDataSaveTopDto dto) {
        produceDataBizService.topSave(dto);
        return CommonResult.success();
    }

    @ApiOperation("生产信息主体信息保存")
    @PostMapping("/bodySave")
    public CommonResult<Void> bodySave(@NotNull @Validated @RequestBody ProduceDataSaveBodyDto dto) {
        produceDataBizService.bodySave(dto);
        return CommonResult.success();
    }

    @PostMapping("/getProduceDataListBySkuList")
    public CommonResult<ResultList<ProduceDataVo>> getProduceDataListBySkuList(@NotNull @Validated @RequestBody ProduceDataSkuListDto dto) {
        return CommonResult.successForList(produceDataBizService.getProduceDataListBySkuList(dto));
    }

}
