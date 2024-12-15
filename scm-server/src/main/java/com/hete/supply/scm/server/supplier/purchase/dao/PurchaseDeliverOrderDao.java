package com.hete.supply.scm.server.supplier.purchase.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseDeliverExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseWaitDeliverDto;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购发货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Component
@Validated
public class PurchaseDeliverOrderDao extends BaseDao<PurchaseDeliverOrderMapper, PurchaseDeliverOrderPo> {

    public CommonPageResult.PageInfo<PurchaseDeliverVo> getDeliverList(Page<PurchaseDeliverOrderPo> page, PurchaseDeliverListDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getDeliverList(page, dto));
    }

    public PurchaseDeliverOrderPo getOneByNo(String purchaseDeliverOrderNo) {
        if (StringUtils.isBlank(purchaseDeliverOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .eq(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNo));
    }

    public List<PurchaseDeliverOrderPo> getListByChildOrderNoList(List<String> childOrderNoList) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .in(PurchaseDeliverOrderPo::getPurchaseChildOrderNo, childOrderNoList)
                .orderByDesc(PurchaseDeliverOrderPo::getCreateTime));
    }

    public List<PurchaseDeliverOrderPo> getListByNoList(List<String> deliverOrderNoList) {
        if (CollectionUtils.isEmpty(deliverOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .in(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, deliverOrderNoList));
    }

    public List<PurchaseDeliverOrderPo> getListByChildOrderNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .eq(PurchaseDeliverOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .ne(PurchaseDeliverOrderPo::getDeliverOrderStatus, DeliverOrderStatus.DELETED));
    }


    public List<PurchaseDeliverOrderPo> getListByChildOrderNoWithDel(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .eq(PurchaseDeliverOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public Integer getExportTotals(PurchaseDeliverListDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseDeliverExportVo> getExportList(Page<Void> page, PurchaseDeliverListDto dto) {
        final IPage<PurchaseDeliverExportVo> exportList = baseMapper.getExportList(page, dto);
        exportList.getRecords().forEach(record -> record.setDeliverOrderStatus(DeliverOrderStatus.valueOf(record.getDeliverOrderStatus()).getRemark()));
        return PageInfoUtil.getPageInfo(exportList);
    }

    public Map<String, PurchaseDeliverOrderPo> getMapByNoList(List<String> deliverOrderNoList) {
        if (CollectionUtils.isEmpty(deliverOrderNoList)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                        .in(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, deliverOrderNoList))
                .stream()
                .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, Function.identity()));
    }

    public IPage<PurchaseDeliverOrderPo> waitDeliverOrderList(Page<PurchaseDeliverOrderPo> page, PurchaseWaitDeliverDto dto, List<String> purchaseDeliverNoList) {

        return baseMapper.selectPage(page, Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .eq(PurchaseDeliverOrderPo::getDeliverOrderStatus, DeliverOrderStatus.WAIT_DELIVER)
                .in(CollectionUtils.isNotEmpty(purchaseDeliverNoList), PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverNoList)
                .in(CollectionUtils.isNotEmpty(dto.getAuthSupplierCode()), PurchaseDeliverOrderPo::getSupplierCode, dto.getAuthSupplierCode())
                .in(CollectionUtils.isNotEmpty(dto.getPurchaseChildOrderNoList()), PurchaseDeliverOrderPo::getPurchaseChildOrderNo, dto.getPurchaseChildOrderNoList())
                .like(StringUtils.isNotBlank(dto.getPurchaseParentOrderNo()), PurchaseDeliverOrderPo::getPurchaseChildOrderNo, dto.getPurchaseParentOrderNo())
                .in(CollectionUtils.isNotEmpty(dto.getPurchaseDeliverOrderNoList()), PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, dto.getPurchaseDeliverOrderNoList())
                .in(CollectionUtils.isNotEmpty(dto.getSupplierCodeList()), PurchaseDeliverOrderPo::getSupplierCode, dto.getSupplierCodeList())
                .in(CollectionUtils.isNotEmpty(dto.getSupplierNameList()), PurchaseDeliverOrderPo::getSupplierName, dto.getSupplierNameList())
                .eq(PurchaseDeliverOrderPo::getIsDirectSend, dto.getIsDirectSend())
                .ge(null != dto.getDeliverDateStart(), PurchaseDeliverOrderPo::getDeliverDate, dto.getDeliverDateStart())
                .le(null != dto.getDeliverDateEnd(), PurchaseDeliverOrderPo::getDeliverDate, dto.getDeliverDateEnd())
                .eq(StringUtils.isNotBlank(dto.getWarehouseCode()), PurchaseDeliverOrderPo::getWarehouseCode, dto.getWarehouseCode())
                .like(StringUtils.isNotBlank(dto.getWarehouseName()), PurchaseDeliverOrderPo::getWarehouseName, dto.getWarehouseName())
                .eq(null != dto.getHasShippingMark(), PurchaseDeliverOrderPo::getHasShippingMark, dto.getHasShippingMark())
                .orderByDesc(PurchaseDeliverOrderPo::getCreateTime));
    }

    public List<PurchaseDeliverOrderPo> getListByTrackingNo(String trackingNo) {
        if (StringUtils.isBlank(trackingNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .eq(PurchaseDeliverOrderPo::getTrackingNo, trackingNo));
    }

    /**
     * 通过采购子单单号批量查询并过滤已作废
     *
     * @author ChenWenLong
     * @date 2023/4/7 16:40
     */
    public List<PurchaseDeliverOrderPo> getListByChildOrderNoListNotStatus(List<String> childOrderNoList, DeliverOrderStatus notDeliverOrderStatus) {
        if (CollectionUtils.isEmpty(childOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .in(PurchaseDeliverOrderPo::getPurchaseChildOrderNo, childOrderNoList)
                .ne(PurchaseDeliverOrderPo::getDeliverOrderStatus, notDeliverOrderStatus)
                .orderByDesc(PurchaseDeliverOrderPo::getCreateTime));
    }

    public List<PurchaseDeliverOrderPo> getListByStatus(@NotEmpty List<DeliverOrderStatus> deliverOrderStatusList,
                                                        List<String> supplierCodeList,
                                                        LocalDateTime warehousingTimeStart,
                                                        LocalDateTime warehousingTimeEnd,
                                                        String purchaseDeliverOrderNo) {
        if (CollectionUtils.isEmpty(deliverOrderStatusList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .like(StringUtils.isNotBlank(purchaseDeliverOrderNo), PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNo)
                .in(PurchaseDeliverOrderPo::getDeliverOrderStatus, deliverOrderStatusList)
                .in(CollectionUtils.isNotEmpty(supplierCodeList), PurchaseDeliverOrderPo::getSupplierCode, supplierCodeList)
                .ge(null != warehousingTimeStart, PurchaseDeliverOrderPo::getWarehousingTime, warehousingTimeStart)
                .le(null != warehousingTimeEnd, PurchaseDeliverOrderPo::getWarehousingTime, warehousingTimeEnd));
    }

    public List<PurchaseDeliverOrderPo> getListByDeliverOrderNoListNotStatus(List<String> purchaseDeliverOrderNoList, DeliverOrderStatus notDeliverOrderStatus) {
        if (CollectionUtils.isEmpty(purchaseDeliverOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .in(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNoList)
                .ne(PurchaseDeliverOrderPo::getDeliverOrderStatus, notDeliverOrderStatus)
                .orderByDesc(PurchaseDeliverOrderPo::getCreateTime));
    }

    public void updateBatchPurchaseDeliverOrderNo(List<String> purchaseDeliverOrderNos, DeliverOrderStatus deliverOrderStatus) {
        if (CollectionUtil.isNotEmpty(purchaseDeliverOrderNos)) {
            final PurchaseDeliverOrderPo purchaseDeliverOrderPo = new PurchaseDeliverOrderPo();
            purchaseDeliverOrderPo.setDeliverOrderStatus(deliverOrderStatus);
            baseMapper.update(purchaseDeliverOrderPo, Wrappers.<PurchaseDeliverOrderPo>lambdaUpdate()
                    .in(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNos));

        }
    }

    public List<PurchaseDeliverOrderPo> getListByReceiveOrderNo(String receiveOrderNo) {
        if (StringUtils.isBlank(receiveOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .eq(PurchaseDeliverOrderPo::getPurchaseReceiptOrderNo, receiveOrderNo));
    }

    public List<PurchaseDeliverOrderPo> getListByChildOrderNoListNotStatusList(List<String> purchaseChildOrderNoList,
                                                                               List<DeliverOrderStatus> deliverOrderStatusList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .in(PurchaseDeliverOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNoList)
                .notIn(PurchaseDeliverOrderPo::getDeliverOrderStatus, deliverOrderStatusList));
    }

    /**
     * 通过发货单模糊查询
     *
     * @param deliverOrderNo:
     * @return List<PurchaseDeliverOrderPo>
     * @author ChenWenLong
     * @date 2023/7/19 10:23
     */
    public List<PurchaseDeliverOrderPo> getListByLikeDeliverOrderNo(String deliverOrderNo) {
        if (StringUtils.isBlank(deliverOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .like(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, deliverOrderNo));
    }

    /**
     * 通过供应商产品名称多组搜索条件获取信息
     *
     * @param list:
     * @return List<PurchaseDeliverOrderPo>
     * @author ChenWenLong
     * @date 2023/7/19 15:01
     */
    public List<PurchaseDeliverOrderPo> getListBySupplierProduct(List<SupplierProductComparePo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return baseMapper.getListBySupplierProduct(list);
    }

    public List<PurchaseDeliverOrderPo> getListByNoListAndStatus(List<String> purchaseDeliverOrderNoList,
                                                                 List<DeliverOrderStatus> deliverOrderStatusList,
                                                                 List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(purchaseDeliverOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .in(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, purchaseDeliverOrderNoList)
                .in(CollectionUtils.isNotEmpty(deliverOrderStatusList), PurchaseDeliverOrderPo::getDeliverOrderStatus, deliverOrderStatusList)
                .in(CollectionUtils.isNotEmpty(supplierCodeList), PurchaseDeliverOrderPo::getSupplierCode, supplierCodeList));
    }

    /**
     * 通过仓库编码查询
     *
     * @param warehouseCode:
     * @return List<PurchaseDeliverOrderPo>
     * @author ChenWenLong
     * @date 2023/12/26 10:32
     */
    public List<PurchaseDeliverOrderPo> getListByWarehouseCode(String warehouseCode) {
        if (StringUtils.isBlank(warehouseCode)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseDeliverOrderPo>lambdaQuery()
                .eq(PurchaseDeliverOrderPo::getWarehouseCode, warehouseCode));
    }

    public BigDecimal getMonthGoodsPayment(LocalDateTime startTime, LocalDateTime endTime, DeliverOrderStatus deliverOrderStatus, String supplierCode) {
        return baseMapper.getMonthGoodsPayment(startTime, endTime, deliverOrderStatus, supplierCode);
    }

    public BigDecimal getInTransitMoney(List<DeliverOrderStatus> deliverOrderStatusList, String supplierCode) {
        return baseMapper.getInTransitMoney(deliverOrderStatusList, supplierCode);
    }
}
