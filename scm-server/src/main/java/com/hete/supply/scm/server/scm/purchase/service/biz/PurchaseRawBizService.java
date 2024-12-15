package com.hete.supply.scm.server.scm.purchase.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.RawDeliverBo;
import com.hete.supply.scm.server.scm.entity.dto.RawLocationItemDto;
import com.hete.supply.scm.server.scm.entity.dto.RawProductItemDto;
import com.hete.supply.scm.server.scm.entity.vo.RawReceiveOrderVo;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderRawDeliverDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.SkuStockBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.WmsDeliverBo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.*;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawDeliverPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseDeliverOrderRawVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseInventoryRecordVo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseRawMsgVo;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierInventoryBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierProductCompareBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierWarehouseBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawDispenseMsgItemVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseRawDispenseMsgVo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/21 13:42
 */
@Service
@RequiredArgsConstructor
public class PurchaseRawBizService {
    private final PurchaseChildOrderRawDao purchaseChildOrderRawDao;
    private final PurchaseBaseService purchaseBaseService;
    private final SupplierInventoryBaseService supplierInventoryBaseService;
    private final PurchaseChildOrderRawDeliverDao purchaseChildOrderRawDeliverDao;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseRawBaseService purchaseRawBaseService;
    private final SupplierWarehouseBaseService supplierWarehouseBaseService;
    private final SupplierProductCompareBaseService supplierProductCompareBaseService;
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final LogBaseService logBaseService;

    /**
     * 跟单确认原料处理
     *
     * @param purchaseChildOrderNoList
     * @param purchaseFollowConfirmItemList
     * @param purchaseParentOrderPoList
     * @param purchaseChildOrderPoList
     */
    public void followConfirmRaw(List<String> purchaseChildOrderNoList,
                                 List<PurchaseFollowConfirmItemDto> purchaseFollowConfirmItemList,
                                 List<PurchaseParentOrderPo> purchaseParentOrderPoList,
                                 List<PurchaseChildOrderPo> purchaseChildOrderPoList) {
        // 删除旧的原料
        purchaseChildOrderRawDao.deleteByChildNoList(purchaseChildOrderNoList);

        // 过滤不存在原料列表的数据
        purchaseFollowConfirmItemList = purchaseFollowConfirmItemList.stream()
                .filter(itemDto -> CollectionUtils.isNotEmpty(itemDto.getRawProductItemList()))
                .collect(Collectors.toList());

        // 处理原料参数，给item的采购订单号赋值
        purchaseFollowConfirmItemList.forEach(rawDto ->
                rawDto.getRawProductItemList().forEach(rawProductItemDto ->
                        rawProductItemDto.setPurchaseChildOrderNo(rawDto.getPurchaseChildOrderNo())));

        final Map<String, PurchaseChildOrderPo> purchaseChildNoPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
        // 添加原料数据
        List<PurchaseChildOrderRawPo> allPurchaseChildOrderRawPoList = new ArrayList<>();
        purchaseFollowConfirmItemList.forEach(itemDto -> {
            final List<RawProductItemDto> rawProductItemList = itemDto.getRawProductItemList();
            if (CollectionUtils.isEmpty(rawProductItemList)) {
                return;
            }
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildNoPoMap.get(itemDto.getPurchaseChildOrderNo());

            final List<RawProductItemDto> heteRawItemDtoList = rawProductItemList.stream()
                    .filter(rawDto -> RawSupplier.HETE.equals(rawDto.getRawSupplier()))
                    .collect(Collectors.toList());

            final List<RawProductItemDto> supplierRawItemDtoList = rawProductItemList.stream()
                    .filter(rawDto -> RawSupplier.SUPPLIER.equals(rawDto.getRawSupplier()))
                    .collect(Collectors.toList());

            final List<RawProductItemDto> otherSupplierRawItemDtoList = rawProductItemList.stream()
                    .filter(rawDto -> RawSupplier.OTHER_SUPPLIER.equals(rawDto.getRawSupplier()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(heteRawItemDtoList)) {
                // 若原料提供方为赫特时
                List<PurchaseChildOrderRawPo> formulaPurchaseChildOrderRawPoList = PurchaseConverter.splitDtoToRawItemBomPo(heteRawItemDtoList,
                        purchaseChildOrderPo, PurchaseRawBizType.FORMULA);
                List<PurchaseChildOrderRawPo> demandPurchaseChildOrderRawPoList = PurchaseConverter.splitDtoToRawItemPo(heteRawItemDtoList,
                        purchaseChildOrderPo, PurchaseRawBizType.DEMAND);

                demandPurchaseChildOrderRawPoList.forEach(rawPo -> {
                    final SkuStockBo skuStockBo = new SkuStockBo();
                    skuStockBo.setRawWarehouseCode(rawPo.getRawWarehouseCode());
                    skuStockBo.setProductQuality(WmsEnum.ProductQuality.GOOD);
                    skuStockBo.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
                    skuStockBo.setPurchaseChildOrderRawPoList(Collections.singletonList(rawPo));
                    skuStockBo.setPlatform(purchaseChildOrderPo.getPlatform());
                    // 校验原料库存
                    final BooleanType isStocked = purchaseBaseService.skuStockInventory(skuStockBo);
                    if (BooleanType.FALSE.equals(isStocked)) {
                        throw new ParamIllegalException("原料SKU:{}在该仓库:{}无库存，请确认后重试", rawPo.getSku(), rawPo.getRawWarehouseCode());
                    }
                });
                allPurchaseChildOrderRawPoList.addAll(formulaPurchaseChildOrderRawPoList);
                allPurchaseChildOrderRawPoList.addAll(demandPurchaseChildOrderRawPoList);

            }
            if (CollectionUtils.isNotEmpty(supplierRawItemDtoList)) {
                // 若原料提供方为供应商时，校验供应商是否维护sku原料，同时记录原料信息
                final List<String> rawSkuList = supplierRawItemDtoList.stream()
                        .map(RawProductItemDto::getSku)
                        .distinct()
                        .collect(Collectors.toList());
                List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryBaseService.getInventoryBySkuAndSupplier(rawSkuList, purchaseChildOrderPo.getSupplierCode());
                final List<String> inventorySkuList = supplierInventoryPoList.stream()
                        .map(SupplierInventoryPo::getSku)
                        .distinct()
                        .collect(Collectors.toList());
                final List<String> notExistSkuList = rawSkuList.stream()
                        .filter(rawSku -> !inventorySkuList.contains(rawSku))
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(notExistSkuList)) {
                    // 当无供应商库存时，系统自动创建绑定及供应商库存关系
                    supplierProductCompareBaseService.insertSupplierProductCompareAndInventory(purchaseChildOrderPo.getSupplierCode(), notExistSkuList);
                }

                List<PurchaseChildOrderRawPo> demandPurchaseChildOrderRawPoList = PurchaseConverter.splitDtoToRawItemPo(supplierRawItemDtoList,
                        purchaseChildOrderPo, PurchaseRawBizType.DEMAND);
                List<PurchaseChildOrderRawPo> formulaPurchaseChildOrderRawPoList = PurchaseConverter.splitDtoToRawItemBomPo(supplierRawItemDtoList,
                        purchaseChildOrderPo, PurchaseRawBizType.FORMULA);
                allPurchaseChildOrderRawPoList.addAll(demandPurchaseChildOrderRawPoList);
                allPurchaseChildOrderRawPoList.addAll(formulaPurchaseChildOrderRawPoList);
                // 预占原料 库存预占：可用库存减少，冻结库存增加。优先扣减备货库存再扣减自备库存。
                purchaseRawBaseService.campOnInventory(purchaseChildOrderPo, supplierRawItemDtoList,
                        SupplierInventoryCtrlReason.FOLLOW_CONFIRM);
            }
            if (CollectionUtils.isNotEmpty(otherSupplierRawItemDtoList)) {
                //  其他供应商提供原料
                List<PurchaseChildOrderRawPo> formulaPurchaseChildOrderRawPoList = PurchaseConverter.splitDtoToRawItemBomPo(otherSupplierRawItemDtoList,
                        purchaseChildOrderPo, PurchaseRawBizType.FORMULA);
                List<PurchaseChildOrderRawPo> demandPurchaseChildOrderRawPoList = PurchaseConverter.splitDtoToRawItemPo(otherSupplierRawItemDtoList,
                        purchaseChildOrderPo, PurchaseRawBizType.DEMAND);
                // 获取供应商对应的虚拟仓库，重新赋值仓库
                final List<String> otherSupplierPurchaseList = otherSupplierRawItemDtoList.stream()
                        .map(RawProductItemDto::getPurchaseChildOrderNo)
                        .distinct()
                        .collect(Collectors.toList());
                final Map<String, String> purchaseChildNoSupplierCodeMap = purchaseChildNoPoMap.entrySet().stream()
                        .filter(entry -> otherSupplierPurchaseList.contains(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, PurchaseChildOrderPo::getSupplierCode));
                final List<String> supplierCodeList = new ArrayList<>(purchaseChildNoSupplierCodeMap.values());
                final List<SupplierWarehousePo> supplierWarehousePoList = supplierWarehouseBaseService.getWarehouseBySupplierCode(supplierCodeList,
                        SupplierWarehouse.VIRTUAL_WAREHOUSE);
                final Map<String, SupplierWarehousePo> supplierCodeWarehouseMap = supplierWarehousePoList.stream()
                        .collect(Collectors.toMap(SupplierWarehousePo::getSupplierCode, Function.identity()));
                demandPurchaseChildOrderRawPoList.forEach(rawPo -> {
                    final String supplierCode = purchaseChildNoSupplierCodeMap.getOrDefault(rawPo.getPurchaseChildOrderNo(), "");
                    final SupplierWarehousePo supplierWarehousePo = supplierCodeWarehouseMap.get(supplierCode);
                    if (null != supplierWarehousePo) {
                        rawPo.setRawWarehouseCode(supplierWarehousePo.getWarehouseCode());
                        rawPo.setRawWarehouseName(supplierWarehousePo.getWarehouseName());
                    }
                });

                allPurchaseChildOrderRawPoList.addAll(formulaPurchaseChildOrderRawPoList);
                allPurchaseChildOrderRawPoList.addAll(demandPurchaseChildOrderRawPoList);
            }

            // 原料来源为我司/其他供应商的，要求wms生成出库单
            if (CollectionUtils.isNotEmpty(heteRawItemDtoList)) {
                // 按照原料仓库分组，要求wms 生成原料发货单(非自定库位)
                final Map<String, List<RawProductItemDto>> rawWarehouseCodeRawItemDtoListMap = heteRawItemDtoList.stream()
                        .filter(rawItemDto -> CollectionUtils.isEmpty(rawItemDto.getRawLocationItemList()))
                        .collect(Collectors.groupingBy(RawProductItemDto::getRawWarehouseCode));
                rawWarehouseCodeRawItemDtoListMap.forEach((rawWarehouseCode, rawItemDtoList) -> {
                    final List<RawDeliverBo> purchaseRawDeliverBoList = rawItemDtoList.stream()
                            .map(po -> {
                                final RawDeliverBo purchaseRawDeliverBo = new RawDeliverBo();
                                purchaseRawDeliverBo.setDeliveryCnt(po.getDeliveryCnt());
                                purchaseRawDeliverBo.setSku(po.getSku());
                                return purchaseRawDeliverBo;
                            }).collect(Collectors.toList());

                    // wms 生成原料发货单
                    final WmsDeliverBo wmsDeliverBo = new WmsDeliverBo();
                    wmsDeliverBo.setRelatedOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                    wmsDeliverBo.setRawWarehouseCode(rawWarehouseCode);
                    wmsDeliverBo.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
                    wmsDeliverBo.setRawDeliverMode(RawDeliverMode.RECEIVE_RAW_DELIVER);
                    wmsDeliverBo.setDispatchNow(BooleanType.FALSE);
                    wmsDeliverBo.setPlatform(purchaseChildOrderPo.getPlatform());
                    purchaseRawBaseService.wmsRawDeliver(wmsDeliverBo, purchaseRawDeliverBoList);
                });

                final List<RawProductItemDto> locationRawItemDtoList = heteRawItemDtoList.stream()
                        .filter(rawItemDto -> CollectionUtils.isNotEmpty(rawItemDto.getRawLocationItemList()))
                        .collect(Collectors.toList());
                // 指定库位
                for (RawProductItemDto rawProductItemDto : locationRawItemDtoList) {
                    final RawDeliverBo purchaseRawDeliverBo = new RawDeliverBo();
                    purchaseRawDeliverBo.setDeliveryCnt(rawProductItemDto.getDeliveryCnt());
                    purchaseRawDeliverBo.setSku(rawProductItemDto.getSku());
                    purchaseRawDeliverBo.setParticularLocation(BooleanType.FALSE);
                    final List<RawLocationItemDto> rawLocationItemList = rawProductItemDto.getRawLocationItemList();
                    if (CollectionUtils.isNotEmpty(rawLocationItemList)) {
                        final List<RawDeliverBo.WareLocationDelivery> wareLocationDeliveryList = rawLocationItemList.stream().map(rawLocationItemDto -> {
                            final RawDeliverBo.WareLocationDelivery wareLocationDelivery = new RawDeliverBo.WareLocationDelivery();
                            wareLocationDelivery.setWarehouseLocationCode(rawLocationItemDto.getWarehouseLocationCode());
                            wareLocationDelivery.setDeliveryAmount(rawLocationItemDto.getDeliveryAmount());
                            wareLocationDelivery.setBatchCode(rawLocationItemDto.getBatchCode());
                            return wareLocationDelivery;
                        }).collect(Collectors.toList());

                        purchaseRawDeliverBo.setWareLocationDeliveryList(wareLocationDeliveryList);
                        purchaseRawDeliverBo.setParticularLocation(BooleanType.TRUE);
                    }
                    // wms 生成原料发货单
                    final WmsDeliverBo wmsDeliverBo = new WmsDeliverBo();
                    wmsDeliverBo.setRelatedOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                    wmsDeliverBo.setRawWarehouseCode(rawProductItemDto.getRawWarehouseCode());
                    wmsDeliverBo.setDeliveryType(WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL);
                    wmsDeliverBo.setRawDeliverMode(RawDeliverMode.RECEIVE_RAW_DELIVER);
                    wmsDeliverBo.setDispatchNow(BooleanType.FALSE);
                    wmsDeliverBo.setPlatform(purchaseChildOrderPo.getPlatform());
                    purchaseRawBaseService.wmsRawDeliver(wmsDeliverBo, Collections.singletonList(purchaseRawDeliverBo));
                }
            }
        });
        purchaseChildOrderRawDao.insertOrUpdateBatch(allPurchaseChildOrderRawPoList);

        // 其他供应商类型，创建对应的原料采购需求单
        final List<RawProductItemDto> otherSupplierRawItemDtoList = purchaseFollowConfirmItemList.stream()
                .map(PurchaseFollowConfirmItemDto::getRawProductItemList).flatMap(Collection::stream)
                .filter(rawDto -> RawSupplier.OTHER_SUPPLIER.equals(rawDto.getRawSupplier()))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(otherSupplierRawItemDtoList)) {
            // 获取sku对应的spu
            final List<String> skuList = otherSupplierRawItemDtoList.stream()
                    .map(RawProductItemDto::getSku)
                    .distinct().collect(Collectors.toList());
            final Map<String, String> skuSpuMap = plmRemoteService.getSpuMapBySkuList(skuList);
            otherSupplierRawItemDtoList.forEach(rawDto -> rawDto.setSpu(skuSpuMap.get(rawDto.getSku())));
            final Map<String, PurchaseParentOrderPo> purchaseParentOrderNoPoMap = purchaseParentOrderPoList.stream()
                    .collect(Collectors.toMap(PurchaseParentOrderPo::getPurchaseParentOrderNo, Function.identity()));
            final Map<String, PurchaseParentOrderPo> purchaseChildNoParentPoMap = purchaseChildOrderPoList.stream()
                    .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo,
                            po -> purchaseParentOrderNoPoMap.get(po.getPurchaseParentOrderNo())));

            // 获取供应商对应的虚拟仓库
            final List<String> otherSupplierPurchaseList = otherSupplierRawItemDtoList.stream()
                    .map(RawProductItemDto::getPurchaseChildOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            final List<String> supplierCodeList = purchaseChildNoPoMap.entrySet().stream()
                    .filter(entry -> otherSupplierPurchaseList.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .map(PurchaseChildOrderPo::getSupplierCode)
                    .distinct()
                    .collect(Collectors.toList());
            final List<SupplierWarehousePo> supplierWarehousePoList = supplierWarehouseBaseService.getWarehouseBySupplierCode(supplierCodeList,
                    SupplierWarehouse.VIRTUAL_WAREHOUSE);
            final List<String> virtualSupplierCodeList = supplierWarehousePoList.stream()
                    .map(SupplierWarehousePo::getSupplierCode)
                    .distinct()
                    .collect(Collectors.toList());
            if (supplierWarehousePoList.size() != supplierCodeList.size()) {
                throw new ParamIllegalException("供应商{}未绑定对应虚拟仓，请在基础设置-商家仓库中绑定后进行此操作",
                        supplierCodeList.stream()
                                .filter(supplierCode -> !virtualSupplierCodeList.contains(supplierCode))
                                .collect(Collectors.toList()));
            }
            final Map<String, SupplierWarehousePo> supplierCodeWarehouseMap = supplierWarehousePoList.stream()
                    .collect(Collectors.toMap(SupplierWarehousePo::getSupplierCode, Function.identity()));

            purchaseBaseService.createSupplierRawPurchaseOrder(otherSupplierRawItemDtoList, purchaseChildNoParentPoMap,
                    supplierCodeWarehouseMap, purchaseChildNoPoMap);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void recordDeliveryOrderNo(DeliveryOrderCreateResultEventDto message) {
        // 更新收货单号到对应的原料发货单以及原料数据
        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(message.getRelatedOrderNo());
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return;
        }

        final List<ProcessDeliveryOrderVo> processDeliveryOrderVoList = wmsRemoteService.getDeliveryOrderByDeliverNo(
                Collections.singletonList(message.getDeliveryOrderNo()));
        if (CollectionUtils.isEmpty(processDeliveryOrderVoList)) {
            throw new BizException("wms返回的出库单号：{}有误，请联系系统管理员", message.getDeliveryOrderNo());
        }

        final ProcessDeliveryOrderVo processDeliveryOrderVo = processDeliveryOrderVoList.get(0);
        final List<ProcessDeliveryOrderVo.DeliveryProduct> productList = processDeliveryOrderVo.getProducts();
        if (CollectionUtils.isEmpty(productList)) {
            throw new BizException("出库单:{}数据错误，请联系系统管理员！", message.getDeliveryOrderNo());
        }

        // 赋值实际出库数
        final Map<String, PurchaseChildOrderRawPo> skuActualRawPoMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .filter(rawPo -> StringUtils.isBlank(rawPo.getSkuBatchCode()))
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getSku, Function.identity(), (item1, item2) -> item1));
        final Map<String, PurchaseChildOrderRawPo> skuHeteActualRawPoMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .filter(rawPo -> StringUtils.isBlank(rawPo.getSkuBatchCode()))
                .filter(rawPo -> RawSupplier.HETE.equals(rawPo.getRawSupplier()))
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getSku, Function.identity(), (item1, item2) -> item1));

        final Map<String, Integer> skuActDeliverCntMap = productList.stream()
                .collect(Collectors.groupingBy(ProcessDeliveryOrderVo.DeliveryProduct::getSkuCode,
                        Collectors.summingInt(ProcessDeliveryOrderVo.DeliveryProduct::getAmount)));

        final List<String> skuList = productList.stream()
                .map(ProcessDeliveryOrderVo.DeliveryProduct::getSkuCode)
                .distinct()
                .collect(Collectors.toList());

        final Map<String, PurchaseChildOrderRawPo> skuDemandRawPoList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.DEMAND.equals(rawPo.getPurchaseRawBizType()))
                .filter(rawPo -> skuList.contains(rawPo.getSku()))
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getSku, Function.identity(), (item1, item2) -> item1));

        final PurchaseChildOrderRawPo purchaseChildOrderRawPo = purchaseChildOrderRawPoList.get(0);
        final List<PurchaseChildOrderRawPo> insertRawPoList = new ArrayList<>();
        skuActDeliverCntMap.forEach((sku, actDeliverCnt) -> {
            // 判断来源，如果为接单原料出库类型的，则直接取sku
            // 如果为原料补充出库的，则需要找对应的
            if (RawDeliverMode.RECEIVE_RAW_DELIVER.equals(message.getRawDeliverMode())) {
                PurchaseChildOrderRawPo demandRawPo = skuDemandRawPoList.get(sku);
                if (null == demandRawPo) {
                    throw new BizException("接单原料出库数据错误，采购单:{}不存在demand类型的sku:{}",
                            purchaseChildOrderRawPo.getPurchaseChildOrderNo(), sku);
                }
                PurchaseChildOrderRawPo actualRawPo = skuActualRawPoMap.get(sku);
                if (null == actualRawPo) {
                    actualRawPo = new PurchaseChildOrderRawPo();
                    actualRawPo.setPurchaseParentOrderNo(purchaseChildOrderRawPo.getPurchaseParentOrderNo());
                    actualRawPo.setPurchaseChildOrderNo(purchaseChildOrderRawPo.getPurchaseChildOrderNo());
                    actualRawPo.setSku(sku);
                    actualRawPo.setDeliveryCnt(0);
                    actualRawPo.setDispenseCnt(0);
                    actualRawPo.setPurchaseRawBizType(PurchaseRawBizType.ACTUAL_DELIVER);
                    actualRawPo.setRawSupplier(demandRawPo.getRawSupplier());
                    actualRawPo.setRawExtra(demandRawPo.getRawExtra());
                }
                actualRawPo.setRawWarehouseCode(processDeliveryOrderVo.getWarehouseCode());
                actualRawPo.setRawWarehouseName(processDeliveryOrderVo.getWarehouseName());
                actualRawPo.setDeliveryCnt(actualRawPo.getDeliveryCnt() + actDeliverCnt);
                actualRawPo.setDispenseCnt(actualRawPo.getDispenseCnt() + actDeliverCnt);
                insertRawPoList.add(actualRawPo);
            } else if (RawDeliverMode.SUPPLY_RAW.equals(message.getRawDeliverMode())) {
                PurchaseChildOrderRawPo actualRawPo = skuHeteActualRawPoMap.get(sku);
                if (null == actualRawPo) {
                    actualRawPo = new PurchaseChildOrderRawPo();
                    actualRawPo.setPurchaseParentOrderNo(purchaseChildOrderRawPo.getPurchaseParentOrderNo());
                    actualRawPo.setPurchaseChildOrderNo(purchaseChildOrderRawPo.getPurchaseChildOrderNo());
                    actualRawPo.setSku(sku);
                    actualRawPo.setDeliveryCnt(0);
                    actualRawPo.setDispenseCnt(0);
                    actualRawPo.setPurchaseRawBizType(PurchaseRawBizType.ACTUAL_DELIVER);
                    actualRawPo.setRawExtra(RawExtra.NORMAL);
                    actualRawPo.setRawSupplier(RawSupplier.HETE);
                }
                actualRawPo.setRawWarehouseCode(processDeliveryOrderVo.getWarehouseCode());
                actualRawPo.setRawWarehouseName(processDeliveryOrderVo.getWarehouseName());
                actualRawPo.setDeliveryCnt(actualRawPo.getDeliveryCnt() + actDeliverCnt);
                actualRawPo.setDispenseCnt(actualRawPo.getDispenseCnt() + actDeliverCnt);
                insertRawPoList.add(actualRawPo);
            }
        });

        purchaseChildOrderRawDao.insertOrUpdateBatch(insertRawPoList);

        // 生成出库单关联记录
        final Map<String, Integer> skuDeliverCntMap = productList.stream()
                .collect(Collectors.groupingBy(ProcessDeliveryOrderVo.DeliveryProduct::getSkuCode,
                        Collectors.summingInt(ProcessDeliveryOrderVo.DeliveryProduct::getAmount)));

        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = insertRawPoList.stream()
                .map(rawPo -> PurchaseConverter.rawPoToRawDeliverPo(rawPo, skuDeliverCntMap.get(rawPo.getSku()),
                        message.getDeliveryOrderNo(), 0L, rawPo.getRawSupplier(), message.getParticularLocation()))
                .collect(Collectors.toList());

        final Map<String, PurchaseChildOrderRawDeliverPo> skuPoMap = purchaseChildOrderRawDeliverPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderRawDeliverPo::getSku,
                        Function.identity(), (po1, po2) -> po2));

        purchaseChildOrderRawDeliverDao.insertBatch(skuPoMap.values());
    }

    public List<PurchaseRawMsgVo> getRawMsgByNo(PurchaseChildNoDto dto) {
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList = purchaseChildOrderRawDao.getListByChildNo(dto.getPurchaseChildOrderNo());
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return Collections.emptyList();
        }

        final List<String> skuList = purchaseChildOrderRawPoList.stream()
                .map(PurchaseChildOrderRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        final Map<String, Integer> bomSkuCntMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.FORMULA.equals(rawPo.getPurchaseRawBizType()))
                .collect(Collectors.groupingBy(PurchaseChildOrderRawPo::getSku,
                        Collectors.summingInt(PurchaseChildOrderRawPo::getDeliveryCnt)));

        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = purchaseChildOrderRawDeliverDao.getListByChildOrderNo(dto.getPurchaseChildOrderNo());
        final List<String> deliverOrderNoList = purchaseChildOrderRawDeliverPoList.stream()
                .map(PurchaseChildOrderRawDeliverPo::getPurchaseRawDeliverOrderNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        final List<ProcessDeliveryOrderVo> processDeliveryOrderVoList = wmsRemoteService.getDeliveryOrderByDeliverNo(deliverOrderNoList);
        final Map<String, ProcessDeliveryOrderVo> deliveryNoPoMap = processDeliveryOrderVoList.stream()
                .collect(Collectors.toMap(ProcessDeliveryOrderVo::getDeliveryOrderNo, Function.identity()));
        final Map<String, List<PurchaseChildOrderRawDeliverPo>> skuDeliverOrderNoListMap = purchaseChildOrderRawDeliverPoList.stream()
                .filter(rawDeliverPo -> StringUtils.isNotBlank(rawDeliverPo.getPurchaseRawDeliverOrderNo()))
                .collect(Collectors.groupingBy(PurchaseChildOrderRawDeliverPo::getSku));
        final Map<String, List<PurchaseChildOrderRawDeliverPo>> rawDeliverNoPoMap = purchaseChildOrderRawDeliverPoList.stream()
                .collect(Collectors.groupingBy(rawDeliverPo -> rawDeliverPo.getSku() + rawDeliverPo.getPurchaseRawDeliverOrderNo()));

        final List<Long> recordIdList = purchaseChildOrderRawDeliverPoList.stream()
                .map(PurchaseChildOrderRawDeliverPo::getSupplierInventoryRecordId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        final List<SupplierInventoryRecordPo> supplierInventoryRecordPoList = supplierInventoryBaseService.getRecordListByIdList(recordIdList);
        final Map<Long, SupplierInventoryRecordPo> recordIdPoMap = supplierInventoryRecordPoList.stream()
                .collect(Collectors.toMap(SupplierInventoryRecordPo::getSupplierInventoryRecordId, Function.identity()));

        final Map<String, List<Long>> skuRecordIdListMap = purchaseChildOrderRawDeliverPoList.stream()
                .filter(rawDeliverPo -> null != rawDeliverPo.getSupplierInventoryRecordId())
                .collect(Collectors.groupingBy(PurchaseChildOrderRawDeliverPo::getSku,
                        Collectors.mapping(PurchaseChildOrderRawDeliverPo::getSupplierInventoryRecordId, Collectors.toList())));
        final List<RawReceiveOrderVo> rawReceiveOrderVoList = new ArrayList<>();
        rawReceiveOrderVoList.addAll(purchaseRawBaseService.rawReceiveOrder(dto.getPurchaseChildOrderNo()));
        rawReceiveOrderVoList.addAll(purchaseRawBaseService.getSupplierRecordOrderList(dto.getPurchaseChildOrderNo()));

        final Map<String, List<RawReceiveOrderVo>> skuReceiveVoMap = rawReceiveOrderVoList.stream()
                .collect(Collectors.groupingBy(RawReceiveOrderVo::getSku));

        // 获取实际发货数
        final Map<String, Integer> skuDeliveryMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .collect(Collectors.groupingBy(rawPo -> rawPo.getRawSupplier().getRemark() + rawPo.getSku(),
                        Collectors.summingInt(PurchaseChildOrderRawPo::getDeliveryCnt)));
        // 获取实际分配数
        final Map<String, Integer> skuDispenseCntMap = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .collect(Collectors.groupingBy(rawPo -> rawPo.getRawSupplier().getRemark() + rawPo.getSku(),
                        Collectors.summingInt(PurchaseChildOrderRawPo::getDispenseCnt)));

        final List<PurchaseRawMsgVo> resultList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.DEMAND.equals(rawPo.getPurchaseRawBizType()))
                .map(rawPo -> {
                    final PurchaseRawMsgVo purchaseRawMsgVo = new PurchaseRawMsgVo();
                    purchaseRawMsgVo.setRawSupplier(rawPo.getRawSupplier());
                    purchaseRawMsgVo.setSku(rawPo.getSku());
                    purchaseRawMsgVo.setSkuEncode(skuEncodeMap.get(rawPo.getSku()));
                    purchaseRawMsgVo.setSkuBomCnt(bomSkuCntMap.getOrDefault(rawPo.getSku(), 0));
                    purchaseRawMsgVo.setExceptConsumeCnt(rawPo.getDeliveryCnt());
                    final Integer skuDeliveryCnt = skuDeliveryMap.get(rawPo.getRawSupplier().getRemark() + rawPo.getSku());
                    purchaseRawMsgVo.setActualDeliveryCnt(null == skuDeliveryCnt ? rawPo.getDeliveryCnt() : skuDeliveryCnt);
                    final Integer skuDispenseCnt = skuDispenseCntMap.get(rawPo.getRawSupplier().getRemark() + rawPo.getSku());
                    purchaseRawMsgVo.setDispenseCnt(null == skuDispenseCnt ? rawPo.getDispenseCnt() : skuDispenseCnt);

                    // 原料出库单详情
                    this.rawPoDeliverOrderDetail(skuDeliverOrderNoListMap, deliveryNoPoMap, rawDeliverNoPoMap, rawPo, purchaseRawMsgVo,
                            skuRecordIdListMap, recordIdPoMap);
                    // 原料入库单详情
                    this.rawPoReceiveOrderDetail(skuReceiveVoMap, rawPo, purchaseRawMsgVo);


                    return purchaseRawMsgVo;
                }).collect(Collectors.toList());

        // 加上补充原料的原料展示
        // 获取需求类型的sku
        final List<String> demandRawSupplierSkuList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.DEMAND.equals(rawPo.getPurchaseRawBizType()))
                .map(rawPo -> rawPo.getRawSupplier().getRemark() + rawPo.getSku())
                .distinct()
                .collect(Collectors.toList());

        // 实发原料且不在需求类型sku里面的属于补充原料
        final List<PurchaseChildOrderRawPo> supplyRawSkuList = purchaseChildOrderRawPoList.stream()
                .filter(rawPo -> PurchaseRawBizType.ACTUAL_DELIVER.equals(rawPo.getPurchaseRawBizType()))
                .filter(rawPo -> StringUtils.isBlank(rawPo.getSkuBatchCode()))
                .filter(rawPo -> !demandRawSupplierSkuList.contains(rawPo.getRawSupplier().getRemark() + rawPo.getSku()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(supplyRawSkuList)) {
            return resultList;
        }
        final List<PurchaseRawMsgVo> supplyRawVoList = supplyRawSkuList.stream().map(rawPo -> {
            final PurchaseRawMsgVo purchaseRawMsgVo = new PurchaseRawMsgVo();
            purchaseRawMsgVo.setRawSupplier(rawPo.getRawSupplier());
            purchaseRawMsgVo.setSku(rawPo.getSku());
            purchaseRawMsgVo.setSkuEncode(skuEncodeMap.get(rawPo.getSku()));
            purchaseRawMsgVo.setSkuBomCnt(bomSkuCntMap.getOrDefault(rawPo.getSku(), 0));
            purchaseRawMsgVo.setExceptConsumeCnt(0);
            purchaseRawMsgVo.setActualDeliveryCnt(skuDeliveryMap.getOrDefault(rawPo.getRawSupplier().getRemark() + rawPo.getSku(), 0));
            purchaseRawMsgVo.setDispenseCnt(rawPo.getDispenseCnt());

            // 原料出库单详情
            this.rawPoDeliverOrderDetail(skuDeliverOrderNoListMap, deliveryNoPoMap, rawDeliverNoPoMap, rawPo, purchaseRawMsgVo,
                    skuRecordIdListMap, recordIdPoMap);
            // 原料入库单详情
            this.rawPoReceiveOrderDetail(skuReceiveVoMap, rawPo, purchaseRawMsgVo);

            return purchaseRawMsgVo;
        }).collect(Collectors.toList());

        resultList.addAll(supplyRawVoList);

        return resultList;
    }

    /**
     * 原料入库单详情
     *
     * @param skuReceiveVoMap
     * @param rawPo
     * @param purchaseRawMsgVo
     */
    private void rawPoReceiveOrderDetail(Map<String, List<RawReceiveOrderVo>> skuReceiveVoMap, PurchaseChildOrderRawPo
            rawPo, PurchaseRawMsgVo purchaseRawMsgVo) {
        purchaseRawMsgVo.setReceiptCnt(0);
        final List<RawReceiveOrderVo> rawReceiveOrderVoList = skuReceiveVoMap.get(rawPo.getSku());
        if (CollectionUtils.isNotEmpty(rawReceiveOrderVoList)) {
            rawReceiveOrderVoList.forEach(vo -> {
                final List<RawReceiveOrderVo.ReceiveOrderVo> receiveOrderVoList = vo.getReceiveOrderVoList();
                final List<RawReceiveOrderVo.ReceiveOrderVo> purchaseReceiptOrderRawList = purchaseRawMsgVo.getPurchaseReceiptOrderRawList();


                final int receiptCnt = receiveOrderVoList.stream()
                        .filter(itemVo -> itemVo.getSku().equals(rawPo.getSku()))
                        .mapToInt(RawReceiveOrderVo.ReceiveOrderVo::getReceiptCnt).sum();
                purchaseRawMsgVo.setReceiptCnt(purchaseRawMsgVo.getReceiptCnt() + receiptCnt);

                final ArrayList<RawReceiveOrderVo.ReceiveOrderVo> resultList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(receiveOrderVoList)) {
                    resultList.addAll(receiveOrderVoList);
                }
                if (CollectionUtils.isNotEmpty(purchaseReceiptOrderRawList)) {
                    resultList.addAll(purchaseReceiptOrderRawList);
                }
                purchaseRawMsgVo.setPurchaseReceiptOrderRawList(resultList);
            });
        }
    }

    /**
     * 赋值原料出库单
     *
     * @param skuDeliverOrderNoListMap
     * @param deliveryNoPoMap
     * @param rawDeliverNoPoMap
     * @param rawPo
     * @param purchaseRawMsgVo
     * @param skuRecordIdListMap
     * @param recordIdPoMap
     */
    private void rawPoDeliverOrderDetail(Map<String, List<PurchaseChildOrderRawDeliverPo>> skuDeliverOrderNoListMap,
                                         Map<String, ProcessDeliveryOrderVo> deliveryNoPoMap,
                                         Map<String, List<PurchaseChildOrderRawDeliverPo>> rawDeliverNoPoMap,
                                         PurchaseChildOrderRawPo rawPo,
                                         PurchaseRawMsgVo purchaseRawMsgVo,
                                         Map<String, List<Long>> skuRecordIdListMap,
                                         Map<Long, SupplierInventoryRecordPo> recordIdPoMap) {
        // 出库单
        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList1 = skuDeliverOrderNoListMap.get(rawPo.getSku());
        if (CollectionUtils.isNotEmpty(purchaseChildOrderRawDeliverPoList1)) {
            // 拿单号
            final List<String> rawDeliverOrderNoList = purchaseChildOrderRawDeliverPoList1.stream()
                    .map(PurchaseChildOrderRawDeliverPo::getPurchaseRawDeliverOrderNo)
                    .collect(Collectors.toList());
            // 根据单号拿wms的出库单信息
            List<ProcessDeliveryOrderVo> rawProcessDeliveryOrderList = rawDeliverOrderNoList.stream()
                    .filter(deliveryNoPoMap::containsKey)
                    .map(deliveryNoPoMap::get)
                    .collect(Collectors.toList());


            final List<PurchaseDeliverOrderRawVo> purchaseDeliverOrderRawVoList = rawProcessDeliveryOrderList.stream()
                    .map(vo -> {
                        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList2 = rawDeliverNoPoMap.get(rawPo.getSku() + vo.getDeliveryOrderNo());
                        // 若原料来源不等则不展示
                        for (PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo : purchaseChildOrderRawDeliverPoList2) {
                            if (!purchaseChildOrderRawDeliverPo.getRawSupplier().equals(rawPo.getRawSupplier())) {
                                return null;
                            }
                        }
                        final Map<String, BooleanType> purchaseRawDeliverParticularLocationMap = purchaseChildOrderRawDeliverPoList2.stream()
                                .filter(po -> StringUtils.isNotBlank(po.getPurchaseRawDeliverOrderNo()))
                                .collect(Collectors.toMap(PurchaseChildOrderRawDeliverPo::getPurchaseRawDeliverOrderNo,
                                        PurchaseChildOrderRawDeliverPo::getParticularLocation, (item1, item2) -> item2));

                        final PurchaseDeliverOrderRawVo purchaseDeliverOrderRawVo = new PurchaseDeliverOrderRawVo();
                        purchaseDeliverOrderRawVo.setDeliveryOrderNo(vo.getDeliveryOrderNo());
                        purchaseDeliverOrderRawVo.setDeliveryNum(vo.getDeliveryAmount());
                        purchaseDeliverOrderRawVo.setDeliveryState(vo.getDeliveryState());
                        purchaseDeliverOrderRawVo.setWarehouseCode(vo.getWarehouseCode());
                        purchaseDeliverOrderRawVo.setWarehouseName(vo.getWarehouseName());
                        if (CollectionUtils.isNotEmpty(vo.getProducts())) {
                            final List<ProcessDeliveryOrderVo.DeliveryProduct> skuProductList = vo.getProducts()
                                    .stream()
                                    .filter(product -> rawPo.getSku().equals(product.getSkuCode()))
                                    .collect(Collectors.toList());
                            purchaseDeliverOrderRawVo.setProductList(skuProductList);
                        }
                        purchaseDeliverOrderRawVo.setParticularLocation(purchaseRawDeliverParticularLocationMap.getOrDefault(vo.getDeliveryOrderNo(), BooleanType.FALSE));
                        return purchaseDeliverOrderRawVo;
                    }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
            purchaseRawMsgVo.setPurchaseDeliverOrderRawList(purchaseDeliverOrderRawVoList);
        }


        // 出库记录
        final List<Long> rawPoRecordIdList = skuRecordIdListMap.get(rawPo.getSku());
        final List<SupplierInventoryRecordPo> rawPoRecordPoList = Optional.ofNullable(rawPoRecordIdList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(recordIdPoMap::containsKey)
                .map(recordIdPoMap::get)
                .collect(Collectors.toList());

        final List<PurchaseInventoryRecordVo> purchaseInventoryRecordList = rawPoRecordPoList.stream().map(recordPo -> {
            final PurchaseInventoryRecordVo purchaseInventoryRecordVo = new PurchaseInventoryRecordVo();
            purchaseInventoryRecordVo.setSupplierInventoryRecordId(recordPo.getSupplierInventoryRecordId());
            purchaseInventoryRecordVo.setCtrlCnt(recordPo.getCtrlCnt());
            purchaseInventoryRecordVo.setSupplierWarehouse(recordPo.getSupplierWarehouse());

            return purchaseInventoryRecordVo;
        }).collect(Collectors.toList());
        purchaseRawMsgVo.setPurchaseInventoryRecordList(purchaseInventoryRecordList);
    }

    public List<PurchaseRawDispenseMsgVo> getRawDispenseCntMsgByNo(PurchaseDispenseDto dto) {
        final List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList = purchaseChildOrderRawDeliverDao.getListIdListOrNoList(dto.getDeliveryOrderNoList(), dto.getSupplierInventoryRecordIdList());

        if (RawSupplier.SUPPLIER.equals(dto.getRawSupplier())) {
            final Map<Long, List<PurchaseChildOrderRawDeliverPo>> rawDeliverNoPoListMap = purchaseChildOrderRawDeliverPoList.stream()
                    .collect(Collectors.groupingBy(PurchaseChildOrderRawDeliverPo::getSupplierInventoryRecordId));

            return rawDeliverNoPoListMap.entrySet().stream()
                    .map(entry -> {
                        final PurchaseRawDispenseMsgVo purchaseRawDispenseMsgVo = new PurchaseRawDispenseMsgVo();
                        purchaseRawDispenseMsgVo.setSupplierInventoryRecordId(entry.getKey());
                        final List<PurchaseRawDispenseMsgItemVo> purchaseRawDispenseMsgItemVoList = this.getPurchaseRawDispenseMsgItemVoList(entry.getValue(), dto.getSku());

                        purchaseRawDispenseMsgVo.setPurchaseRawDispenseMsgItemList(purchaseRawDispenseMsgItemVoList);
                        return purchaseRawDispenseMsgVo;
                    }).collect(Collectors.toList());
        } else if (RawSupplier.OTHER_SUPPLIER.equals(dto.getRawSupplier()) || RawSupplier.HETE.equals(dto.getRawSupplier())) {
            final Map<String, List<PurchaseChildOrderRawDeliverPo>> rawDeliverNoPoListMap = purchaseChildOrderRawDeliverPoList.stream()
                    .collect(Collectors.groupingBy(PurchaseChildOrderRawDeliverPo::getPurchaseRawDeliverOrderNo));

            return rawDeliverNoPoListMap.entrySet().stream()
                    .map(entry -> {
                        final PurchaseRawDispenseMsgVo purchaseRawDispenseMsgVo = new PurchaseRawDispenseMsgVo();
                        purchaseRawDispenseMsgVo.setDeliveryOrderNo(entry.getKey());
                        final List<PurchaseRawDispenseMsgItemVo> purchaseRawDispenseMsgItemVoList = this.getPurchaseRawDispenseMsgItemVoList(entry.getValue(), dto.getSku());

                        purchaseRawDispenseMsgVo.setPurchaseRawDispenseMsgItemList(purchaseRawDispenseMsgItemVoList);
                        return purchaseRawDispenseMsgVo;
                    }).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private List<PurchaseRawDispenseMsgItemVo> getPurchaseRawDispenseMsgItemVoList
            (List<PurchaseChildOrderRawDeliverPo> purchaseChildOrderRawDeliverPoList, String sku) {

        return purchaseChildOrderRawDeliverPoList.stream()
                .filter(value -> value.getSku().equals(sku))
                .map(value -> {
                    // 根据原料来源，sku，采购子单号查询采购原料表，获取分配数
                    final PurchaseRawDispenseMsgItemVo purchaseRawDispenseMsgItemVo = new PurchaseRawDispenseMsgItemVo();
                    purchaseRawDispenseMsgItemVo.setSku(value.getSku());
                    purchaseRawDispenseMsgItemVo.setDispenseCnt(value.getDispenseCnt());
                    purchaseRawDispenseMsgItemVo.setPurchaseChildOrderNo(value.getPurchaseChildOrderNo());
                    return purchaseRawDispenseMsgItemVo;
                }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelRawReceiptOrder(DeliveryOrderCancelEventDto message) {
        PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = purchaseRawReceiptOrderDao.getOneByDeliverNo(message.getDeliveryOrderNo());
        if (null == purchaseRawReceiptOrderPo) {
            throw new BizException("出库单号：{}不存在对应采购收货单，取消失败！", message.getDeliveryOrderNo());
        }
        purchaseRawReceiptOrderPo.setReceiptOrderStatus(ReceiptOrderStatus.CANCEL);
        purchaseRawReceiptOrderDao.updateByIdVersion(purchaseRawReceiptOrderPo);

        // 日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_RAW_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo(), ReceiptOrderStatus.CANCEL.getRemark(), Collections.emptyList());
    }
}
