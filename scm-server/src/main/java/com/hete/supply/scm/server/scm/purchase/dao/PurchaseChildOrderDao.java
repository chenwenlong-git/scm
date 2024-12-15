package com.hete.supply.scm.server.scm.purchase.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseProductSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.supply.scm.server.scm.entity.vo.PurchaseCheckSupplierCodeVo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseChildOrderExportBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseLatestPriceItemBo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDefaultPriceItemDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseSkuAndSupplierItemDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.*;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购需求子单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Component
@Validated
public class PurchaseChildOrderDao extends BaseDao<PurchaseChildOrderMapper, PurchaseChildOrderPo> {

    public List<PurchaseChildOrderPo> getListByParentNo(String purchaseParentOrderNo) {
        if (StringUtils.isBlank(purchaseParentOrderNo)) {
            return new ArrayList<>();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNo));
    }

    public List<PurchaseChildOrderVo> getListByParentNoAndStatus(String purchaseParentOrderNo,
                                                                 List<PurchaseOrderStatus> purchaseOrderStatusList,
                                                                 List<String> authSupplierCode) {
        if (StringUtils.isBlank(purchaseParentOrderNo)) {
            return Collections.emptyList();
        }

        return baseMapper.getPurchaseChildListIgnoreDelete(purchaseParentOrderNo, purchaseOrderStatusList, authSupplierCode);
    }

    public PurchaseChildOrderPo getLatestPurchaseChild(@NotBlank String purchaseParentOrderNo) {
        return baseMapper.selectOneIgnoreDelete(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNo)
                .orderByDesc(PurchaseChildOrderPo::getCreateTime)
                .orderByDesc(PurchaseChildOrderPo::getPurchaseChildOrderId)
                .last("limit 1"));
    }

    /**
     * 通过模糊子单号查询
     *
     * @author ChenWenLong
     * @date 2022/11/9 10:11
     */
    public List<PurchaseChildOrderPo> getListLikeByChildNo(String purchaseChildOrderNo) {
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .like(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    /**
     * 通过模糊子单号和类型查询
     *
     * @author ChenWenLong
     * @date 2022/11/9 10:11
     */
    public List<PurchaseChildOrderPo> getListLikeByChildNoAndType(String purchaseChildOrderNo,
                                                                  PurchaseBizType purchaseBizType,
                                                                  String supplierCode,
                                                                  List<PurchaseOrderStatus> purchaseOrderStatusNotList) {
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .like(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderPo::getPurchaseBizType, purchaseBizType)
                .eq(PurchaseChildOrderPo::getSupplierCode, supplierCode)
                .notIn(PurchaseChildOrderPo::getPurchaseOrderStatus, purchaseOrderStatusNotList));
    }

    public List<PurchaseChildOrderPo> getByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new ArrayList<>();
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseChildOrderId, idList));
    }

    public PurchaseChildOrderPo getOneByChildOrderNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return null;
        }

        return baseMapper.selectOne(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo));
    }

    public CommonPageResult.PageInfo<PurchaseProductSearchVo> searchProductPurchase(Page<Void> page, PurchaseProductSearchDto dto) {
        final IPage<PurchaseProductSearchVo> pageResult = baseMapper.searchProductPurchase(page, dto);

        return PageInfoUtil.getPageInfo(pageResult);
    }

    public PurchaseChildOrderPo getOneByChildOrderNoAndType(String purchaseChildOrderNo, PurchaseBizType purchaseBizType,
                                                            List<String> authSupplierCode) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(null != purchaseBizType, PurchaseChildOrderPo::getPurchaseBizType, purchaseBizType)
                .in(CollectionUtils.isNotEmpty(authSupplierCode), PurchaseChildOrderPo::getSupplierCode, authSupplierCode));
    }

    /**
     * 通过供应商和时间查询数据列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 10:38
     */
    public List<PurchaseChildOrderChangeSearchVo> purchaseChildOrderChangeSearch(List<String> supplierCodeList, List<PurchaseBizType> purchaseBizTypeList,
                                                                                 LocalDateTime warehousingTime, LocalDateTime warehousingTimeStart,
                                                                                 LocalDateTime warehousingTimeEnd, PurchaseOrderStatus purchaseOrderStatus) {
        return baseMapper.purchaseChildOrderChangeSearch(supplierCodeList, purchaseBizTypeList,
                warehousingTime, warehousingTimeStart, warehousingTimeEnd, purchaseOrderStatus);
    }

    public List<PurchaseChildOrderPo> getListByNoList(List<String> purchaseChildOrderNoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNoList));
    }

    public void removeByParentOrderNo(@NotEmpty List<String> purchaseParentOrderNoList) {
        baseMapper.deleteSkipCheck(Wrappers.<PurchaseChildOrderPo>lambdaUpdate()
                .in(PurchaseChildOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNoList));
    }

    public Integer getChildExportTotals(PurchaseProductSearchDto dto) {
        return baseMapper.getChildExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseChildExportVo> getChildExportList(Page<Void> page, PurchaseProductSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getChildExportList(page, dto));

    }

    public Integer getSkuChildExportTotals(PurchaseProductSearchDto dto) {
        return baseMapper.getSkuParentExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseChildSkuExportVo> getSkuChildExportList(Page<Void> page, PurchaseProductSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getSkuChildExportList(page, dto));

    }

    public Integer getRawChildExportTotals(PurchaseProductSearchDto dto) {
        return baseMapper.getRawParentExportTotals(dto);
    }


    public CommonPageResult.PageInfo<PurchaseChildRawExportVo> getRawChildExportList(Page<Void> page, PurchaseProductSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getRawChildExportList(page, dto));
    }

    /**
     * 根据编号批量修改状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 16:20
     */
    public void updateBatchPurchaseChildOrderNo(List<String> purchaseChildOrderNos, PurchaseOrderStatus purchaseOrderStatus) {
        if (CollectionUtil.isNotEmpty(purchaseChildOrderNos)) {
            final PurchaseChildOrderPo purchaseChildOrderPo = new PurchaseChildOrderPo();
            purchaseChildOrderPo.setPurchaseOrderStatus(purchaseOrderStatus);
            baseMapper.update(purchaseChildOrderPo, Wrappers.<PurchaseChildOrderPo>lambdaUpdate()
                    .in(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNos));
        }
    }

    public List<PurchaseChildOrderPo> getListByParentNoAndSupplier(String supplierCode, String purchaseParentOrderNo,
                                                                   List<PurchaseOrderStatus> supplierPurchaseStatusList) {
        if (StringUtils.isBlank(purchaseParentOrderNo)) {
            return Collections.emptyList();
        }
        if (StringUtils.isBlank(supplierCode)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNo)
                .eq(PurchaseChildOrderPo::getSupplierCode, supplierCode)
                .in(PurchaseChildOrderPo::getPurchaseOrderStatus, supplierPurchaseStatusList));
    }

    public List<PurchaseChildOrderPo> getListByStatusList(List<PurchaseOrderStatus> statusList) {
        if (CollectionUtil.isEmpty(statusList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseOrderStatus, statusList));
    }


    public List<PurchaseChildOrderPo> getListByStatus(PurchaseOrderStatus purchaseOrderStatus) {
        if (null == purchaseOrderStatus) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getPurchaseOrderStatus, purchaseOrderStatus));
    }

    /**
     * 通过编号批量查询列表详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 15:10
     */
    public List<PurchaseChildOrderExportBo> getItemBatchPurchaseChildOrderNo(List<String> purchaseChildOrderNoList) {
        if (CollectionUtil.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.getItemBatchPurchaseChildOrderNo(purchaseChildOrderNoList);
    }


    public List<PurchaseChildOrderPo> getListByParentNoList(List<String> purchaseParentOrderNoList) {
        if (CollectionUtil.isEmpty(purchaseParentOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNoList));
    }

    public List<PurchaseChildOrderPo> getListByParentNoAndNeStatus(List<String> purchaseParentOrderNoList,
                                                                   PurchaseOrderStatus purchaseOrderStatus) {
        if (CollectionUtils.isEmpty(purchaseParentOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseParentOrderNo, purchaseParentOrderNoList)
                .ne(PurchaseChildOrderPo::getPurchaseOrderStatus, purchaseOrderStatus));
    }

    public List<PurchaseSkuPriceVo> getPurchasePriceBySkuAndSupplier(List<PurchaseSkuAndSupplierItemDto> purchaseSkuAndSupplierItemList,
                                                                     List<PurchaseOrderStatus> notInStatusList, LocalDateTime defaultTime) {
        return baseMapper.getPurchasePriceBySkuAndSupplier(purchaseSkuAndSupplierItemList, notInStatusList,
                PurchaseBizType.PRODUCT, defaultTime);
    }

    public List<PurchaseChildOrderPo> getListByStatusListAndOverdue(List<PurchaseOrderStatus> workingStatusList) {
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseOrderStatus, workingStatusList)
                .ne(PurchaseChildOrderPo::getIsOverdue, BooleanType.TRUE));
    }

    public List<PurchaseChildOrderPo> getListByIsOverdue(BooleanType isOverdue) {
        if (null == isOverdue) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getIsOverdue, isOverdue));
    }

    /**
     * 通过供应商产品名称多组搜索条件获取信息
     *
     * @param list:
     * @return List<PurchaseChildOrderPo>
     * @author ChenWenLong
     * @date 2023/7/19 15:01
     */
    public List<PurchaseChildOrderPo> getListBySupplierProduct(List<SupplierProductComparePo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return baseMapper.getListBySupplierProduct(list);
    }

    public List<PurchaseChildOrderPo> getListByPurchaseOrderType(PurchaseOrderType purchaseOrderType) {
        if (null == purchaseOrderType) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getPurchaseOrderType, purchaseOrderType));
    }

    public List<PurchaseChildOrderPo> getListByIsUrgentOrder(BooleanType isUrgentOrder) {
        if (null == isUrgentOrder) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .eq(PurchaseChildOrderPo::getIsUrgentOrder, isUrgentOrder));
    }

    public long selectCountByStatusList(List<PurchaseOrderStatus> statusList) {
        if (CollectionUtils.isEmpty(statusList)) {
            return 0;
        }

        return baseMapper.selectCount(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseOrderStatus, statusList));
    }

    public IPage<PurchaseChildOrderPo> getPageByStatus(Page<PurchaseChildOrderPo> page, List<PurchaseOrderStatus> statusList) {
        return baseMapper.selectPage(page, Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseOrderStatus, statusList));
    }

    public List<PurchaseChildOrderPo> getListByNoListAndPurchaseOrderType(String purchaseChildOrderNo,
                                                                          PurchaseOrderType purchaseOrderType) {
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .like(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNo)
                .eq(PurchaseChildOrderPo::getPurchaseOrderType, purchaseOrderType));
    }

    public List<PurchaseChildOrderPo> getListByChildNoListAndType(List<String> purchaseChildOrderNoList,
                                                                  String supplierCode,
                                                                  List<PurchaseOrderStatus> purchaseOrderStatusNotList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNoList)
                .eq(PurchaseChildOrderPo::getSupplierCode, supplierCode)
                .notIn(PurchaseChildOrderPo::getPurchaseOrderStatus, purchaseOrderStatusNotList));
    }

    public List<PurchaseChildOrderPo> getListByNoListAndType(List<String> purchaseChildOrderNoList,
                                                             PurchaseBizType purchaseBizType) {
        if (CollectionUtils.isEmpty(purchaseChildOrderNoList)) {
            return Collections.emptyList();
        }

        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .in(PurchaseChildOrderPo::getPurchaseChildOrderNo, purchaseChildOrderNoList)
                .eq(purchaseBizType != null, PurchaseChildOrderPo::getPurchaseBizType, purchaseBizType));
    }

    public List<PurchaseCheckSupplierCodeVo> getListByLastFewDays(Integer days) {
        return baseMapper.getListByLastFewDays(days);
    }

    public List<PurchaseChildOrderPo> getSplitPurchase() {
        return baseMapper.getSplitPurchase();
    }

    public List<SkuUndeliveredCntVo> getPurchaseUndeliveredCntBySku(List<String> skuList, PurchaseOrderStatus purchaseOrderStatus) {
        return baseMapper.getPurchaseUndeliveredCntBySku(skuList, purchaseOrderStatus);
    }

    public List<PurchaseChildOrderPo> getCanFinishPurchase() {
        return baseMapper.getCanFinishPurchase();
    }

    /**
     * 根据采购子订单号和可创建质检状态查询采购子订单列表。
     *
     * @param purchaseChildOrderNo 采购子订单号，支持模糊查询
     * @param canCreateQcStatus    可创建质检状态列表
     * @return 符合条件的采购子订单列表
     */
    public List<PurchaseChildOrderPo> queryPurchaseChildOrders(String purchaseChildOrderNo,
                                                               List<PurchaseOrderStatus> canCreateQcStatus) {
        return baseMapper.selectList(Wrappers.<PurchaseChildOrderPo>lambdaQuery()
                .like(PurchaseChildOrderPo::getPurchaseChildOrderNo,
                        purchaseChildOrderNo)
                .in(PurchaseChildOrderPo::getPurchaseOrderStatus, canCreateQcStatus));
    }


    public List<PurchaseDefaultPriceItemVo> getDefaultPriceByNoList(List<PurchaseDefaultPriceItemDto> purchaseDefaultPriceItemList) {
        if (CollectionUtils.isEmpty(purchaseDefaultPriceItemList)) {
            return Collections.emptyList();
        }

        return baseMapper.getDefaultPriceByNoList(purchaseDefaultPriceItemList);
    }

    public List<PurchaseLatestPriceItemBo> getPurchasePriceBySkuList(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptyList();
        }
        return baseMapper.getPurchasePriceBySkuList(skuList);
    }

    public Integer getRawSkuChildExportTotals(PurchaseProductSearchDto dto) {
        return baseMapper.getRawSkuChildExportTotals(dto);
    }

    public CommonPageResult.PageInfo<PurchaseChildSkuRawExportVo> getRawSkuChildExportList(Page<Void> page, PurchaseProductSearchDto dto) {
        return PageInfoUtil.getPageInfo(baseMapper.getRawSkuChildExportList(page, dto));

    }
}
