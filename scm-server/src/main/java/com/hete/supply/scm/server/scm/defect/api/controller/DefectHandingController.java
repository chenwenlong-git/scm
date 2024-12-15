package com.hete.supply.scm.server.scm.defect.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.DefectHandingSearchDto;
import com.hete.supply.scm.server.scm.defect.entity.dto.*;
import com.hete.supply.scm.server.scm.defect.service.biz.DefectBizService;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderNoDto;
import com.hete.supply.scm.server.scm.entity.vo.DefectHandingSearchVo;
import com.hete.supply.scm.server.scm.entity.vo.ReceiveOrderPrintVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
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
 * @author weiwenxin
 * @date 2023/6/21 09:33
 */
@Validated
@RestController
@RequestMapping("/scm/defect")
@RequiredArgsConstructor
@Api(tags = "次品处理")
public class DefectHandingController {
    private final DefectBizService defectBizService;


    @ApiOperation("次品处理列表")
    @PostMapping("/searchDefect")
    public CommonPageResult<DefectHandingSearchVo> searchDefect(@NotNull @Validated @RequestBody DefectHandingSearchDto dto) {
        return new CommonPageResult<>(defectBizService.searchDefect(dto));
    }

    @ApiOperation("大货次品退供")
    @PostMapping("/returnSupply")
    public CommonResult<Void> returnSupply(@NotNull @Validated @RequestBody DefectHandlingNoListDto dto) {
        defectBizService.returnSupply(dto);
        return CommonResult.success();
    }

    @ApiOperation("次品报废")
    @PostMapping("/scrapped")
    public CommonResult<Void> scrapped(@NotNull @Validated @RequestBody DefectHandlingScrappedDto dto) {
        defectBizService.scrapped(dto);
        return CommonResult.success();
    }

    @ApiOperation("次品换货")
    @PostMapping("/exchangeGoods")
    public CommonResult<Void> exchangeGoods(@NotNull @Validated @RequestBody DefectHandlingExchangeGoodsDto dto) {
        defectBizService.exchangeGoods(dto);
        return CommonResult.success();
    }

    @ApiOperation("次品让步")
    @PostMapping("/compromise")
    public CommonResult<Void> compromise(@NotNull @Validated @RequestBody DefectCompromiseDto dto) {
        defectBizService.compromise(dto);
        return CommonResult.success();
    }

    @ApiOperation("库内次品退供")
    @PostMapping("/returnSupplyInside")
    public CommonResult<Void> returnSupplyInside(@NotNull @Validated @RequestBody DefectHandlingReturnInsideDto dto) {
        defectBizService.returnSupply(dto);
        return CommonResult.success();
    }


    @ApiOperation("加工次品退供")
    @PostMapping("/returnSupplyProcess")
    public CommonResult<Void> returnSupplyProcess(@NotNull @Validated @RequestBody DefectHandlingReturnProcessDto dto) {
        defectBizService.returnSupplyProcess(dto);
        return CommonResult.success();
    }


    @ApiOperation("打印收货单")
    @PostMapping("/printReceiveOrderByNo")
    public CommonResult<ReceiveOrderPrintVo> printReceiveOrderByNo(@NotNull @Validated @RequestBody ReceiveOrderNoDto dto) {
        return CommonResult.success(defectBizService.printReceiveOrderByNo(dto));
    }

    @ApiOperation("原料次品退供")
    @PostMapping("/returnSupplyMaterial")
    public CommonResult<Void> returnSupplyMaterial(@NotNull @Validated @RequestBody DefectHandlingReturnInsideDto dto) {
        defectBizService.returnSupply(dto);
        return CommonResult.success();
    }

    @ApiOperation("次品记录列表导出")
    @PostMapping("/defectExport")
    public CommonResult<Void> defectExport(@NotNull @Validated @RequestBody DefectHandingSearchDto dto) {
        defectBizService.defectExport(dto);
        return CommonResult.success();
    }
}
