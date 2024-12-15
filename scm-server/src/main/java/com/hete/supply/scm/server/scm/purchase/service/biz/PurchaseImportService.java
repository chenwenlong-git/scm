package com.hete.supply.scm.server.scm.purchase.service.biz;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildConfirmImportationDto;
import com.hete.supply.scm.api.scm.importation.entity.dto.PurchaseChildPlanConfirmImportationDto;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmWarehouseUtil;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.dao.SkuInfoDao;
import com.hete.supply.scm.server.scm.entity.po.SkuInfoPo;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderChangeDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseParentOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseSkuSupplierDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseSkuSupplierItemDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderChangePo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseSkuSupplierItemVo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.WarehouseBaseService;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupOpCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierUrgentStatus;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.scm.supplier.service.ref.SupplierCapacityRefService;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/10/23 09:23
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Validated
public class PurchaseImportService {
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final SupplierBaseService supplierBaseService;
    private final LogBaseService logBaseService;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final PurchaseBaseService purchaseBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final SkuInfoDao skuInfoDao;
    private final SupplierCapacityRefService supplierCapacityRefService;

    @Transactional(rollbackFor = Exception.class)
    public void importPlanConfirmPurchaseChild(PurchaseChildPlanConfirmImportationDto dto) {
        if (StringUtils.isBlank(dto.getPurchaseChildOrderNo())) {
            throw new ParamIllegalException("采购子单号不能为空，导入修改失败");
        }
        if (StringUtils.isBlank(dto.getSupplierCode())) {
            throw new ParamIllegalException("供应商代码不能为空，导入修改失败");
        }

        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        if (null == purchaseChildOrderPo) {
            throw new ParamIllegalException("查找不到对应的采购子单:{}", dto.getPurchaseChildOrderNo());
        }
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        if (null == purchaseChildOrderItemPo) {
            throw new ParamIllegalException("查找不到对应的采购子单:{}", dto.getPurchaseChildOrderNo());
        }
        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChildOrderChangeDao.getByChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        if (null == purchaseChildOrderChangePo) {
            throw new ParamIllegalException("查找不到对应的采购子单:{}", dto.getPurchaseChildOrderNo());
        }

        final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(dto.getSupplierCode());
        if (null == supplierPo) {
            throw new ParamIllegalException("查找不到对应的供应商:{}", dto.getSupplierCode());
        }
        if (!PurchaseOrderStatus.WAIT_CONFIRM.equals(purchaseChildOrderPo.getPurchaseOrderStatus())) {
            throw new ParamIllegalException("创建失败！采购子单状态不是待确认状态");
        }
        // 更新产能
        BigDecimal singleCapacity;
        final SkuInfoPo skuInfoPo = skuInfoDao.getBySku(purchaseChildOrderItemPo.getSku());
        if (null == skuInfoPo) {
            singleCapacity = BigDecimal.ZERO;
            log.info("sku:{}没有配置sku_info信息", purchaseChildOrderItemPo.getSku());
        } else {
            singleCapacity = skuInfoPo.getSingleCapacity();
        }

        final BigDecimal capacity = singleCapacity.multiply(new BigDecimal(purchaseChildOrderItemPo.getPurchaseCnt()))
                .setScale(2, RoundingMode.HALF_UP);
        // 新供应商扣减产能
        final List<SupOpCapacityBo> supOpCapacityList = new ArrayList<>();
        final LocalDate capacityDate = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), dto.getSupplierCode());
        final SupOpCapacityBo supOpCapacityBo = new SupOpCapacityBo();
        supOpCapacityBo.setSupplierCode(dto.getSupplierCode());
        supOpCapacityBo.setOperateDate(capacityDate);
        supOpCapacityBo.setOperateValue(capacity.negate());
        supOpCapacityBo.setBizNo(dto.getPurchaseChildOrderNo());
        supOpCapacityList.add(supOpCapacityBo);
        if (StringUtils.isNotBlank(purchaseChildOrderPo.getSupplierCode())) {
            // 旧供应商增加产能
            final LocalDate capacityDate1 = purchaseBaseService.getCapacityDate(purchaseChildOrderPo.getPromiseDate(), purchaseChildOrderPo.getSupplierCode());
            final SupOpCapacityBo supOpCapacityBo1 = new SupOpCapacityBo();
            supOpCapacityBo1.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
            supOpCapacityBo1.setOperateDate(capacityDate1);
            supOpCapacityBo1.setOperateValue(purchaseChildOrderPo.getCapacity());
            supOpCapacityBo1.setBizNo(dto.getPurchaseChildOrderNo());
            supOpCapacityList.add(supOpCapacityBo1);
        }
        supplierCapacityRefService.operateSupplierCapacityBatch(supOpCapacityList);

        final PurchaseSkuSupplierItemDto purchaseSkuSupplierItemDto = new PurchaseSkuSupplierItemDto();
        purchaseSkuSupplierItemDto.setSupplierCode(supplierPo.getSupplierCode());
        purchaseSkuSupplierItemDto.setSku(purchaseChildOrderItemPo.getSku());
        purchaseSkuSupplierItemDto.setExpectedOnShelvesDate(purchaseChildOrderPo.getExpectedOnShelvesDate());
        purchaseSkuSupplierItemDto.setCreateTime(LocalDateTime.now());
        purchaseSkuSupplierItemDto.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        final PurchaseSkuSupplierDto purchaseSkuSupplierDto = new PurchaseSkuSupplierDto();
        purchaseSkuSupplierDto.setPurchaseSkuSupplierItemList(Collections.singletonList(purchaseSkuSupplierItemDto));
        final List<PurchaseSkuSupplierItemVo> supplierDateDetail = purchaseBaseService.getSupplierDateDetail(purchaseSkuSupplierDto);
        if (CollectionUtils.isEmpty(supplierDateDetail)) {
            throw new ParamIllegalException("该供应商:{}未维护物流时效以及生产周期", dto.getSupplierCode());
        }
        final PurchaseSkuSupplierItemVo purchaseSkuSupplierItemVo = supplierDateDetail.get(0);
        final SupplierUrgentStatus supplierUrgentStatus = purchaseSkuSupplierItemVo.getSupplierUrgentStatus();
        if (SupplierUrgentStatus.CYCLE.equals(supplierUrgentStatus)
                || SupplierUrgentStatus.LOGISTICS_AGING.equals(supplierUrgentStatus)
                || SupplierUrgentStatus.LOGISTICS_AGING_CYCLE.equals(supplierUrgentStatus)) {
            throw new ParamIllegalException("该供应商:{},{}", dto.getSupplierCode(), supplierUrgentStatus.getRemark());
        }

        purchaseChildOrderPo.setSupplierCode(supplierPo.getSupplierCode());
        purchaseChildOrderPo.setSupplierName(supplierPo.getSupplierName());
        if (StringUtils.isNotBlank(dto.getOrderRemarks())) {
            purchaseChildOrderPo.setOrderRemarks(dto.getOrderRemarks());
        }
        purchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM);
        if (SupplierUrgentStatus.URGENT.equals(supplierUrgentStatus)) {
            purchaseChildOrderPo.setIsUrgentOrder(BooleanType.TRUE);
        } else if (SupplierUrgentStatus.NOT_URGENT.equals(supplierUrgentStatus)) {
            purchaseChildOrderPo.setIsUrgentOrder(BooleanType.FALSE);
        }
        purchaseChildOrderPo.setDeliverDate(purchaseSkuSupplierItemVo.getDeliverDate());

        purchaseChildOrderPo.setCapacity(capacity);
        purchaseChildOrderDao.updateByIdVersion(purchaseChildOrderPo);

        // 产能变更日志
        logBaseService.purchaseCapacityLog(LogBizModule.PURCHASE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), purchaseChildOrderPo, capacity);

        // 赋值计划人、计划时间
        purchaseChildOrderChangePo.setPlanConfirmUser(GlobalContext.getUserKey());
        purchaseChildOrderChangePo.setPlanConfirmUsername(GlobalContext.getUsername());
        purchaseChildOrderChangePo.setPlanConfirmTime(LocalDateTime.now());

        purchaseChildOrderChangeDao.updateByIdVersion(purchaseChildOrderChangePo);

        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM.getRemark(), Collections.emptyList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void importPurchaseConfirmPurchaseChild(@Valid @NotNull PurchaseChildConfirmImportationDto dto) {
        log.info("采购导入批量跟单确认入参:{}", JSON.toJSONString(dto));
        if (StringUtils.isBlank(dto.getPurchaseChildOrderNo())) {
            throw new ParamIllegalException("采购子单单号不能为空");
        }
        if (StringUtils.isBlank(dto.getWarehouseCode())) {
            throw new ParamIllegalException("收货仓库编码不能为空");
        }
        if (StringUtils.isBlank(dto.getWarehouseCode())) {
            throw new ParamIllegalException("收货仓库编码不能为空");
        }
        if (null == dto.getPurchasePrice()) {
            throw new ParamIllegalException("采购价不能为空");
        }
        if (dto.getPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ParamIllegalException("采购价不能小于0，请重新填写！");
        }

        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPurchaseChildOrderNo());
        Assert.notNull(purchaseChildOrderPo, () -> new ParamIllegalException("查找不到对应的采购子单:{}", dto.getPurchaseChildOrderNo()));
        if (!PurchaseOrderStatus.WAIT_FOLLOWER_CONFIRM.equals(purchaseChildOrderPo.getPurchaseOrderStatus())) {
            throw new ParamIllegalException("导入失败，采购子单:{}状态不为跟单待确认，请确认后再导入！", dto.getPurchaseChildOrderNo());
        }
        final SkuType skuType = purchaseChildOrderPo.getSkuType();
        if (!SkuType.SM_SKU.equals(skuType)) {
            throw new ParamIllegalException("仅{}类型支持批量确认", SkuType.SM_SKU.getRemark());
        }


        PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderDao.getOneByNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        Assert.notNull(purchaseParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNo(dto.getPurchaseChildOrderNo());
        Assert.notEmpty(purchaseChildOrderItemPoList, () -> new BizException("查找不到对应的采购子单，拆分加工采购子单失败！"));

        // 网红类型判断是否网红仓
        if (PurchaseDemandType.WH.equals(purchaseParentOrderPo.getPurchaseDemandType()) && !ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为网红时，只能选择网红仓创建，请重新选择仓库后再提交！");
        }
        if (PurchaseDemandType.NORMAL.equals(purchaseParentOrderPo.getPurchaseDemandType()) && ScmWarehouseUtil.WH_WAREHOUSE_CODE.equals(dto.getWarehouseCode())) {
            throw new ParamIllegalException("采购需求类型为常规时，不能选择网红仓创建，请重新选择仓库后再提交！");
        }
        List<String> warehouseCodeList = new ArrayList<>();
        warehouseCodeList.add(dto.getWarehouseCode());
        final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(warehouseCodeList);
        final Map<String, WarehouseVo> warehouseVoMap = warehouseVoList.stream()
                .collect(Collectors.toMap(WarehouseVo::getWarehouseCode, Function.identity()));

        final WarehouseVo warehouseVo = warehouseVoMap.get(dto.getWarehouseCode());
        if (null == warehouseVo) {
            throw new ParamIllegalException("收货仓库：{}不存在", dto.getWarehouseCode());
        }

        purchaseChildOrderPo.setWarehouseCode(warehouseVo.getWarehouseCode());
        purchaseChildOrderPo.setWarehouseName(warehouseVo.getWarehouseName());
        purchaseChildOrderPo.setWarehouseTypes(warehouseVo.getWarehouseTypeName());
        purchaseChildOrderPo.setIsDirectSend(WarehouseBaseService.getIsDirectSendByWarehouseType(List.of(warehouseVo.getWarehouseTypeName())));
        purchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.PRODUCT);
        purchaseChildOrderPo.setShippableCnt(purchaseChildOrderPo.getPurchaseTotal());


        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPoList.get(0);
        purchaseChildOrderItemPo.setPurchasePrice(dto.getPurchasePrice());
        purchaseChildOrderItemPo.setSettlePrice(dto.getPurchasePrice());

        purchaseChildOrderItemDao.updateBatchByIdVersion(purchaseChildOrderItemPoList);
        purchaseBaseService.updateStatusAfterCreateChildOrder(Collections.singletonList(purchaseChildOrderPo),
                Collections.singletonList(purchaseParentOrderPo));

    }
}
