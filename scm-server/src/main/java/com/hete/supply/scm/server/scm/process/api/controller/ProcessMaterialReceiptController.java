package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.MaterialBackDto;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.vo.MaterialBackVo;
import com.hete.supply.scm.api.scm.entity.vo.ProcessMaterialReceiptExportVo;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptByNoDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptConfirmDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptDetailDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptQueryDto;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptDetailVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessMaterialReceiptVo;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessMaterialReceiptBizService;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:23
 */
@RestController
@Api(tags = "加工原料收货管理")
@RequestMapping("/scm/processMaterialReceipt")
@RequiredArgsConstructor
public class ProcessMaterialReceiptController {
    private final ProcessMaterialReceiptBizService processMaterialReceiptBizService;

    @ApiOperation("加工原料收货列表")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessMaterialReceiptVo> getByPage(@NotNull @Valid @RequestBody ProcessMaterialReceiptQueryDto dto) {
        return new CommonPageResult<>(processMaterialReceiptBizService.getByPage(dto));
    }

    @PostMapping("/getByNo")
    @ApiOperation("通过单号查询原料收货单")
    public CommonResult<ResultList<ProcessMaterialReceiptDetailVo>> getByNo(@NotNull @Valid @RequestBody ProcessMaterialReceiptByNoDto dto) {
        return CommonResult.successForList(processMaterialReceiptBizService.getByNo(dto));
    }

    @PostMapping("/getByProcessOrderNo")
    @ApiOperation("通过加工单号查询原料收货单")
    public CommonResult<ResultList<ProcessMaterialReceiptDetailVo>> getByProcessOrderNo(@NotNull @Valid @RequestBody ProcessMaterialReceiptByNoDto dto) {
        return CommonResult.successForList(processMaterialReceiptBizService.getByProcessOrderNo(dto));
    }

    @PostMapping("/detail")
    @ApiOperation("加工原料收货详情")
    public CommonResult<ProcessMaterialReceiptDetailVo> detail(@NotNull @Valid @RequestBody ProcessMaterialReceiptDetailDto dto) {
        return CommonResult.success(processMaterialReceiptBizService.detail(dto));
    }

    @PostMapping("/confirmReceipt")
    @ApiOperation("确认收货")
    public CommonResult<Void> confirmReceipt(@NotNull @Valid @RequestBody ProcessMaterialReceiptConfirmDto dto) {
        processMaterialReceiptBizService.confirmReceipt(dto);
        return CommonResult.success();
    }

    @PostMapping("/getExportList")
    @ApiOperation("获取需要导出的列表")
    public CommonPageResult<ProcessMaterialReceiptExportVo> getExportList(@NotNull @Valid @RequestBody ProcessMaterialReceiptQueryByApiDto dto) {
        return new CommonPageResult<>(processMaterialReceiptBizService.getExportList(dto));
    }

    @PostMapping("/getMaterialBackInfo")
    @ApiOperation("获取当前批次码原料可归信息")
    public CommonResult<MaterialBackVo> getMaterialBackInfo(@NotNull @Valid @RequestBody MaterialBackDto dto) {
        return CommonResult.success(processMaterialReceiptBizService.getMaterialBackInfo(dto));
    }

}
