package com.hete.supply.scm.server.scm.process.api.controller;

import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.supply.scm.server.scm.entity.bo.OperatorUserBo;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.vo.SkuMaterialVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuProcedureVo;
import com.hete.supply.scm.server.scm.entity.vo.WarehouseListVo;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.process.service.base.ProcessOrderBaseService;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderReportBizService;
import com.hete.supply.scm.server.scm.service.base.ReportServerFactory;
import com.hete.supply.scm.server.scm.service.base.ReportStatisticStrategyFactory;
import com.hete.supply.scm.server.scm.service.biz.ProcessOrderExportBizService;
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
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:23
 */
@RestController
@Api(tags = "加工单管理")
@RequestMapping("/scm/processOrder")
@RequiredArgsConstructor
public class ProcessOrderController {

    private final ProcessOrderBizService processOrderBizService;
    private final ProcessOrderBaseService processOrderBaseService;
    private final ProcessOrderExportBizService exportBizService;
    private final ProcessOrderReportBizService processOrderReportBizService;
    private final ReportServerFactory reportServerFactory;
    private final ReportStatisticStrategyFactory reportStatisticStrategyFactory;

    @ApiOperation("加工单列表")
    @PostMapping("/getByPage")
    public CommonPageResult<ProcessOrderVo> getByPage(@NotNull @Valid @RequestBody ProcessOrderQueryDto dto) {
        return new CommonPageResult<>(processOrderBizService.getByPage(dto));
    }

    @PostMapping("/create")
    @ApiOperation("创建加工单")
    public CommonResult<Void> create(@NotNull @Valid @RequestBody ProcessOrderCreateNewDto dto) {
        processOrderBizService.createByNormal(dto);
        return CommonResult.success();
    }

    @PostMapping("/edit")
    @ApiOperation("编辑加工单")
    public CommonResult<ProcessOrderPo> edit(@NotNull @Valid @RequestBody ProcessOrderEditDto dto) {
        return CommonResult.success(processOrderBizService.edit(dto));
    }

    @PostMapping("/detail")
    @ApiOperation("加工单详情")
    public CommonResult<ProcessOrderDetailVo> detail(@NotNull @Valid @RequestBody ProcessOrderDetailDto dto) {
        return CommonResult.success(processOrderBizService.detail(dto));
    }

    @PostMapping("/detailByNo")
    @ApiOperation("通过编号获取加工单详情")
    public CommonResult<ProcessOrderDetailVo> detailByNo(@NotNull @Valid @RequestBody ProcessOrderDetailByNoDto dto) {
        return CommonResult.success(processOrderBizService.detailByNo(dto));
    }

    @PostMapping("/defectiveHandle")
    @ApiOperation("提交次品处理记录")
    public CommonResult<Void> defectiveHandle(@NotNull @Valid @RequestBody ProcessOrderDefectiveHandleDto dto) {
        processOrderBizService.defectiveHandle(dto);
        return CommonResult.success();
    }

    @PostMapping("/defectiveRecord")
    @ApiOperation("次品记录查询")
    public CommonResult<ProcessDefectiveRecordByNoVo> defectiveRecord(@NotNull @Valid @RequestBody ProcessOrderNoDto dto) {
        return CommonResult.success(processOrderBizService.defectiveRecord(dto));
    }

    @PostMapping("/checkMaterial")
    @ApiOperation("加工单检查原料")
    public CommonResult<ProcessOrderPo> checkMaterial(@NotNull @Valid @RequestBody ProcessOrderCheckMaterialDto dto) {
        return CommonResult.success(processOrderBizService.checkMaterialByWeb(dto));
    }

    @PostMapping("/refreshMaterial")
    @ApiOperation("加工单更新原料bom信息")
    public CommonResult<ProcessOrderPo> refreshMaterial(@NotNull @Valid @RequestBody ProcessOrderCheckMaterialDto dto) {
        return CommonResult.success(processOrderBizService.refreshMaterialByWeb(dto));
    }

    @PostMapping("/stockMatch")
    @ApiOperation("加工单库存匹配")
    public CommonResult<ProcessOrderVo> stockMatch(@NotNull @Valid @RequestBody ProcessOrderStockMatchDto dto) {
        return CommonResult.success(processOrderBizService.stockMatch(dto));
    }

    @PostMapping("/batchPrint")
    @ApiOperation("批量打印")
    public CommonResult<ResultList<ProcessOrderPrintVo>> batchPrint(@NotNull @Valid @RequestBody ProcessOrderBatchPrintDto dto) {
        return CommonResult.successForList(processOrderBizService.batchPrintByProcessOrderIds(dto));
    }

    @PostMapping("/batchPrintByDeliveryNo")
    @ApiOperation("通过出库单号批量打印加工单")
    public CommonResult<ResultList<ProcessOrderPrintByWmsVo>> batchPrintByDeliveryNo(@NotNull @Valid @RequestBody ProcessOrderBatchPrintByDeliveryNoDto dto) {
        return CommonResult.successForList(processOrderBizService.batchPrintHttpByDeliveryNo(dto));
    }

    @PostMapping("/batchPrintSkuCode")
    @ApiOperation("批量打印批次码")
    public CommonResult<ResultList<ProcessOrderPrintSkuCodeVo>> batchPrintSkuCode(@NotNull @Valid @RequestBody ProcessOrderBatchPrintDto dto) {
        return CommonResult.successForList(processOrderBizService.batchPrintSkuCode(dto));
    }

    @PostMapping("/changeStatus")
    @ApiOperation("更改加工单状态")
    public CommonResult<Boolean> changeStatus(@NotNull @Valid @RequestBody ProcessOrderChangeStatusDto dto) {
        return CommonResult.success(processOrderBizService.changeUp(dto));
    }


    @PostMapping("/remove")
    @ApiOperation("取消加工")
    public CommonResult<Boolean> remove(@NotNull @Valid @RequestBody ProcessOrderRemoveDto dto) {
        return CommonResult.success(processOrderBizService.remove(dto));
    }

    @PostMapping("/quit")
    @ApiOperation("作废")
    public CommonResult<Boolean> quit(@NotNull @Valid @RequestBody ProcessOrderRemoveDto dto) {
        return CommonResult.success(processOrderBizService.quit(dto));
    }

    @PostMapping("/editProcedure")
    @ApiOperation("编辑工序")
    public CommonResult<Boolean> addProcedure(@NotNull @Valid @RequestBody ProcessOrderAddProcedureDto dto) {
        return CommonResult.success(processOrderBizService.editProcedure(dto));
    }


    @PostMapping("/confirmDeliver")
    @ApiOperation("确认发货")
    public CommonResult<Boolean> confirmDeliver(@NotNull @Valid @RequestBody ProcessOrderConfirmDeliverDto dto) {
        return CommonResult.success(processOrderBizService.confirmDeliver(dto, new OperatorUserBo()));
    }

    @PostMapping("/completeHandover")
    @ApiOperation("加工单完成交接")
    public CommonResult<Void> completeHandover(@NotNull @Valid @RequestBody ProcessOrderCompleteHandoverDto dto) {
        processOrderBizService.completeHandover(dto);
        return CommonResult.success();
    }

    @PostMapping("/batchCompleteHandover")
    @ApiOperation("批量完成交接")
    public CommonResult<Void> completeHandover(@NotNull @Valid @RequestBody ProcessOrderCompleteHandoverListDto dto) {
        processOrderBizService.batchCompleteHandover(dto);
        return CommonResult.success();
    }

    @PostMapping("/addMaterial")
    @ApiOperation("补充原料")
    public CommonResult<Void> addMaterial(@NotNull @Valid @RequestBody ProcessOrderMaterialAddDto dto) {
        processOrderBizService.addMaterial(dto);
        return CommonResult.success();
    }

    @PostMapping("/backMaterial")
    @ApiOperation("归还原料")
    public CommonResult<Boolean> backMaterial(@NotNull @Valid @RequestBody ProcessOrderMaterialBackDto dto) {
        return CommonResult.success(processOrderBizService.backMaterial(dto));
    }

    @PostMapping("/getProductInfoListBySku")
    @ApiOperation("查询加工单生产信息")
    public CommonResult<ResultList<SampleChildOrderInfoVo>> getProcessOrderSampleList(@NotNull @Valid @RequestBody ProcessOrderNoDto dto) {
        return CommonResult.successForList(processOrderBizService.getProcessOrderSampleList(dto));
    }

    @PostMapping("/getFileCodeListBySku")
    @ApiOperation("查询图片信息")
    public CommonResult<ResultList<String>> getFileCodeListBySku(@NotNull @Valid @RequestBody SkuPlatformDto dto) {
        return CommonResult.successForList(processOrderBizService.getFileCodeListBySku(dto));
    }

    @PostMapping("/getExportListByOrder")
    @ApiOperation("按单导出")
    public CommonPageResult<ProcessOrderExportByOrderVo> getExportListByOrder(@NotNull @Valid @RequestBody ProcessOrderQueryByApiDto dto) {
        return new CommonPageResult<>(processOrderBizService.getExportListByOrder(dto));
    }

    @PostMapping("/getExportListByItem")
    @ApiOperation("按成品导出")
    public CommonPageResult<ProcessOrderExportByItemVo> getExportListByItem(@NotNull @Valid @RequestBody ProcessOrderQueryByApiDto dto) {
        return new CommonPageResult<>(processOrderBizService.getExportListByItem(dto));
    }

    @PostMapping("/getExportListByMaterial")
    @ApiOperation("按原料导出")
    public CommonPageResult<ProcessOrderExportByMaterialVo> getExportListByMaterial(@NotNull @Valid @RequestBody ProcessOrderQueryByApiDto dto) {
        return new CommonPageResult<>(processOrderBizService.getExportListByMaterial(dto));
    }

    @PostMapping("/getDeliveryOrder")
    @ApiOperation("原料出库信息")
    public CommonResult<ResultList<ProcessOrderDeliveryOrderVo>> getDeliveryOrder(@NotNull @Valid @RequestBody ProcessOrderDeliveryOrderDto dto) {
        return CommonResult.successForList(processOrderBizService.getDeliveryOrder(dto));
    }

    @PostMapping("/getReceiveOrder")
    @ApiOperation("原料入库信息")
    public CommonResult<ResultList<ProcessOrderReceiveOrderVo>> getReceiveOrder(@NotNull @Valid @RequestBody ProcessOrderDeliveryOrderDto dto) {
        return CommonResult.successForList(processOrderBizService.getReceiveOrder(dto));
    }

    @PostMapping("/getCompleteReceiveOrder")
    @ApiOperation("加工单明细成品入库信息")
    public CommonResult<ResultList<ProcessOrderCompleteReceiveOrderVo>> getCompleteReceiveOrder(@NotNull @Valid @RequestBody ProcessOrderDeliveryOrderDto dto) {
        return CommonResult.successForList(processOrderBizService.getCompleteReceiveOrder(dto));
    }

    @ApiOperation("加工单收货信息")
    @PostMapping("/procReceOrderInfo")
    public CommonResult<ProcRecOrderInfoVo> getProcRecOrderInfo(@NotNull @Valid @RequestBody ProcRecOrderInfoDto dto) {
        return CommonResult.success(processOrderBizService.getProcRecOrderInfo(dto));
    }

    @PostMapping("/updateProcessOrderNeedProcessPlan")
    @ApiOperation("更新加工单排产状态")
    public CommonResult<Boolean> updateProcessOrderNeedProcessPlan(@NotNull @Valid @RequestBody UpdateNeedProcessPlanDto dto) {
        processOrderBizService.updateProcessOrderNeedProcessPlan(dto);
        return CommonResult.success(true);
    }

    @PostMapping("/refreshProcessOrderProcedure")
    @ApiOperation("更新加工单工序信息")
    public CommonResult<Boolean> refreshProcessOrderProcedure(@NotNull @Valid @RequestBody RefreshProcessOrderProcedureDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        processOrderBizService.refreshProcessOrderProcedures(processOrderNo);
        return CommonResult.success(true);
    }

    @PostMapping("/refreshProcessOrderMaterial")
    @ApiOperation("更新加工单原料信息")
    public CommonResult<Boolean> refreshProcessOrderMaterial(@NotNull @Valid @RequestBody RefreshProcessOrderMaterialDto dto) {
        String processOrderNo = dto.getProcessOrderNo();
        processOrderBizService.refreshProcessOrderMaterials(processOrderNo);
        return CommonResult.success(true);
    }

    @PostMapping("/getSkuProcedures")
    @ApiOperation("通过sku获取工序信息")
    public CommonResult<ResultList<SkuProcedureVo>> getSkuProcedures(@NotNull @Valid @RequestBody GetSkuProcedureDto dto) {
        return CommonResult.successForList(processOrderBaseService.getSkuProcedures(dto));
    }

    @PostMapping("/getSkuMaterials")
    @ApiOperation("通过sku获取原料信息")
    public CommonResult<ResultList<SkuMaterialVo>> getSkuMaterials(@NotNull @Valid @RequestBody GetSkuMaterialDto dto) {
        return CommonResult.successForList(processOrderBaseService.getSkuMaterials(dto));
    }

    @ApiOperation("获取加工单生产信息")
    @PostMapping("/getProcessOrderProductionInfo")
    public CommonResult<ProcessOrderProductionInfoVo> getProcessOrderGenerateInfo(@Valid @NotNull @RequestBody ProcessOrderGenerateInfoDto dto) {
        ProcessOrderProductionInfoVo procProdInfoVo = processOrderBizService.getProcProdInfoVo(dto);
        return CommonResult.success(procProdInfoVo);
    }

    @PostMapping("/inventoryShortageReportExport")
    @ApiOperation("加工单缺货报表导出")
    public CommonResult<Void> exportInventoryShortageReport(@NotNull @RequestBody InventoryShortageReportExportDto dto) {
        exportBizService.exportInventoryShortageReport(dto);
        return CommonResult.success();
    }

    @ApiOperation("根据加工单类型获取仓库下拉列表")
    @PostMapping("/getWarehouseList")
    public CommonResult<ResultList<WarehouseListVo>> getWarehouseList(@NotNull @RequestBody QueryProcOrderWarehouseInfoDto queryProcOrderWarehouseInfoDto) {
        List<WarehouseListVo> warehouseList = processOrderBizService.getWarehouseList(queryProcOrderWarehouseInfoDto);
        return CommonResult.successForList(warehouseList);
    }

    @ApiOperation("根据加工单类型获取仓库下拉列表")
    @PostMapping("/refreshPromiseDateDelayed")
    public CommonResult<Void> refreshPromiseDateDelayed() {
        processOrderBizService.refreshPromiseDateDelayed();
        return CommonResult.success();
    }

    @PostMapping("/getMaterialInfo")
    @ApiOperation("获取原料信息")
    public CommonResult<ResultList<MaterialInfoVo>> getMaterialInfo(@NotNull @Valid @RequestBody MaterialInfoReqDto dto) {
        return CommonResult.successForList(processOrderBizService.getMaterialInfo(dto));
    }

    @ApiOperation("获取复杂工序配置")
    @PostMapping("/getComplexProcConfig")
    public CommonResult<ComplexProcConfigVo> getComplexProcConfig() {
        return CommonResult.success(processOrderBizService.getComplexProcConfig());
    }
}
