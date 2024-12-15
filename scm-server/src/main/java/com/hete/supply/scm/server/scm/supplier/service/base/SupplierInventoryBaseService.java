package com.hete.supply.scm.server.scm.supplier.service.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataGetRawInventoryBo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataGetCapacityDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemPo;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemRawPo;
import com.hete.supply.scm.server.scm.supplier.converter.SupplierInventoryConverter;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryRecordDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCodeAndSkuBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryChangeBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryResultBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventorySubBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventorySubItemDto;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/17 15:42
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierInventoryBaseService {
    private final SupplierInventoryDao supplierInventoryDao;
    private final SupplierInventoryRecordDao supplierInventoryRecordDao;
    private final PlmRemoteService plmRemoteService;


    public List<SupplierInventoryPo> getInventoryBySkuAndSupplier(List<String> skuList, String supplierCode) {
        return supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCode, skuList);
    }

    /**
     * 入参的自备库存和备货库存均为扣减值
     *
     * @param inventorySubItemList
     */
    public void subInventoryBySkuAndSupplier(List<InventorySubItemDto> inventorySubItemList) {

        if (CollectionUtils.isEmpty(inventorySubItemList)) {
            return;
        }
        supplierInventoryDao.subInventoryBySkuAndSupplier(inventorySubItemList);
    }

    public void insertBatchRecord(List<SupplierInventoryRecordPo> supplierInventoryRecordPoList) {
        if (CollectionUtils.isEmpty(supplierInventoryRecordPoList)) {
            return;
        }

        supplierInventoryRecordDao.insertBatch(supplierInventoryRecordPoList);
    }

    /**
     * 用于计算扣减的备货库存和自备库存使用
     *
     * @param bo
     * @return
     */
    public SupplierInventoryResultBo getSubResultByBo(SupplierInventorySubBo bo) {
        // 扣减备货库存的数值
        int stockUpDecrement = 0;
        // 扣减自备库存的数值
        int selfProvideDecrement = 0;
        Integer stockUpInventory = bo.getStockUpInventory();

        final Integer actualConsumeCnt = bo.getActualConsumeCnt();

        if (stockUpInventory >= actualConsumeCnt) {
            stockUpDecrement += actualConsumeCnt;
        } else {
            stockUpDecrement += stockUpInventory;
            int remaining = actualConsumeCnt - stockUpInventory;
            selfProvideDecrement += remaining;
        }

        final SupplierInventoryResultBo supplierInventoryResultBo = new SupplierInventoryResultBo();
        supplierInventoryResultBo.setStockUpDecrement(stockUpDecrement);
        supplierInventoryResultBo.setSelfProvideDecrement(selfProvideDecrement);

        return supplierInventoryResultBo;
    }

    public List<SupplierInventoryRecordPo> getRecordListByIdList(List<Long> recordIdList) {
        return supplierInventoryRecordDao.getListByIdList(recordIdList);
    }

    /**
     * 库存变更同时新增库存记录
     *
     * @param inventoryChangeItemList
     * @param supplierInventoryCtrlType
     * @param supplierWarehouse
     * @param supplierInventoryCtrlReason
     * @param relateNo
     */
    public void inventoryChange(List<InventoryChangeItemDto> inventoryChangeItemList,
                                SupplierInventoryCtrlType supplierInventoryCtrlType, SupplierWarehouse supplierWarehouse,
                                SupplierInventoryCtrlReason supplierInventoryCtrlReason, String relateNo) {
        if (CollectionUtils.isEmpty(inventoryChangeItemList)) {
            return;
        }

        // 查库存
        final List<SupplierCodeAndSkuBo> supplierCodeAndSkuBoList = SupplierInventoryConverter.convertChangeDtoToSupplierSkuBo(inventoryChangeItemList);
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCodeAndSkuBoList);
        if (inventoryChangeItemList.size() != supplierInventoryPoList.size()) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试");
        }


        // 更新库存
        handleInventoryChange(supplierWarehouse, inventoryChangeItemList);

        // 新增库存记录
        final List<SupplierInventoryRecordPo> supplierInventoryRecordPoList = SupplierInventoryConverter.convertInventoryDtoToRecordPo(inventoryChangeItemList,
                supplierInventoryPoList, supplierInventoryCtrlType, supplierWarehouse, supplierInventoryCtrlReason,
                SupplierInventoryRecordStatus.EFFECTIVE, relateNo);
        supplierInventoryRecordDao.insertBatch(supplierInventoryRecordPoList);
    }


    /**
     * 处理供应商仓库库存变更。
     *
     * @param supplierWarehouse       供应商仓库类型
     * @param inventoryChangeItemList 库存变更项列表
     */
    public void handleInventoryChange(SupplierWarehouse supplierWarehouse, List<InventoryChangeItemDto> inventoryChangeItemList) {
        if (SupplierWarehouse.SELF_PROVIDE.equals(supplierWarehouse)) {
            supplierInventoryDao.selfProvideInventoryChange(inventoryChangeItemList);
        } else if (SupplierWarehouse.STOCK_UP.equals(supplierWarehouse)) {
            supplierInventoryDao.stockUpInventoryChange(inventoryChangeItemList);
        } else if (SupplierWarehouse.DEFECTIVE_WAREHOUSE.equals(supplierWarehouse)) {
            supplierInventoryDao.defectiveInventoryChange(inventoryChangeItemList);
        }
    }

    @RedisLock(prefix = ScmRedisConstant.QC_CONFIG_DEFECT_UPDATE, key = "#supplierInventoryRecordId", waitTime = 1,
            leaseTime = -1)
    public void approveInventoryChangeRecord(Long supplierInventoryRecordId, InventoryApproveResult changeResult) {
        // 前置校验：存在记录 & 状态=待审核
        SupplierInventoryRecordPo supplierInventoryRecordPo
                = ParamValidUtils.requireNotNull(supplierInventoryRecordDao.getById(supplierInventoryRecordId),
                "未找到所选库存变更记录。请确保您选择的记录存在，并且未被删除或修改。您也可以尝试刷新页面或重新执行查询操作。");

        SupplierInventoryRecordStatus curStatus
                = supplierInventoryRecordPo.getSupplierInventoryRecordStatus();
        String supplierCode = supplierInventoryRecordPo.getSupplierCode();
        String sku = supplierInventoryRecordPo.getSku();
        ParamValidUtils.requireEquals(true,
                Objects.equals(SupplierInventoryRecordStatus.PENDING_APPROVAL, curStatus),
                StrUtil.format("无法审核所选记录。该记录的当前状态不是待审核。请刷新页面并重新选择待审核的记录进行审核。" +
                        "记录信息：供应商编码：{}，SKU：{}", supplierCode, sku));

        supplierInventoryRecordPo.setApproveUser(GlobalContext.getUserKey());
        supplierInventoryRecordPo.setApproveUsername(GlobalContext.getUsername());
        // 根据审核结果处理：库存变更记录状态 & 供应商库存
        /**
         * - 库存增加时（发起）--可用库存不变，增加冻结库存
         * - 审核通过时--可用库存增加，冻结库存减少
         * - 审核不通过时--可用库存不变，冻结库存减少
         * - 库存减少时（发起）--可用库存减少，冻结库存增加
         * - 审核通过时--可用库存不变，冻结库存减少
         * - 审核不通过时--可用库存增加，冻结库存减少
         */
        final SupplierInventoryChangeBo supplierInventoryChangeBo = new SupplierInventoryChangeBo();
        if (Objects.equals(InventoryApproveResult.APPROVED, changeResult)) {
            supplierInventoryRecordPo.setSupplierInventoryRecordStatus(SupplierInventoryRecordStatus.EFFECTIVE);
            supplierInventoryRecordPo.setEffectiveTime(LocalDateTime.now());
            if (SupplierWarehouse.STOCK_UP.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                    supplierInventoryChangeBo.setFrzStockUpInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setStockUpInventory(supplierInventoryRecordPo.getCtrlCnt());
                } else {
                    supplierInventoryChangeBo.setFrzStockUpInventory(supplierInventoryRecordPo.getCtrlCnt());
                }
            } else if (SupplierWarehouse.SELF_PROVIDE.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                    supplierInventoryChangeBo.setFrzSelfProvideInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setSelfProvideInventory(supplierInventoryRecordPo.getCtrlCnt());
                } else {
                    supplierInventoryChangeBo.setFrzSelfProvideInventory(supplierInventoryRecordPo.getCtrlCnt());
                }
            } else if (SupplierWarehouse.DEFECTIVE_WAREHOUSE.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                    supplierInventoryChangeBo.setFrzDefectiveInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setDefectiveInventory(supplierInventoryRecordPo.getCtrlCnt());
                } else {
                    supplierInventoryChangeBo.setFrzDefectiveInventory(supplierInventoryRecordPo.getCtrlCnt());
                }
            }
        } else {
            if (SupplierWarehouse.STOCK_UP.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                    supplierInventoryChangeBo.setFrzStockUpInventory(-supplierInventoryRecordPo.getCtrlCnt());
                } else {
                    supplierInventoryChangeBo.setStockUpInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setFrzStockUpInventory(supplierInventoryRecordPo.getCtrlCnt());
                }
            } else if (SupplierWarehouse.SELF_PROVIDE.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                    supplierInventoryChangeBo.setFrzSelfProvideInventory(-supplierInventoryRecordPo.getCtrlCnt());
                } else {
                    supplierInventoryChangeBo.setSelfProvideInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setFrzSelfProvideInventory(supplierInventoryRecordPo.getCtrlCnt());
                }
            } else if (SupplierWarehouse.DEFECTIVE_WAREHOUSE.equals(supplierInventoryRecordPo.getSupplierWarehouse())) {
                if (supplierInventoryRecordPo.getCtrlCnt() > 0) {
                    supplierInventoryChangeBo.setFrzDefectiveInventory(-supplierInventoryRecordPo.getCtrlCnt());
                } else {
                    supplierInventoryChangeBo.setDefectiveInventory(-supplierInventoryRecordPo.getCtrlCnt());
                    supplierInventoryChangeBo.setFrzDefectiveInventory(supplierInventoryRecordPo.getCtrlCnt());
                }
            }
            supplierInventoryRecordPo.setSupplierInventoryRecordStatus(SupplierInventoryRecordStatus.REJECTED);
        }
        supplierInventoryChangeBo.setSku(supplierInventoryRecordPo.getSku());
        supplierInventoryChangeBo.setSupplierCode(supplierInventoryRecordPo.getSupplierCode());
        this.inventoryChange(Collections.singletonList(supplierInventoryChangeBo));

        supplierInventoryRecordDao.updateByIdVersion(supplierInventoryRecordPo);
    }


    public void inventoryChange(@NotEmpty @Valid List<SupplierInventoryChangeBo> supplierInventoryChangeBoList) {
        supplierInventoryDao.inventoryChange(supplierInventoryChangeBoList);
    }

    /**
     * 获取供应商库存（备货库存+自备库存）
     *
     * @param produceDataGetRawInventoryBo:
     * @return Map<String, Integer>
     * @author ChenWenLong
     * @date 2024/8/12 14:53
     */
    public Map<String, Integer> getSupplierInventoryMap(List<ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto> dtoList,
                                                        ProduceDataGetRawInventoryBo produceDataGetRawInventoryBo) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return new HashMap<>();
        }
        Map<String, List<ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto>> dtoMap = dtoList.stream()
                .collect(Collectors.groupingBy(ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto::getSku));

        // BOM的信息map
        Map<Long, ProduceDataItemPo> produceDataItemPoMap = Optional.ofNullable(produceDataGetRawInventoryBo.getProduceDataItemPoList())
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(ProduceDataItemPo::getProduceDataItemId, Function.identity()));

        // 获取供应商库存信息Bo
        List<SupplierCodeAndSkuBo> supplierCodeAndSkuBoList = new ArrayList<>();
        // 用于是否已经添加的SupplierCode + Sku组合查询条件
        Set<String> supplierCodeAndSkuSet = new HashSet<>();

        // 组装查询BO,通过生产资料BOM对应原料sku+入参的供应商组合查询对应供应商库存信息
        List<ProduceDataItemRawPo> produceDataItemRawPos = Optional.ofNullable(produceDataGetRawInventoryBo.getProduceDataItemRawPoList())
                .orElse(new ArrayList<>());
        for (ProduceDataItemRawPo produceDataItemRawPo : produceDataItemRawPos) {
            // 获取原料对应BOM信息
            ProduceDataItemPo produceDataItemPo = produceDataItemPoMap.get(produceDataItemRawPo.getProduceDataItemId());
            if (null == produceDataItemPo) {
                continue;
            }

            // 通过BOM的sku获取对应Dto的供应商列表
            List<ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto> produceDataGetCapacityItemDtoList = dtoMap.get(produceDataItemPo.getSku());
            if (CollectionUtils.isEmpty(produceDataGetCapacityItemDtoList)) {
                continue;
            }
            for (ProduceDataGetCapacityDto.ProduceDataGetCapacityItemDto itemDto : produceDataGetCapacityItemDtoList) {
                // 组合 key，形式是 SupplierCode + Sku
                String combinationKey = itemDto.getSupplierCode() + produceDataItemRawPo.getSku();
                // 判断这个组合是否已经存在，如果不存在则加入列表
                if (!supplierCodeAndSkuSet.contains(combinationKey)) {
                    supplierCodeAndSkuSet.add(combinationKey);
                    final SupplierCodeAndSkuBo supplierCodeAndSkuBo = new SupplierCodeAndSkuBo();
                    supplierCodeAndSkuBo.setSupplierCode(itemDto.getSupplierCode());
                    supplierCodeAndSkuBo.setSku(produceDataItemRawPo.getSku());
                    supplierCodeAndSkuBoList.add(supplierCodeAndSkuBo);
                }
            }
        }
        final List<SupplierInventoryPo> supplierInventoryPoList = supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCodeAndSkuBoList);
        // 原料SKU备货库存+自备库存
        return supplierInventoryPoList.stream()
                .collect(Collectors.toMap(po -> po.getSupplierCode() + po.getSku(),
                        supplierInventoryPo -> supplierInventoryPo.getStockUpInventory() + supplierInventoryPo.getSelfProvideInventory()));
    }
}













