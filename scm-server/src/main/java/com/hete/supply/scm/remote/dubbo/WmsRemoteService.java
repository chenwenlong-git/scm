package com.hete.supply.scm.remote.dubbo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderMaterialSkuBo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.*;
import com.hete.supply.wms.api.basic.entity.vo.*;
import com.hete.supply.wms.api.basic.facade.*;
import com.hete.supply.wms.api.entry.entity.dto.*;
import com.hete.supply.wms.api.entry.entity.vo.*;
import com.hete.supply.wms.api.entry.facade.OnShelvesFacade;
import com.hete.supply.wms.api.entry.facade.QcFacade;
import com.hete.supply.wms.api.entry.facade.ReceiveOrderFacade;
import com.hete.supply.wms.api.interna.entity.dto.*;
import com.hete.supply.wms.api.interna.entity.vo.*;
import com.hete.supply.wms.api.interna.facade.InventoryFacade;
import com.hete.supply.wms.api.leave.entity.dto.DeliveryOrderCreateDto;
import com.hete.supply.wms.api.leave.entity.dto.DeliveryQueryDto;
import com.hete.supply.wms.api.leave.entity.dto.ProcessOrderQueryDto;
import com.hete.supply.wms.api.leave.entity.dto.PurchaseChildOrderNoDto;
import com.hete.supply.wms.api.leave.entity.vo.DeliveryOrderCreateVo;
import com.hete.supply.wms.api.leave.entity.vo.DeliveryOrderVo;
import com.hete.supply.wms.api.leave.entity.vo.OverseaDeliveryOrderVo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.supply.wms.api.leave.facade.DeliveryOrderFacade;
import com.hete.supply.wms.api.leave.facade.ReturnOrderFacade;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.core.util.CollSplitUtil;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: RockyHuas
 * @date: 2022/12/5 10:25
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class WmsRemoteService {

    // 定义查询库存分页数的常量
    public static final int INVENTORY_PAGE_SIZE = 100;

    @DubboReference(check = false)
    private InventoryFacade inventoryFacade;

    @DubboReference(check = false)
    private DeliveryOrderFacade deliveryOrderFacade;

    @DubboReference(check = false)
    private QcFacade qcFacade;

    @DubboReference(check = false)
    private WmsBasicFacade wmsBasicFacade;

    @DubboReference(check = false)
    private ReceiveOrderFacade receiveOrderFacade;

    @DubboReference(check = false)
    private SkuBatchFacde skuBatchFacde;

    @DubboReference(check = false)
    private ReturnOrderFacade returnOrderFacade;

    @DubboReference(check = false)
    private ContainerFacde containerFacde;

    @DubboReference(check = false)
    private OnShelvesFacade onShelvesFacade;

    @DubboReference(check = false)
    private WarehouseFacade warehouseFacade;


    /**
     * 通过多个批次码获取 sku 列表
     *
     * @param skuBatchCodes
     * @return
     */
    public List<SimpleSkuBatchVo> getSkuBatchByBatchCodes(List<String> skuBatchCodes) {
        if (CollectionUtils.isEmpty(skuBatchCodes)) {
            return Collections.emptyList();
        }
        SkuBatchQueryDto skuBatchQueryDto = new SkuBatchQueryDto();
        skuBatchQueryDto.setBatchCodes(skuBatchCodes);

        CommonResult<ResultList<SimpleSkuBatchVo>> result = skuBatchFacde.getSkuBatchByBatchCodes(skuBatchQueryDto);
        ResultList<SimpleSkuBatchVo> data = DubboResponseUtil.checkCodeAndGetData(result);

        return new ArrayList<>(Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList()));
    }

    /**
     * 查询库存
     *
     * @param dto
     * @return
     */
    public List<SkuInventoryVo> getSkuInventoryList(SkuInstockInventoryQueryDto dto) {
        CommonResult<ResultList<SkuInventoryVo>> result = inventoryFacade.getSkuAvailableInventory(dto);
        ResultList<SkuInventoryVo> data = DubboResponseUtil.checkCodeAndGetData(result);

        return new ArrayList<>(Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList()));
    }

    /**
     * @Description 创建加工原料出库单
     * @author yanjiawei
     * @Date 2024/9/9 11:10
     */
    public DeliveryOrderCreateVo createProcDelivery(String processOrderNo,
                                                    List<ProcessOrderMaterialSkuBo> skus,
                                                    String warehouseCode, String deliveryNote,
                                                    WmsEnum.ProductQuality productQuality,
                                                    String platCode) {
        DeliveryOrderCreateDto deliveryOrderCreateDto = new DeliveryOrderCreateDto();
        deliveryOrderCreateDto.setDeliveryType(WmsEnum.DeliveryType.PROCESS);
        deliveryOrderCreateDto.setRelatedOrderNo(processOrderNo);
        deliveryOrderCreateDto.setTargetWarehouseCode(warehouseCode);
        deliveryOrderCreateDto.setWarehouseCode(warehouseCode);
        deliveryOrderCreateDto.setRemark(deliveryNote);
        deliveryOrderCreateDto.setProductQuality(productQuality);
        deliveryOrderCreateDto.setPlatCode(platCode);

        List<DeliveryOrderCreateDto.DeliveryDetail> deliveryDetails = skus.stream()
                .map(item -> {
                    DeliveryOrderCreateDto.DeliveryDetail deliveryDetail = new DeliveryOrderCreateDto.DeliveryDetail();
                    deliveryDetail.setSkuCode(item.getSku());
                    deliveryDetail.setPlanDeliveryAmount(item.getDeliveryNum());
                    deliveryDetail.setBatchCode(item.getSkuBatchCode());
                    return deliveryDetail;
                }).collect(Collectors.toList());
        deliveryOrderCreateDto.setDeliveryDetails(deliveryDetails);

        CommonResult<DeliveryOrderCreateVo> result = deliveryOrderFacade.create(deliveryOrderCreateDto);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }


    /**
     * 创建批次码并返回map, key为sku,value为skuBatchCode
     *
     * @param skuBatchCreateDto
     * @return
     */
    public Map<String, String> batchCreateBatchCode(@NotNull @Valid SkuBatchCreateDto skuBatchCreateDto) {
        CommonResult<SkuBatchVo> result = skuBatchFacde.batchCreateBatchCode(skuBatchCreateDto);
        SkuBatchVo skuBatchVo = DubboResponseUtil.checkCodeAndGetData(result);
        if (null == skuBatchVo) {
            throw new BizException("批次码创建失败");
        }
        final List<SkuVo> skuList = skuBatchVo.getSkuList();
        if (CollectionUtils.isEmpty(skuList)) {
            throw new BizException("批次码创建失败");
        }

        return skuList.stream()
                .collect(Collectors.toMap(SkuVo::getSkuCode, SkuVo::getBatchCode));
    }

    public BooleanType overseasCanDeliver(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return BooleanType.FALSE;
        }

        final PurchaseChildOrderNoDto purchaseChildOrderNoDto = new PurchaseChildOrderNoDto();
        purchaseChildOrderNoDto.setPurchaseChildOrderNo(purchaseChildOrderNo);
        final CommonResult<OverseaDeliveryOrderVo> result
                = deliveryOrderFacade.getByPurchaseChildOrderNo(purchaseChildOrderNoDto);
        OverseaDeliveryOrderVo overseaDeliveryOrderVo = DubboResponseUtil.checkCodeAndGetData(result);
        return overseaDeliveryOrderVo.getCanShip();

    }

    /**
     * 查询WMS出库单列表信息
     *
     * @author ChenWenLong
     * @date 2023/3/17 16:25
     */
    public List<ProcessDeliveryOrderVo> getProcessDeliveryOrder(String processOrderNo,
                                                                WmsEnum.DeliveryType deliveryType) {
        ProcessOrderQueryDto dto = new ProcessOrderQueryDto();
        dto.setProcessOrderNo(processOrderNo);
        dto.setDeliveryType(deliveryType);
        CommonResult<ResultList<ProcessDeliveryOrderVo>> result = deliveryOrderFacade.getProcessDeliveryOrder(dto);
        ResultList<ProcessDeliveryOrderVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        return new ArrayList<>(Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList()));

    }

    /**
     * 根据出库单号查询出库单信息
     *
     * @param deliverOrderNoList
     * @return
     */
    public List<ProcessDeliveryOrderVo> getDeliveryOrderByDeliverNo(List<String> deliverOrderNoList) {
        if (CollectionUtils.isEmpty(deliverOrderNoList)) {
            return Collections.emptyList();
        }

        return CollSplitUtil.collSplitExec(deliverOrderNoList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME, curDeliverOrderNoList -> {
            ProcessOrderQueryDto dto = new ProcessOrderQueryDto();
            dto.setDeliveryOrderNoList(curDeliverOrderNoList);
            return deliveryOrderFacade.getProcessDeliveryOrder(dto);
        }, result -> DubboResponseUtil.checkCodeAndGetData(result).getList(), "批量查询加工出库单信息返回为空！");
    }

    /**
     * 查询WMS入库单列表信息
     *
     * @author ChenWenLong
     * @date 2023/3/17 16:25
     */
    public List<ReceiveOrderForScmVo> getReceiveOrderList(ReceiveOrderGetDto dto) {
        if (CollectionUtils.isEmpty(dto.getScmBizNoList()) && CollectionUtils.isEmpty(dto.getReceiveOrderNoList())) {
            log.warn("查询WMS入库单列表时查询单号都为空！");
            return new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(dto.getScmBizNoList()) && CollectionUtils.isNotEmpty(dto.getReceiveOrderNoList())) {
            throw new BizException("查询WMS入库单列表时，业务单号和收货单号不能同时存在，请联系管理员！");
        }
        List<String> noList = CollectionUtils.isNotEmpty(dto.getScmBizNoList()) ?
                dto.getScmBizNoList().stream().distinct().collect(Collectors.toList()) :
                dto.getReceiveOrderNoList().stream().distinct().collect(Collectors.toList());
        try {
            return CollSplitUtil.collSplitExec(noList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME, list -> {
                ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
                if (CollectionUtils.isNotEmpty(dto.getReceiveOrderNoList())) {
                    receiveOrderGetDto.setReceiveOrderNoList(list);
                }
                if (CollectionUtils.isNotEmpty(dto.getScmBizNoList())) {
                    receiveOrderGetDto.setScmBizNoList(list);
                }
                receiveOrderGetDto.setReceiveType(dto.getReceiveType());
                return receiveOrderFacade.getReceiveOrderList(receiveOrderGetDto);
            }, result -> DubboResponseUtil.checkCodeAndGetData(result).getList(), "批量查询入库单信息为空！");
        } catch (BizException e) {
            return new ArrayList<>();
        }
    }

    public List<SkuBatchRelateOrderVo> getByRelateOrderNoList(List<String> relateOrderNoList) {
        if (CollectionUtils.isEmpty(relateOrderNoList)) {
            return new ArrayList<>();
        }
        final SkuBatchRelateOrderNoDto skuBatchRelateOrderNoDto = new SkuBatchRelateOrderNoDto();
        skuBatchRelateOrderNoDto.setRelateOrderNoList(relateOrderNoList);
        final CommonResult<ResultList<SkuBatchRelateOrderVo>> result = skuBatchFacde.getByRelateOrderNoList(skuBatchRelateOrderNoDto);
        ResultList<SkuBatchRelateOrderVo> data = DubboResponseUtil.checkCodeAndGetData(result);

        return new ArrayList<>(Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList()));
    }


    /**
     * 获取wms所有仓库
     *
     * @return
     */
    public List<WarehouseVo> getAllWarehouse() {
        final CommonResult<ResultList<WarehouseVo>> result = warehouseFacade.getList();
        ResultList<WarehouseVo> data = DubboResponseUtil.checkCodeAndGetData(result);

        return new ArrayList<>(Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList()));
    }

    /**
     * 根据仓库编码获取仓库信息
     *
     * @param warehouseCodeList
     * @return
     */
    public List<WarehouseVo> getWarehouseByCode(List<String> warehouseCodeList) {
        if (CollectionUtils.isEmpty(warehouseCodeList)) {
            return Collections.emptyList();
        }

        final WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setWarehouseCodeList(warehouseCodeList.stream()
                .distinct()
                .collect(Collectors.toList()));
        final CommonResult<ResultList<WarehouseVo>> result = wmsBasicFacade.getWarehouseByCode(warehouseDto);
        ResultList<WarehouseVo> data = DubboResponseUtil.checkCodeAndGetData(result);

        return new ArrayList<>(Optional.ofNullable(data)
                .map(ResultList::getList)
                .orElse(Collections.emptyList()));
    }


    /**
     * 校验指定仓库编码对应的仓库类型是否匹配给定的仓库类型。
     *
     * @param warehouseCode  仓库编码，不能为空或空字符串。
     * @param warehouseTypes 待匹配的仓库类型。
     * @return 如果存在指定仓库编码且其类型与待匹配的仓库类型一致，返回 true；否则返回 false。
     * @throws ParamIllegalException 如果仓库编码为空或未找到对应的仓库信息。
     */
    public boolean isMatchWarehouseTypes(String warehouseCode,
                                         Set<WmsEnum.WarehouseType> warehouseTypes) {
        if (StringUtils.isBlank(warehouseCode)) {
            throw new ParamIllegalException("归还原料校验虚拟仓异常！仓库编码为空！");
        }
        List<String> warehouseCodes = Collections.singletonList(warehouseCode);
        List<WarehouseVo> warehousesByCode = this.getWarehouseByCode(warehouseCodes);
        if (CollectionUtils.isEmpty(warehousesByCode)) {
            throw new ParamIllegalException("归还原料校验虚拟仓异常！仓库信息不存在！");
        }
        return warehousesByCode.stream()
                .anyMatch(warehouseVo -> warehouseTypes.contains(warehouseVo.getWarehouseType()));
    }


    /**
     * 批量sku创建批量批次码
     *
     * @param dto:
     * @return Map<String, String>
     * @author ChenWenLong
     * @date 2023/6/28 15:34
     */
    public Map<String, String> createBatchCode4SkuList(@NotNull @Valid SkuBatchCreate4SkuListDto dto) {
        List<String> skuCodeList = dto.getSkuCodeList();
        if (CollectionUtils.isEmpty(skuCodeList)) {
            throw new BizException("创建批次码时sku不能为空!");
        }
        if (skuCodeList.stream()
                .distinct()
                .count() < skuCodeList.size()) {
            throw new BizException("创建批次码时sku存在重复!");
        }
        final CommonResult<ResultList<SkuVo>> result = skuBatchFacde.createBatchCode4SkuList(dto);
        ResultList<SkuVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        if (null == data) {
            throw new BizException("批次码创建失败");
        }
        final List<SkuVo> skuList = data.getList();
        if (CollectionUtils.isEmpty(skuList)) {
            throw new BizException("批次码创建失败");
        }

        return skuList.stream()
                .collect(Collectors.toMap(SkuVo::getSkuCode, SkuVo::getBatchCode));
    }

    /**
     * 通过次品记录批量sku创建批量批次码
     *
     * @param dto:
     * @return Map<String, List < String>>
     * @author ChenWenLong
     * @date 2023/6/29 09:41
     */
    public Map<String, List<SkuVo>> createBatchCode4DefectHandlingList(@NotNull @Valid SkuBatchCreate4defectNoListDto dto) {
        List<SkuBatchCreate4defectNoListDto.DefectHandlingDto> defectHandlingNoList = dto.getDefectHandlingNoList();
        List<String> noList = new ArrayList<>();
        for (SkuBatchCreate4defectNoListDto.DefectHandlingDto defectHandlingDto : defectHandlingNoList) {
            if (CollectionUtils.isEmpty(defectHandlingDto.getSkuCodeList())) {
                throw new BizException("创建批次码时sku不能为空!");
            }
            if (noList.contains(defectHandlingDto.getDefectHandlingNo())) {
                throw new BizException("创建批次码时存在重复的单号{}，请核对数据",
                        defectHandlingDto.getDefectHandlingNo());
            }
            noList.add(defectHandlingDto.getDefectHandlingNo());
        }

        CommonResult<ResultList<SkuBatch4DefectHandlingVo>> batchCode4DefectHandlingList
                = skuBatchFacde.createBatchCode4DefectHandlingList(dto);
        ResultList<SkuBatch4DefectHandlingVo> data
                = DubboResponseUtil.checkCodeAndGetData(batchCode4DefectHandlingList);
        if (null == data) {
            throw new BizException("批次码创建失败");
        }
        List<SkuBatch4DefectHandlingVo> list = data.getList();
        if (CollectionUtils.isEmpty(list)) {
            throw new BizException("批次码创建失败");
        }
        for (SkuBatch4DefectHandlingVo skuBatch4DefectHandlingVo : list) {
            if (CollectionUtils.isEmpty(skuBatch4DefectHandlingVo.getSkuList())) {
                throw new BizException("wms返回批次码数据错误，次品记录单号{}的批次码为空",
                        skuBatch4DefectHandlingVo.getDefectHandlingNo());
            }
        }

        return list.stream()
                .collect(Collectors.toMap(SkuBatch4DefectHandlingVo::getDefectHandlingNo,
                        SkuBatch4DefectHandlingVo::getSkuList));
    }

    /**
     * 获取wms容器列表
     *
     * @param dto
     * @return
     */
    public List<Container4ScmListVo> getWmsContainerList(ContainerListQueryDto dto) {
        final CommonPageResult<Container4ScmListVo> pageResult = containerFacde.pageList(dto);
        if (null == pageResult) {
            return Collections.emptyList();
        }
        final CommonPageResult.PageInfo<Container4ScmListVo> container4ScmListVoPageInfo
                = DubboResponseUtil.checkCodeAndGetData(pageResult);

        if (null == container4ScmListVoPageInfo) {
            return Collections.emptyList();
        }

        return container4ScmListVoPageInfo.getRecords();
    }


    /**
     * 更新容器状态
     *
     * @param dto
     * @return
     */
    public ContainerUpdateStateVo updateContainerState(ContainerUpdateStateDto dto) {
        final CommonResult<ContainerUpdateStateVo> pageResult = containerFacde.updateContainerState(dto);
        if (null == pageResult) {
            return null;
        }
        return DubboResponseUtil.checkCodeAndGetData(pageResult);
    }

    /**
     * 检查容器更新状态是否匹配指定的条件。
     *
     * @param containerUpdateResult 容器状态更新的结果对象。
     * @param containerCode         要匹配的容器编码。
     * @param warehouseCode         要匹配的仓库编码。
     * @param state                 要匹配的容器状态。
     * @return 如果容器更新状态匹配指定条件，返回 true；否则返回 false。
     */
    public boolean isContainerUpdateStateMatch(ContainerUpdateStateVo containerUpdateResult,
                                               String containerCode,
                                               String warehouseCode,
                                               WmsEnum.ContainerState state) {
        return Objects.nonNull(containerUpdateResult)
                && Objects.equals(containerCode, containerUpdateResult.getContainerCode())
                && Objects.equals(warehouseCode, containerUpdateResult.getWarehouseCode())
                && Objects.equals(state, containerUpdateResult.getState());
    }

    /**
     * 根据查询条件获取批次可用库存信息列表，可以选择按指定字段对库存信息分组并求和。
     *
     * @param dto              批次库存查询条件
     * @param groupingFunction 用于分组求和的函数，传入一个 BatchCodeInventoryVo 返回分组字段的值
     * @return 批次可用库存信息列表，根据条件可能包含分组求和后的结果
     */
    public List<BatchCodeInventoryVo> listBatchCodeAvailableInventory(BatchCodeInventoryQueryDto dto,
                                                                      Function<BatchCodeInventoryVo, String> groupingFunction) {
        CommonResult<ResultList<BatchCodeInventoryVo>> resultListCommonResult
                = inventoryFacade.listBatchCodeAvailableInventory(dto);
        if (Objects.isNull(resultListCommonResult) || Objects.isNull(resultListCommonResult.getData())) {
            log.info("resultListCommonResult:{}", JSON.toJSONString(resultListCommonResult));
            return Collections.emptyList();
        }
        List<BatchCodeInventoryVo> batchCodeInventoryVoList
                = DubboResponseUtil.checkCodeAndGetData(resultListCommonResult)
                .getList();
        log.info("batchCodeInventoryVoList:{}", JSON.toJSONString(batchCodeInventoryVoList));

        // 没有批次码，对相同sku的可用库存数分组求和
        if (CollectionUtils.isNotEmpty(batchCodeInventoryVoList) && Objects.nonNull(groupingFunction)) {
            Map<String, Integer> skuSumMap = batchCodeInventoryVoList.stream()
                    .collect(Collectors.groupingBy(groupingFunction,
                            Collectors.summingInt(BatchCodeInventoryVo::getInStockAmount)));

            // 使用Lambda表达式和Stream操作剔除重复的skuCode
            List<BatchCodeInventoryVo> removeDuplicateSkus = new ArrayList<>(batchCodeInventoryVoList.stream()
                    .collect(Collectors.toMap(
                            BatchCodeInventoryVo::getSkuCode,
                            vo -> vo,
                            (existing, replacement) -> existing
                    ))
                    .values());
            removeDuplicateSkus.forEach(removeDuplicateSku -> {
                String skuCode = removeDuplicateSku.getSkuCode();
                Integer inStockAmount = skuSumMap.get(skuCode);
                if (Objects.nonNull(inStockAmount)) {
                    removeDuplicateSku.setInStockAmount(inStockAmount);
                }
            });
            return removeDuplicateSkus;
        }
        return batchCodeInventoryVoList;
    }

    /**
     * 通过批次码获取对应关联单据号
     *
     * @param dto:
     * @return List<SkuBatchRelateOrderVo>
     * @author ChenWenLong
     * @date 2023/9/20 14:26
     */
    public List<SkuBatchRelateOrderVo> getRelateOrderNoList(SkuBatchQueryRelateOrderDto dto) {
        ResultList<SkuBatchRelateOrderVo> data
                = DubboResponseUtil.checkCodeAndGetData(skuBatchFacde.getRelateOrderNoList(dto));
        if (null == data) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(data.getList())) {
            return new ArrayList<>();
        }
        return data.getList();
    }

    /**
     * 通过质检单号列表获取WMS上架信息列表。该方法将质检单号列表分割为多个子集，使用多线程异步请求WMS系统获取对应上架信息，
     * 最后合并结果列表。
     *
     * @param qcOrderNos 质检单号列表
     * @return WMS上架信息列表
     */
    public List<OnShelvesOrderScmVo.OnShelvesOrder> getOnShelvesOrderByQcOrderNos(Collection<String> qcOrderNos) {
        // 使用工具类将质检单号列表分割为子集，每个子集包含的质检单号数量由 ScmConstant.SPLIT_SIZE 定义
        return CollSplitUtil.collSplitExec(qcOrderNos, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME, list -> {
            // 构建查询参数对象，设置质检单号列表
            OnShelvesOrderScmQueryDto queryDto = new OnShelvesOrderScmQueryDto();
            queryDto.setQcOrderNoList(list);

            // 发起异步请求获取WMS上架信息
            return onShelvesFacade.getOnShelvesOrderList(queryDto);
        }, result -> {
            // 检查响应结果并提取上架信息列表，如果响应为空或不符合期望，返回空列表
            return Objects.isNull(DubboResponseUtil.checkCodeAndGetData(result)) ? Collections.emptyList() :
                    DubboResponseUtil.checkCodeAndGetData(result)
                            .getOnShelvesOrderList();
        }, "通过质检单获取WMS上架信息异常");
    }


    /**
     * 通过上架单号列表获取WMS上架信息列表。该方法将上架单号列表分割为多个子集，使用多线程异步请求WMS系统获取对应上架信息，
     * 最后合并结果列表。
     *
     * @param onShelvesOrderNos 上架单号列表
     * @return WMS上架信息列表
     */
    public List<OnShelvesOrderScmVo.OnShelvesOrder> getOnShelvesOrderByOnShelvesOrderNos(Collection<String> onShelvesOrderNos) {
        // 使用工具类将上架单号列表分割为子集，每个子集包含的上架单号数量由 ScmConstant.SPLIT_SIZE 定义
        return CollSplitUtil.collSplitExec(onShelvesOrderNos, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME,
                list -> {
                    // 构建查询参数对象，设置上架单号列表
                    OnShelvesOrderScmQueryDto queryDto = new OnShelvesOrderScmQueryDto();
                    queryDto.setOnShelvesOrderNoList(list);

                    // 发起异步请求获取WMS上架信息
                    return onShelvesFacade.getOnShelvesOrderList(queryDto);
                }, result -> {
                    // 检查响应结果并提取上架信息列表，如果响应为空或不符合期望，返回空列表
                    return Objects.isNull(DubboResponseUtil.checkCodeAndGetData(result)) ? Collections.emptyList() :
                            DubboResponseUtil.checkCodeAndGetData(result)
                                    .getOnShelvesOrderList();
                }, "通过上架单号获取WMS上架信息异常");
    }

    /**
     * 通过质检单号列表获取WMS质检信息列表。该方法将质检单号列表分割为多个子集，使用多线程异步请求WMS系统获取对应质检信息，
     * 最后合并结果列表。
     *
     * @param qcOrderNos 质检单号列表
     * @return WMS质检信息列表
     */
    public List<QcOrderToScmVo> getWmsQcOrderByQcOrderNos(Collection<String> qcOrderNos) {
        // 使用工具类将质检单号列表分割为子集，每个子集包含的质检单号数量由 ScmConstant.SPLIT_SIZE 定义
        return CollSplitUtil.collSplitExec(qcOrderNos, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME, list -> {
            // 构建查询参数对象，设置质检单号列表
            QcOrderListDto queryDto = new QcOrderListDto();
            queryDto.setQcOrderNoList(list);

            // 发起异步请求获取WMS质检信息
            return qcFacade.getQcOrderList(queryDto)
                    .getData();
        }, result -> {
            // 检查响应结果并提取质检信息列表，如果响应为空或不符合期望，返回空列表
            return Objects.nonNull(result) ? result.getList() : Collections.emptyList();
        }, "通过质检单号获取WMS质检信息异常");
    }

    /**
     * 根据仓库编码与收货类型校验是否符合
     *
     * @param receiveType
     * @param warehouseCode
     * @return
     */
    public BooleanType mateWarehouseCodeAndReceiveType(WmsEnum.ReceiveType receiveType,
                                                       String warehouseCode) {
        final ReceiveTypeDto receiveTypeDto = new ReceiveTypeDto();
        receiveTypeDto.setReceiveTypeList(List.of(receiveType));
        final CommonResult<ResultList<WarehouseForScmVo>> result
                = receiveOrderFacade.getWarehouseListByReceiveType(receiveTypeDto);
        ResultList<WarehouseForScmVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        final List<WarehouseForScmVo> warehouseForScmVoList = data.getList();

        if (CollectionUtils.isEmpty(warehouseForScmVoList)) {
            return BooleanType.FALSE;
        }
        final List<WarehouseForScmVo.Warehouse> warehouseList = warehouseForScmVoList.stream()
                .map(WarehouseForScmVo::getWarehouseList)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(warehouseList)) {
            return BooleanType.FALSE;
        }
        for (WarehouseForScmVo.Warehouse warehouse : warehouseList) {
            if (warehouse.getWarehouseCode()
                    .equals(warehouseCode)) {
                return BooleanType.TRUE;
            }
        }

        return BooleanType.FALSE;
    }

    /**
     * 单据号支持批量查询WMS出库单列表信息
     *
     * @param relatedOrderNoList:
     * @return List<ProcessDeliveryOrderVo>
     * @author ChenWenLong
     * @date 2023/10/11 11:10
     */
    public List<ProcessDeliveryOrderVo> getProcessDeliveryOrderBatch(List<String> relatedOrderNoList,
                                                                     WmsEnum.DeliveryType deliveryType) {
        if (CollectionUtils.isEmpty(relatedOrderNoList)) {
            return Collections.emptyList();
        }
        final List<String> filterRelatedOrderNoList = relatedOrderNoList.stream()
                .distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        List<ProcessDeliveryOrderVo> processDeliveryOrderVoList
                = CollSplitUtil.collSplitExec(filterRelatedOrderNoList, ScmConstant.SPLIT_SIZE,
                ScmConstant.THREAD_POOL_NAME, list -> {
                    ProcessOrderQueryDto processOrderQueryDto = new ProcessOrderQueryDto();
                    processOrderQueryDto.setDeliveryType(deliveryType);
                    processOrderQueryDto.setRelatedOrderNoList(list);
                    return deliveryOrderFacade.getProcessDeliveryOrder(processOrderQueryDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "根据scm的关联单号查询对应的出库单信息出错！");

        return Optional.ofNullable(processDeliveryOrderVoList)
                .orElse(Collections.emptyList());
    }

    public String createDeliveryOrder(DeliveryOrderCreateDto createDto) {
        CommonResult<DeliveryOrderCreateVo> result = deliveryOrderFacade.create(createDto);
        String deliveryOrderNo = DubboResponseUtil.checkCodeAndGetData(result)
                .getDeliveryOrderNo();
        if (StrUtil.isBlank(deliveryOrderNo)) {
            throw new BizException("创建原料出库单失败，无出库单号！");
        }
        return deliveryOrderNo;
    }

    public List<InventoryForPlmVo.InventoryInfo> getInventoryInfoList(InventoryForPlmDto inventoryForPlmDto) {
        if (Objects.isNull(inventoryForPlmDto)) {
            return Collections.emptyList();
        }
        List<InventoryForPlmDto.InventoryQueryInfo> param = inventoryForPlmDto.getInventoryQueryInfoList();
        if (CollectionUtils.isEmpty(param)) {
            return Collections.emptyList();
        }
        return CollSplitUtil.collSplitExec(param, INVENTORY_PAGE_SIZE, ScmConstant.THREAD_POOL_NAME, cuttingParam -> {
                    InventoryForPlmDto queryParam = new InventoryForPlmDto();
                    queryParam.setPlatCode(inventoryForPlmDto.getPlatCode());
                    queryParam.setInventoryQueryInfoList(cuttingParam);
                    return inventoryFacade.listInventoryForPlm(queryParam).getData();
                }, result -> Objects.nonNull(result) ? result.getInventoryInfoList() :
                        Collections.emptyList(),
                "获取仓储服务库存信息异常！");
    }

    public List<SkuBatchPriceVo> getSkuBatchPriceList(List<String> batchCodes) {
        if (CollectionUtils.isEmpty(batchCodes)) {
            return Collections.emptyList();
        }

        return CollSplitUtil.collSplitExec(batchCodes, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME,
                cuttingBatchCodes -> {
                    SkuBatchPriceQueryDto skuBatchPriceQueryDto
                            = new SkuBatchPriceQueryDto();
                    skuBatchPriceQueryDto.setBatchCodeList(cuttingBatchCodes);
                    return skuBatchFacde.getSkuBatchPriceList(skuBatchPriceQueryDto);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result)
                        .getList(), "批量查询批次码信息为空！");
    }

    /**
     * 获取批次码的价格
     *
     * @param batchCodeList:
     * @return Map<String, BigDecimal>
     * @author ChenWenLong
     * @date 2024/5/29 17:23
     */
    public Map<String, BigDecimal> getSkuBatchPriceMapBySkuBatchList(List<String> batchCodeList) {
        if (CollectionUtils.isEmpty(batchCodeList)) {
            return Collections.emptyMap();
        }

        List<SkuBatchPriceVo> skuBatchPriceList = this.getSkuBatchPriceList(batchCodeList);
        return skuBatchPriceList.stream()
                .collect(Collectors.toMap(SkuBatchPriceVo::getBatchCode, SkuBatchPriceVo::getPrice));
    }


    /**
     * 获取固定批次码的品类id
     *
     * @return
     */
    public List<Long> getRegularSkuCategoryIdList() {
        final CommonResult<ResultList<CategoryIdVo>> categoryIdList = skuBatchFacde.getCategoryIdList();
        final ResultList<CategoryIdVo> categoryIdVoResultList = categoryIdList.getData();
        if (null == categoryIdVoResultList) {
            return Collections.emptyList();
        }
        final List<CategoryIdVo> categoryIdVoList = categoryIdVoResultList.getList();
        if (CollectionUtils.isEmpty(categoryIdVoList)) {
            return Collections.emptyList();
        }
        return categoryIdVoList.stream()
                .map(CategoryIdVo::getCategoryId)
                .collect(Collectors.toList());
    }


    /**
     * 分页获取昨日库存批次码信息
     *
     * @param pageNo
     * @return
     */
    public List<BatchCodeInventoryPageVo> getYestInventoryData(Integer pageNo) {
        final BatchCodeInventoryPageQueryDto dto = new BatchCodeInventoryPageQueryDto();
        dto.setPageNo(pageNo);
        dto.setPageSize(200);
        final CommonPageResult<BatchCodeInventoryPageVo> result = inventoryFacade.pageBatchCodeInventoryBySkuList(dto);
        final CommonPageResult.PageInfo<BatchCodeInventoryPageVo> data = result.getData();
        final List<BatchCodeInventoryPageVo> records = data.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        return records;
    }

    /**
     * 根据仓库编码获取仓库信息
     *
     * @param warehouseCodes
     * @return
     */
    public List<WarehouseVo> listByWarehouseCodes(Set<String> warehouseCodes) {
        if (CollectionUtils.isEmpty(warehouseCodes)) {
            return Collections.emptyList();
        }

        WarehouseDto queryWarehouseDto = new WarehouseDto();
        queryWarehouseDto.setWarehouseCodeList(new ArrayList<>(warehouseCodes));
        CommonResult<ResultList<WarehouseVo>> result = warehouseFacade.getWarehouseByCode(queryWarehouseDto);

        ResultList<WarehouseVo> data = DubboResponseUtil.checkCodeAndGetData(result);
        if (Objects.isNull(data) || CollectionUtils.isEmpty(data.getList())) {
            return Collections.emptyList();
        }
        return data.getList();
    }

    /**
     * 根据出库单号或者业务单据号查询出库单
     *
     * @param dto
     * @return
     */
    public List<DeliveryOrderVo> getListDetailByDeliveryOrderNo(DeliveryQueryDto dto) {
        if (CollectionUtils.isEmpty(dto.getDeliveryOrderNoList()) && CollectionUtils.isEmpty(dto.getExpressOrderNoList())) {
            return Collections.emptyList();
        }

        DeliveryQueryDto param = new DeliveryQueryDto();
        param.setDeliveryOrderNoList(dto.getDeliveryOrderNoList());
        param.setExpressOrderNoList(dto.getExpressOrderNoList());

        final CommonResult<ResultList<DeliveryOrderVo>> result = deliveryOrderFacade.getListDetailByDeliveryOrderNo(param);
        final ResultList<DeliveryOrderVo> deliveryOrderVoResultList = DubboResponseUtil.checkCodeAndGetData(result);
        return deliveryOrderVoResultList.getList();
    }

    /**
     * 获取可用库存
     *
     * @param dto
     * @return
     */
    public List<InventoryVo> getAvailableInventory(AvailableInventoryQueryDto dto) {
        final CommonResult<ResultList<InventoryVo>> result
                = inventoryFacade.getAvailableInventory(dto);

        return result.getData().getList();
    }


    /**
     * 根据仓库类型获取仓库列表
     *
     * @param typeList
     * @return
     */
    public List<WarehouseVo> getWarehouseVoByType(List<WmsEnum.WarehouseType> typeList) {
        if (CollectionUtils.isEmpty(typeList)) {
            return Collections.emptyList();
        }
        return CollSplitUtil.collSplitExec(typeList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME,
                types -> {
                    WarehouseDto param = new WarehouseDto();
                    param.setWarehouseTypeList(types);
                    return warehouseFacade.getWarehouseByCode(param);
                }, result -> DubboResponseUtil.checkCodeAndGetData(result).getList(), "批量查询仓库的信息返回为空！");
    }

    /**
     * 根据收货单号获取收货单信息
     *
     * @param dto
     * @return
     */
    public List<ReceiveDeliveryOrderVo> queryDeliveryOrderNoList(ReceiveDeliveryOrderNoQueryDto dto) {
        final CommonResult<ResultList<ReceiveDeliveryOrderVo>> result
                = receiveOrderFacade.queryDeliveryOrderNoList(dto);

        return result.getData().getList();
    }
}
