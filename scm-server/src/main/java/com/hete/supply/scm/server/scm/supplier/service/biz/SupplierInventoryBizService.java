package com.hete.supply.scm.server.scm.supplier.service.biz;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.ApproveInventoryChangeRecordDto;
import com.hete.supply.scm.api.scm.entity.dto.InventoryRecordDto;
import com.hete.supply.scm.api.scm.entity.dto.SearchInventoryDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryExportVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierInventoryRecordExportVo;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.dao.ProduceDataAttrDao;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import com.hete.supply.scm.server.scm.nacosconfig.ProduceDataProp;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.stockup.dao.StockUpOrderDao;
import com.hete.supply.scm.server.scm.stockup.dao.StockUpOrderItemDao;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderItemPo;
import com.hete.supply.scm.server.scm.stockup.entity.po.StockUpOrderPo;
import com.hete.supply.scm.server.scm.supplier.builder.SupplierInventoryBuilder;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierInventoryConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryRecordDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierProductCompareDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCodeAndSkuBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryChangeBo;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierAndSkuDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.InventoryRecordVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SearchInventoryVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierSkuInventoryVo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierInventoryBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeDto;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/10 15:37
 */
@Service
@RequiredArgsConstructor
@Validated
public class SupplierInventoryBizService {
    private final SupplierInventoryDao supplierInventoryDao;
    private final SupplierInventoryRecordDao supplierInventoryRecordDao;
    private final PlmRemoteService plmRemoteService;
    private final ConsistencySendMqService consistencySendMqService;
    private final SupplierInventoryBaseService supplierInventoryBaseService;
    private final SupplierProductCompareDao supplierProductCompareDao;
    private final StockUpOrderDao stockUpOrderDao;
    private final StockUpOrderItemDao stockUpOrderItemDao;
    private final ProduceDataAttrDao produceDataAttrDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;

    private final ProduceDataProp produceDataProp;

    public CommonPageResult.PageInfo<SearchInventoryVo> searchInventory(SearchInventoryDto dto) {
        //产品名称
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }

        final CommonPageResult.PageInfo<SearchInventoryVo> pageInfo = supplierInventoryDao.searchInventory(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<SearchInventoryVo> records = pageInfo.getRecords();
        final List<String> skuList = records.stream()
                .map(SearchInventoryVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        records.forEach(record -> {
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            record.setInsStockUpInventory(record.getStockUpInventory() + record.getFrzStockUpInventory());
            record.setInsSelfProvideInventory(record.getSelfProvideInventory() + record.getFrzSelfProvideInventory());
            record.setInsDefectiveInventory(record.getDefectiveInventory() + record.getFrzDefectiveInventory());
        });

        return pageInfo;
    }

    public CommonPageResult.PageInfo<InventoryRecordVo> searchInventoryRecord(InventoryRecordDto dto) {
        //产品名称
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }
        final CommonPageResult.PageInfo<InventoryRecordVo> pageInfo = supplierInventoryRecordDao.searchInventoryRecord(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<InventoryRecordVo> records = pageInfo.getRecords();
        final List<String> skuList = records.stream()
                .map(InventoryRecordVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        records.forEach(record -> {
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
        });

        return pageInfo;
    }

    public List<SupplierSkuInventoryVo> getInventoryBySupplierAndSku(SupplierAndSkuDto dto) {
        final List<SupplierCodeAndSkuBo> supplierCodeAndSkuBoList = dto.getSupplierAndSkuItemList().stream()
                .map(itemDto -> {
                    final SupplierCodeAndSkuBo supplierCodeAndSkuBo = new SupplierCodeAndSkuBo();
                    supplierCodeAndSkuBo.setSku(itemDto.getSku());
                    supplierCodeAndSkuBo.setSupplierCode(itemDto.getSupplierCode());
                    return supplierCodeAndSkuBo;
                }).collect(Collectors.toList());

        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCodeAndSkuBoList);

        final List<SupplierSkuInventoryVo> supplierSkuInventoryVoList = SupplierInventoryConverter.convertSupplierInventoryPoToVo(dto.getSupplierAndSkuItemList(), supplierInventoryPoList);

        supplierSkuInventoryVoList.forEach(vo -> {
            Integer otherSupplierInventory = supplierInventoryDao.getOtherSupplierInventory(vo.getSku(), vo.getSupplierCode());
            vo.setOtherSupplierInventory(otherSupplierInventory);
        });

        return supplierSkuInventoryVoList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void inventoryChange(InventoryChangeDto dto) {
        List<InventoryChangeItemDto> inventoryChangeItemList = dto.getInventoryChangeItemList();
        final List<SupplierCodeAndSkuBo> supplierCodeAndSkuBoList = SupplierInventoryConverter.convertChangeDtoToSupplierSkuBo(inventoryChangeItemList);
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCodeAndSkuBoList);
        if (inventoryChangeItemList.size() != supplierInventoryPoList.size()) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试");
        }

        // 定义不需要审核的供应商仓库类型列表
        List<SupplierWarehouse> notNeedApproveSupplierWarehouses
                = List.of(SupplierWarehouse.SELF_PROVIDE, SupplierWarehouse.DEFECTIVE_WAREHOUSE);

        List<InventoryChangeItemDto> notNeedApproveDto = inventoryChangeItemList.stream()
                .filter(inventoryChangeDto -> notNeedApproveSupplierWarehouses.contains(
                        inventoryChangeDto.getSupplierWarehouse()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notNeedApproveDto)) {
            for (InventoryChangeItemDto updateSupplierInventoryDto : notNeedApproveDto) {
                String supplierCode = updateSupplierInventoryDto.getSupplierCode();
                String sku = updateSupplierInventoryDto.getSku();
                Integer curChangeCnt = updateSupplierInventoryDto.getInventoryChangeCnt();
                SupplierWarehouse supplierWarehouse = updateSupplierInventoryDto.getSupplierWarehouse();
                String recordRemark = updateSupplierInventoryDto.getRecordRemark();

                supplierInventoryBaseService.handleInventoryChange(supplierWarehouse, List.of(updateSupplierInventoryDto));

                SupplierInventoryPo matchSupplierInventoryPo = supplierInventoryPoList.stream()
                        .filter(supplierInventoryPo -> Objects.equals(supplierCode,
                                supplierInventoryPo.getSupplierCode()) && Objects.equals(
                                sku, supplierInventoryPo.getSku()))
                        .findFirst()
                        .orElse(null);
                // 保存已生效的库存变更记录
                SupplierInventoryRecordPo supplierInventoryRecordPo
                        = SupplierInventoryBuilder.buildSupplierInventoryRecordPo(matchSupplierInventoryPo,
                        supplierWarehouse,
                        curChangeCnt,
                        SupplierInventoryCtrlReason.SUPPLIER_CTRL,
                        SupplierInventoryRecordStatus.EFFECTIVE,
                        recordRemark);
                supplierInventoryRecordDao.insert(supplierInventoryRecordPo);
            }
        }

        // 定义需要审核的供应商仓库类型列表
        List<SupplierWarehouse> needApproveSupplierWarehouses
                = List.of(SupplierWarehouse.STOCK_UP);

        List<InventoryChangeItemDto> needApproveDto = inventoryChangeItemList.stream()
                .filter(inventoryChangeDto -> needApproveSupplierWarehouses.contains(
                        inventoryChangeDto.getSupplierWarehouse()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(needApproveDto)) {
            final List<SupplierInventoryChangeBo> supplierInventoryChangeBoList = new ArrayList<>();
            for (InventoryChangeItemDto updateSupplierInventoryDto : needApproveDto) {
                String supplierCode = updateSupplierInventoryDto.getSupplierCode();
                String sku = updateSupplierInventoryDto.getSku();
                SupplierWarehouse supplierWarehouse = updateSupplierInventoryDto.getSupplierWarehouse();
                Integer curChangeCnt = updateSupplierInventoryDto.getInventoryChangeCnt();
                String recordRemark = updateSupplierInventoryDto.getRecordRemark();

                // 保存已生效的库存变更记录
                SupplierInventoryPo matchSupplierInventoryPo = supplierInventoryPoList.stream()
                        .filter(supplierInventoryPo -> Objects.equals(supplierCode,
                                supplierInventoryPo.getSupplierCode()) && Objects.equals(
                                sku, supplierInventoryPo.getSku()))
                        .findFirst()
                        .orElse(null);
                SupplierInventoryRecordPo supplierInventoryRecordPo
                        = SupplierInventoryBuilder.buildSupplierInventoryRecordPo(matchSupplierInventoryPo,
                        supplierWarehouse,
                        curChangeCnt,
                        SupplierInventoryCtrlReason.SUPPLIER_CTRL,
                        SupplierInventoryRecordStatus.PENDING_APPROVAL,
                        recordRemark);
                supplierInventoryRecordDao.insert(supplierInventoryRecordPo);

                /**
                 * 库存增加时（发起）--可用库存不变，增加冻结库存
                 * 库存减少时（发起）--可用库存减少，冻结库存增加
                 */
                final SupplierInventoryChangeBo supplierInventoryChangeBo = new SupplierInventoryChangeBo();
                if (SupplierWarehouse.STOCK_UP.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                    if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                        supplierInventoryChangeBo.setFrzStockUpInventory(supplierInventoryRecordPo.getCtrlCnt());
                    } else {
                        supplierInventoryChangeBo.setStockUpInventory(supplierInventoryRecordPo.getCtrlCnt());
                        supplierInventoryChangeBo.setFrzStockUpInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    }
                } else if (SupplierWarehouse.SELF_PROVIDE.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                    if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                        supplierInventoryChangeBo.setFrzSelfProvideInventory(supplierInventoryRecordPo.getCtrlCnt());
                    } else {
                        supplierInventoryChangeBo.setSelfProvideInventory(supplierInventoryRecordPo.getCtrlCnt());
                        supplierInventoryChangeBo.setFrzSelfProvideInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    }
                } else if (SupplierWarehouse.DEFECTIVE_WAREHOUSE.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                    if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                        supplierInventoryChangeBo.setFrzDefectiveInventory(supplierInventoryRecordPo.getCtrlCnt());
                    } else {
                        supplierInventoryChangeBo.setDefectiveInventory(supplierInventoryRecordPo.getCtrlCnt());
                        supplierInventoryChangeBo.setFrzDefectiveInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    }
                }
                supplierInventoryChangeBo.setSku(supplierInventoryRecordPo.getSku());
                supplierInventoryChangeBo.setSupplierCode(supplierInventoryRecordPo.getSupplierCode());
                supplierInventoryChangeBoList.add(supplierInventoryChangeBo);
            }
            supplierInventoryBaseService.inventoryChange(supplierInventoryChangeBoList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSupplierInventory(SearchInventoryDto dto, FileOperateBizType fileOperateBizType) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                fileOperateBizType.getCode(), dto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exportSupplierInventoryRecord(InventoryRecordDto dto, FileOperateBizType fileOperateBizType) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                fileOperateBizType.getCode(), dto));
    }

    public Integer getExportTotals(SearchInventoryDto dto) {
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }

        return supplierInventoryDao.getExportTotals(dto);
    }

    public CommonResult<ExportationListResultBo<SupplierInventoryExportVo>> getExportList(SearchInventoryDto dto) {
        ExportationListResultBo<SupplierInventoryExportVo> resultBo = new ExportationListResultBo<>();

        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }

        final CommonPageResult.PageInfo<SupplierInventoryExportVo> pageInfo = supplierInventoryDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<SupplierInventoryExportVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        List<String> skuList = records.stream()
                .map(SupplierInventoryExportVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        final List<ProduceDataAttrPo> produceDataAttrPoList = produceDataAttrDao.getListBySkuList(skuList,
                Arrays.asList(produceDataProp.getLaceAreaAttributeNameId(), produceDataProp.getCompleteLongSizeNameId(),
                        produceDataProp.getMaterialSizeNameId()));
        final Map<String, String> skuAttrNameAttrValueMap = produceDataAttrPoList.stream()
                .collect(Collectors.groupingBy(po -> po.getSku() + po.getAttributeNameId(),
                        Collectors.mapping(ProduceDataAttrPo::getAttrValue, Collectors.joining(","))));

        final List<StockUpOrderPo> stockUpOrderPoList = stockUpOrderDao.getBySkuListAndStatus(skuList,
                Arrays.asList(StockUpOrderStatus.TO_BE_ACCEPT, StockUpOrderStatus.IN_PROGRESS));
        final List<String> stockUpOrderNoList = stockUpOrderPoList.stream()
                .map(StockUpOrderPo::getStockUpOrderNo)
                .collect(Collectors.toList());
        final List<StockUpOrderItemPo> stockUpOrderItemPoList = stockUpOrderItemDao.getListByStockUpOrderNoList(stockUpOrderNoList);
        final Map<String, List<StockUpOrderPo>> skuStockUpPoListMap = stockUpOrderPoList.stream()
                .collect(Collectors.groupingBy(po -> po.getSku() + po.getSupplierCode()));

        records.forEach(record -> {
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            record.setInsStockUpInventory(record.getStockUpInventory() + record.getFrzStockUpInventory());
            record.setInsSelfProvideInventory(record.getSelfProvideInventory() + record.getFrzSelfProvideInventory());
            record.setInsDefectiveInventory(record.getDefectiveInventory() + record.getFrzDefectiveInventory());
            if (record.getSelfProvideInventory() < 0 || record.getStockUpInventory() < 0 || record.getDefectiveInventory() < 0) {
                record.setInventoryStatusStr(InventoryStatus.TO_BE_MAINTENANCE.getRemark());
            } else {
                record.setInventoryStatusStr(InventoryStatus.NO_MAINTENANCE.getRemark());
            }
            final List<StockUpOrderPo> stockUpOrderPoList1 = skuStockUpPoListMap.get(record.getSku() + record.getSupplierCode());
            if (CollectionUtils.isEmpty(stockUpOrderPoList1)) {
                record.setDeliveryNotReturnCnt(0);
            } else {
                final int placeOrderCnt = stockUpOrderPoList1.stream()
                        .mapToInt(StockUpOrderPo::getPlaceOrderCnt)
                        .sum();
                final List<String> stockUpOrderNoList1 = stockUpOrderPoList1.stream().map(StockUpOrderPo::getStockUpOrderNo)
                        .collect(Collectors.toList());
                final int warehousingCnt = stockUpOrderItemPoList.stream()
                        .filter(itemPo -> stockUpOrderNoList1.contains(itemPo.getStockUpOrderNo()))
                        .mapToInt(StockUpOrderItemPo::getWarehousingCnt)
                        .sum();
                record.setDeliveryNotReturnCnt(placeOrderCnt - warehousingCnt);
            }

            record.setLaceArea(skuAttrNameAttrValueMap.get(record.getSku() + produceDataProp.getLaceAreaAttributeNameId()));
            record.setCompleteLongSize(skuAttrNameAttrValueMap.get(record.getSku() + produceDataProp.getCompleteLongSizeNameId()));
            record.setMaterial(skuAttrNameAttrValueMap.get(record.getSku() + produceDataProp.getMaterialSizeNameId()));
        });
        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    public Integer getRecordExportTotals(InventoryRecordDto dto) {
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }

        return supplierInventoryRecordDao.getExportTotals(dto);
    }

    public CommonResult<ExportationListResultBo<SupplierInventoryRecordExportVo>> getRecordExportList(InventoryRecordDto dto) {
        ExportationListResultBo<SupplierInventoryRecordExportVo> resultBo = new ExportationListResultBo<>();

        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }

        final CommonPageResult.PageInfo<SupplierInventoryRecordExportVo> pageInfo = supplierInventoryRecordDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<SupplierInventoryRecordExportVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        // 查询关联单据数据
        final List<String> relateNoList = records.stream()
                .map(SupplierInventoryRecordExportVo::getRelateNo)
                .distinct()
                .collect(Collectors.toList());

        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(relateNoList);
        final List<String> purchaseSkuList = purchaseChildOrderItemPoList.stream()
                .map(PurchaseChildOrderItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> purchaseChildNoSkuMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo,
                        PurchaseChildOrderItemPo::getSku, (item1, item2) -> item2));
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(relateNoList);
        final Map<String, String> purchaseChildOrderNoSupplierCodeMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo,
                        PurchaseChildOrderPo::getSupplierCode));

        List<String> skuList = records.stream()
                .map(SupplierInventoryRecordExportVo::getSku)
                .collect(Collectors.toList());
        skuList.addAll(purchaseSkuList);
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        Map<String, SupplierProductComparePo> supplierProductCompareMap = supplierProductCompareDao.getBatchSkuMap(skuList);

        records.forEach(record -> {
            record.setSupplierInventoryCtrlTypeStr(record.getSupplierInventoryCtrlType().getRemark());
            record.setSupplierInventoryCtrlReasonStr(record.getSupplierInventoryCtrlReason().getRemark());
            record.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            final SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(record.getSupplierCode() + record.getSku());
            if (null != supplierProductComparePo) {
                record.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
            }

            record.setSupplierWarehouseStr(record.getSupplierWarehouse().getRemark());
            record.setSupplierInventoryRecordStatusStr(Objects.nonNull(
                    record.getSupplierInventoryRecordStatus()) ? record.getSupplierInventoryRecordStatus()
                    .getRemark() : "");

            final String relateNoSku = purchaseChildNoSkuMap.getOrDefault(record.getRelateNo(), "");
            record.setRelateNoSku(relateNoSku);
            record.setRelateSkuEncode(skuEncodeMap.get(relateNoSku));
            final String supplierCode = purchaseChildOrderNoSupplierCodeMap.getOrDefault(record.getRelateNo(), "");
            final SupplierProductComparePo supplierProductComparePo1 = supplierProductCompareMap.get(supplierCode + relateNoSku);
            if (null != supplierProductComparePo1) {
                record.setRelateSkuSupplierProductName(supplierProductComparePo1.getSupplierProductName());
            }
        });
        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void approveInventoryChangeRecord(ApproveInventoryChangeRecordDto dto) {
        List<Long> supplierInventoryRecordIds = dto.getSupplierInventoryRecordIds();
        InventoryApproveResult changeResult = dto.getChangeResult();

        // 前置校验
        List<SupplierInventoryRecordPo> supplierInventoryRecordPos
                = ParamValidUtils.requireNotEmpty(supplierInventoryRecordDao.listByIds(supplierInventoryRecordIds),
                "数据已被更新或删除，请刷新页面后重试。");
        ParamValidUtils.requireEquals(true, supplierInventoryRecordPos.stream()
                        .allMatch(inventoryRecord -> Objects.equals(
                                SupplierInventoryRecordStatus.PENDING_APPROVAL,
                                inventoryRecord.getSupplierInventoryRecordStatus())),
                "批量审核失败！发现非待审核记录，请尝试刷新页面并重新选择待审核的库存变更记录进行批量审核。");

        for (Long supplierInventoryRecordId : supplierInventoryRecordIds) {
            supplierInventoryBaseService.approveInventoryChangeRecord(supplierInventoryRecordId, changeResult);
        }
    }
}
