package com.hete.supply.scm.server.supplier.supplier.service.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlReason;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryCtrlType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInventoryRecordStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.api.scm.importation.entity.dto.SupplierInventoryImportationDto;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.supplier.builder.SupplierInventoryBuilder;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierInventoryRecordDao;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierInventoryChangeBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryRecordPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierInventoryBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.scm.server.supplier.supplier.entity.dto.InventoryChangeItemDto;
import com.hete.support.api.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/22 21:48
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SupplierInventoryImportService {
    private final SupplierInventoryDao supplierInventoryDao;
    private final SupplierInventoryRecordDao supplierInventoryRecordDao;
    private final AuthBaseService authBaseService;
    private final SupplierInventoryBaseService supplierInventoryBaseService;

    @Transactional(rollbackFor = Exception.class)
    public void importSupplierInventory(SupplierInventoryImportationDto dto) {
        // 校验：供应商编码是否有权限
        String supplierCode
                = ParamValidUtils.requireNotBlank(dto.getSupplierCode(),
                "供应商编码不能为空，请确保在导入操作中提供供应商编码。");
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("获取不到供应商权限，请重新配置后重试！");
        }
        ParamValidUtils.requireContains(supplierCode, supplierCodeList,
                StrUtil.format("当前导入用户的供应商权限为{},不具备供应商:{}的库存修改权限",
                        supplierCodeList.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")), supplierCode));

        String sku
                = ParamValidUtils.requireNotBlank(dto.getSku(),
                "库存变更的SKU不能为空，请确保在导入操作中提供SKU。");

        // 校验：仓库编码必填 & 必须是备货仓/自备仓/不良仓
        String supplierWarehouse
                = ParamValidUtils.requireNotBlank(dto.getSupplierWarehouse(),
                "操作库存对应的仓库信息未填写，请确保在导入操作中提供仓库。");
        SupplierWarehouse supplierWarehouseEnum
                = ParamValidUtils.requireNotNull(SupplierWarehouse.getByName(supplierWarehouse),
                "仓库信息填写错误：请填写正确的仓库信息，仅允许填写备货仓/自备仓/不良仓。");

        List<SupplierWarehouse> canImportSupplierWarehouseEnums = Arrays.asList(SupplierWarehouse.STOCK_UP,
                SupplierWarehouse.SELF_PROVIDE,
                SupplierWarehouse.DEFECTIVE_WAREHOUSE);
        ParamValidUtils.requireContains(supplierWarehouseEnum, canImportSupplierWarehouseEnums,
                "仓库信息填写错误：请填写正确的仓库信息，仅允许填写备货仓/自备仓/不良仓。");

        // 校验：操作数量必填，不等于0 不能超过最大值，不能小于最小值
        Integer curChangeCnt
                = ParamValidUtils.requireNotNull(dto.getCtrlCnt(),
                "操作库存数量不能为空，请确保在导入操作中提供库存变更数量。");
        ParamValidUtils.requireNotEquals(curChangeCnt, 0, "操作数量不能为0，请输入一个有效的库存变更数量。");
        ParamValidUtils.requireLessThanOrEqual(curChangeCnt, Integer.MAX_VALUE,
                "操作数量超过了最大值，请输入一个有效的库存变更数量。");
        ParamValidUtils.requireGreaterThan(curChangeCnt, Integer.MIN_VALUE,
                "操作数量小于了最小值，请输入一个有效的库存变更数量。");

        final SupplierInventoryPo supplierInventoryPo
                = ParamValidUtils.requireNotNull(supplierInventoryDao.getInventoryBySkuAndSupplier(supplierCode, sku),
                StrUtil.format("获取不到供应商:{}，对应的sku:{}库存数据，请维护后再进行操作",
                        supplierCode, sku));

        // 定义不需要审核的供应商仓库类型列表
        List<SupplierWarehouse> notNeedApproveSupplierWarehouses
                = List.of(SupplierWarehouse.SELF_PROVIDE, SupplierWarehouse.DEFECTIVE_WAREHOUSE);

        // 如果供应商仓库类型在不需要审核的列表中
        if (notNeedApproveSupplierWarehouses.contains(supplierWarehouseEnum)) {
            // 构建操作库存变更DTO并更新供应商库存
            String inventorySupplierCode = supplierInventoryPo.getSupplierCode();
            String inventorySku = supplierInventoryPo.getSku();
            InventoryChangeItemDto updateSupplierInventoryDto
                    = SupplierInventoryBuilder.buildInventoryChangeItemDto(inventorySupplierCode, inventorySku,
                    supplierWarehouseEnum, curChangeCnt);
            supplierInventoryBaseService.handleInventoryChange(supplierWarehouseEnum, List.of(updateSupplierInventoryDto));

            // 保存已生效的库存变更记录
            SupplierInventoryRecordPo supplierInventoryRecordPo
                    = SupplierInventoryBuilder.buildSupplierInventoryRecordPo(supplierInventoryPo,
                    supplierWarehouseEnum,
                    SupplierInventoryCtrlType.CHECK,
                    curChangeCnt,
                    SupplierInventoryCtrlReason.SUPPLIER_CTRL,
                    SupplierInventoryRecordStatus.EFFECTIVE);
            supplierInventoryRecordDao.insert(supplierInventoryRecordPo);
        }

        // 定义需要审核的供应商仓库类型列表
        List<SupplierWarehouse> needApproveSupplierWarehouses
                = List.of(SupplierWarehouse.STOCK_UP);

        // 如果供应商仓库类型在需要审核的列表中
        if (needApproveSupplierWarehouses.contains(supplierWarehouseEnum)) {
            // 保存待审核的库存变更记录
            SupplierInventoryRecordPo supplierInventoryRecordPo
                    = SupplierInventoryBuilder.buildSupplierInventoryRecordPo(supplierInventoryPo,
                    supplierWarehouseEnum,
                    SupplierInventoryCtrlType.CHECK,
                    curChangeCnt,
                    SupplierInventoryCtrlReason.SUPPLIER_CTRL,
                    SupplierInventoryRecordStatus.PENDING_APPROVAL);
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
            supplierInventoryBaseService.inventoryChange(Collections.singletonList(supplierInventoryChangeBo));
        }
    }
}
