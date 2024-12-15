package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.scm.api.scm.entity.enums.MissingInformation;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.builder.InventoryBuilder;
import com.hete.supply.scm.server.scm.enums.CostCalculationStrategyType;
import com.hete.supply.scm.server.scm.process.builder.DeliveryOrderBuilder;
import com.hete.supply.scm.server.scm.process.converter.MissInformationConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.InventoryMatchingValidationResultBo;
import com.hete.supply.scm.server.scm.process.entity.bo.RepairOrderBindingInfoBo;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculationStrategy;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculatorStrategyFactory;
import com.hete.supply.wms.api.interna.entity.dto.InventoryForPlmDto;
import com.hete.supply.wms.api.interna.entity.vo.InventoryForPlmVo;
import com.hete.supply.wms.api.leave.entity.dto.DeliveryOrderCreateDto;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/2.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepairOrderBaseService {

    private final RepairOrderDao repairOrderDao;
    private final WmsRemoteService wmsRemoteService;
    private final RepairOrderResultDao repairOrderResultDao;
    private final Environment environment;
    private final RepairOrderItemDao repairOrderItemDao;
    private final ProcessMaterialBackDao materialBackDao;
    private final ProcessMaterialReceiptDao materialReceiptDao;
    private final ProcessMaterialBackItemDao materialBackItemDao;
    private final ProcessOrderMaterialDao processOrderMaterialDao;
    private final ProcessMaterialReceiptItemDao materialReceiptItemDao;
    private final CostCalculatorStrategyFactory costCalculatorStrategyFactory;


    /**
     * 将给定的订单项列表按照每个批次的最大数量进行分割和分组。
     *
     * @param items     订单项列表，每个订单项是一个包含商品名和数量的Map
     * @param batchSize 每个批次的最大数量
     * @return 分割后的订单批次，以Map形式表示，键为分隔条数号，值为该批次的订单项列表
     */
    public Map<Integer, List<Map<String, Integer>>> splitAndGroupByBatchSize(List<Map<String, Integer>> items,
                                                                             int batchSize) {
        // 检查订单项列表是否为空
        if (CollectionUtil.isEmpty(items)) {
            return new HashMap<>(0);
        }

        // 定义Map排序比较器，按照订单项中的数量降序排序
        Comparator<Map<String, Integer>> mapComparator = Comparator.comparing(map -> map.values()
                .iterator()
                .next(), Comparator.reverseOrder());

        // 使用优先队列按照排序比较器对订单项进行排序
        PriorityQueue<Map<String, Integer>> priorityQueue = new PriorityQueue<>(mapComparator);
        priorityQueue.addAll(items);

        // 存储分割后的订单批次，键为批次号，值为订单项列表
        Map<Integer, List<Map<String, Integer>>> result = new HashMap<>(16);

        // 用于生成批次号的计数器
        AtomicInteger no = new AtomicInteger(1);

        // 当前批次的订单项总数量
        int current = 0;

        // 临时存储当前批次的订单项列表
        List<Map<String, Integer>> itemCaches = Lists.newArrayList();

        // 遍历优先队列，进行订单项的分割
        while (!priorityQueue.isEmpty()) {
            // 弹出数量最多的订单项
            Map<String, Integer> item = priorityQueue.poll();

            // 遍历订单项，按照数量进行分割
            for (Map.Entry<String, Integer> entry : item.entrySet()) {
                String key = entry.getKey();
                Integer quantity = entry.getValue();

                // 累加当前批次的订单项总数量
                current += quantity;

                // 判断当前批次的订单项总数量是否超过批次大小
                if (current >= batchSize) {
                    // 计算归还数量 & 将归还数量重新加入优先队列
                    int backNum = current - batchSize;
                    if (backNum > 0) {
                        priorityQueue.offer(Map.of(key, backNum));
                    }

                    // 计算取用数量 & 将取用数量的订单项加入临时列表
                    int useNum = quantity - backNum;
                    itemCaches.add(Map.of(key, useNum));

                    // 将当前批次的订单项列表加入结果Map，并更新批次号、当前批次总数量和临时列表
                    result.put(no.getAndIncrement(), deepCopyList(itemCaches));
                    current = 0;
                    itemCaches.clear();
                } else {
                    // 将订单项加入当前批次的临时列表
                    itemCaches.add(Map.of(key, quantity));
                }
            }
        }

        if (!itemCaches.isEmpty()) {
            result.put(no.getAndIncrement(), deepCopyList(itemCaches));
        }

        // 返回最终的分割结果
        return result;
    }


    /**
     * 执行返修单库存匹配操作。
     *
     * @param repairOrderNo 返修单号
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PLAN_REFRESH_STOCK_PREFIX, key = "#planNo", waitTime = 5, leaseTime = -1)
    public void doRepairOrderStockMatching(String planNo,
                                           String repairOrderNo) {
        // 校验库存匹配参数的有效性
        InventoryMatchingValidationResultBo inventoryMatchingValidationResultBo = validateInventoryMatchingParams(repairOrderNo);
        if (!inventoryMatchingValidationResultBo.isValid()) {
            return;
        }

        // 获取返修单信息和原料信息列表
        RepairOrderPo repairOrderPo = inventoryMatchingValidationResultBo.getRepairOrderPo();
        List<ProcessOrderMaterialPo> processOrderMaterialPos = inventoryMatchingValidationResultBo.getProcessOrderMaterialPos();

        // 校验原料库存是否足够
        String platform = ParamValidUtils.requireNotBlank(repairOrderPo.getPlatform(), "校验原料库存失败！平台编码为空");
        boolean allInventorySufficient = validateMaterialInventory(platform, processOrderMaterialPos);

        if (allInventorySufficient) {
            // 如果库存足够，创建返修单原料出库单
            DeliveryOrderCreateDto createDeliveryParam = DeliveryOrderBuilder.buildDeliveryOrderCreateDto(repairOrderNo, platform, processOrderMaterialPos);
            String deliveryNo = wmsRemoteService.createDeliveryOrder(createDeliveryParam);

            // 更新原料出库单号
            processOrderMaterialPos.forEach(updateDeliveryPo -> updateDeliveryPo.setDeliveryNo(deliveryNo));
            processOrderMaterialDao.updateBatchByIdVersion(processOrderMaterialPos);

            // 更新返修信息
            repairOrderPo.setMissingInformation("");
            repairOrderPo.setRepairOrderStatus(RepairOrderStatus.WAITING_FOR_PRODUCTION);
        } else {
            // 如果库存不足，更新缺失信息为库存不足
            String existMissingInformation = repairOrderPo.getMissingInformation();
            Set<MissingInformation> existMissingInformationList = MissInformationConverter.convertToMissingInformationEnums(existMissingInformation);
            existMissingInformationList.add(MissingInformation.OUT_OF_STOCK);
            repairOrderPo.setMissingInformation(MissInformationConverter.convertToMissInformation(existMissingInformationList));
        }
        repairOrderDao.updateByIdVersion(repairOrderPo);
    }


    /**
     * 校验原料库存是否足够。
     *
     * @param processOrderMaterialPos 原料信息列表
     * @return true 如果所有原料的可用库存足够，否则 false
     */
    public boolean validateMaterialInventory(String platform, List<ProcessOrderMaterialPo> processOrderMaterialPos) {
        // 如果原料信息列表为空，直接返回 false
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            return false;
        }

        // 构建 InventoryQueryInfo 列表 & 调用远程服务获取库存信息
        InventoryForPlmDto queryParam = new InventoryForPlmDto();
        List<InventoryForPlmDto.InventoryQueryInfo> inventoryQueryParam
                = InventoryBuilder.buildInventoryQueryInfoList(processOrderMaterialPos);
        queryParam.setInventoryQueryInfoList(inventoryQueryParam);
        queryParam.setPlatCode(platform);
        List<InventoryForPlmVo.InventoryInfo> inventoryResults = wmsRemoteService.getInventoryInfoList(queryParam);

        // 如果库存信息列表为空，直接返回 false
        if (CollectionUtils.isEmpty(inventoryResults)) {
            return false;
        }

        // 校验所有原料的可用库存是否足够
        for (ProcessOrderMaterialPo material : processOrderMaterialPos) {
            String sku = material.getSku();
            String warehouseCode = material.getWarehouseCode();
            int deliveryNum = material.getDeliveryNum();
            String shelfCode = material.getShelfCode();

            InventoryForPlmVo.InventoryInfo matchInventoryResult = inventoryResults.stream()
                    .filter(inventoryResult -> Objects.equals(warehouseCode,
                            inventoryResult.getWarehouseCode()) && Objects.equals(sku,
                            inventoryResult.getSkuCode()) && (StrUtil.isBlank(
                            shelfCode) || Objects.equals(shelfCode, inventoryResult.getLocationCode())))
                    .findFirst()
                    .orElse(null);

            // 如果找不到匹配的库存信息，或者可用库存不足，返回 false
            if (Objects.isNull(matchInventoryResult) || matchInventoryResult.getAvailAmount() < deliveryNum) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验库存匹配参数的有效性，并返回校验结果对象。
     *
     * @param repairOrderNo 返修单号
     * @return 校验结果对象，包含返修单信息和原料信息列表
     */
    private InventoryMatchingValidationResultBo validateInventoryMatchingParams(String repairOrderNo) {
        InventoryMatchingValidationResultBo validationResult = new InventoryMatchingValidationResultBo();

        RepairOrderPo repairOrderPo = repairOrderDao.getByRepairOrderNo(repairOrderNo);
        if (Objects.isNull(repairOrderPo)) {
            log.warn("校验库存失败，返修单信息不存在。");
            validationResult.setValid(false);
            return validationResult;
        }

        RepairOrderStatus status = repairOrderPo.getRepairOrderStatus();
        if (!RepairOrderStatus.WAITING_FOR_READY.equals(status)) {
            log.warn("校验库存失败，状态非{}！返修单号:{}", RepairOrderStatus.WAITING_FOR_READY.getRemark(),
                    repairOrderNo);
            validationResult.setValid(false);
            return validationResult;
        }

        // 库存校验
        List<ProcessOrderMaterialPo> processOrderMaterialPos = processOrderMaterialDao.listByRepairOrderNo(
                repairOrderNo);
        if (CollectionUtils.isEmpty(processOrderMaterialPos)) {
            log.warn("校验库存失败，无原料信息！返修单号:{}", repairOrderNo);
            validationResult.setValid(false);
            return validationResult;
        }

        validationResult.setRepairOrderPo(repairOrderPo);
        validationResult.setProcessOrderMaterialPos(processOrderMaterialPos);
        validationResult.setValid(true);
        return validationResult;
    }

    private static List<Map<String, Integer>> deepCopyList(List<Map<String, Integer>> originalList) {
        List<Map<String, Integer>> copiedList = new ArrayList<>();

        for (Map<String, Integer> originalMap : originalList) {
            // 遍历原始列表中的每个 Map，创建新的 Map
            Map<String, Integer> copiedMap = new HashMap<>(originalMap);
            copiedList.add(copiedMap);
        }

        return copiedList;
    }

    /**
     * 获取返修单绑定信息列表
     *
     * @param repairOrderNo 返修单号
     * @return 包含返修单绑定信息的列表，若返修单号为空，则返回空列表
     */
    public List<RepairOrderBindingInfoBo> getRepairOrderBindingInfo(String repairOrderNo) {
        // 判断返修单号是否为空
        if (StrUtil.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }

        RepairOrderPo repairOrderPo = ParamValidUtils.requireNotNull(repairOrderDao.getByRepairOrderNo(repairOrderNo),
                StrUtil.format(
                        "获取返修单 {} 原料绑定信息失败，返修单信息不存在，请刷新页面后重试",
                        repairOrderNo));
        RepairOrderStatus repairOrderStatus = repairOrderPo.getRepairOrderStatus();
        boolean isInspectionCompleted = Arrays.asList(RepairOrderStatus.WAITING_FOR_RECEIVING,
                        RepairOrderStatus.COMPLETED)
                .contains(repairOrderStatus);

        // 查询原料收货信息
        List<ProcessMaterialReceiptPo> processMaterialReceiptPos = materialReceiptDao.listByRepairOrderNo(
                repairOrderNo);
        List<Long> processMaterialReceiptIds = processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> materialReceiptItemPos = materialReceiptItemDao.getByMaterialReceiptIds(
                processMaterialReceiptIds);

        // 查询原料归还信息
        List<ProcessMaterialBackPo> processMaterialBackPos = materialBackDao.listByRepairOrderNo(repairOrderNo);
        List<Long> processMaterialBackIds = processMaterialBackPos.stream()
                .map(ProcessMaterialBackPo::getProcessMaterialBackId)
                .collect(Collectors.toList());
        List<ProcessMaterialBackItemPo> materialBackItemPos = materialBackItemDao.listByProcessMaterialBackIds(
                processMaterialBackIds);

        // 查询返修结果信息
        List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultDao.listByRepairOrderNo(repairOrderNo);

        // 构建返修单绑定信息列表
        return materialReceiptItemPos.stream()
                .map(materialReceiptItemPo -> {
                    String skuBatchCode = materialReceiptItemPo.getSkuBatchCode();

                    RepairOrderBindingInfoBo repairOrderBindingInfoBo = new RepairOrderBindingInfoBo();
                    repairOrderBindingInfoBo.setBatchCode(skuBatchCode);
                    repairOrderBindingInfoBo.setSku(materialReceiptItemPo.getSku());
                    repairOrderBindingInfoBo.setOutboundQuantity(materialReceiptItemPo.getDeliveryNum());
                    repairOrderBindingInfoBo.setReceivedQuantity(materialReceiptItemPo.getReceiptNum());

                    // 计算加工绑定数量
                    Integer materialUsageQuantity = repairOrderResultPos.stream()
                            .filter(repairOrderResultPo -> Objects.equals(skuBatchCode,
                                    repairOrderResultPo.getMaterialBatchCode()))
                            .map(repairOrderResultPo -> isInspectionCompleted ?
                                    repairOrderResultPo.getQcPassQuantity() :
                                    repairOrderResultPo.getMaterialUsageQuantity())
                            .reduce(0, Integer::sum);
                    repairOrderBindingInfoBo.setMaterialUsageQuantity(materialUsageQuantity);

                    // 计算已归还数量
                    Integer returnedQuantity = materialBackItemPos.stream()
                            .filter(materialBackItemPo -> Objects.equals(skuBatchCode,
                                    materialBackItemPo.getSkuBatchCode()))
                            .map(ProcessMaterialBackItemPo::getDeliveryNum)
                            .reduce(0, Integer::sum);
                    repairOrderBindingInfoBo.setReturnedQuantity(returnedQuantity);

                    return repairOrderBindingInfoBo;
                })
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void calCost(String repairOrderNo) {
        CostCalculationStrategy costCalculationStrategy
                = costCalculatorStrategyFactory.getCostCalculationStrategy(CostCalculationStrategyType.REPAIR_ORDER);
        costCalculationStrategy.calculateBatchCodeCost(repairOrderNo);
    }
}
