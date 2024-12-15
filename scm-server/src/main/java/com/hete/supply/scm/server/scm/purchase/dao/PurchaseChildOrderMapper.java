package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseProductSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.*;
import com.hete.supply.scm.server.scm.entity.vo.PurchaseCheckSupplierCodeVo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseChildOrderExportBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseLatestPriceItemBo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDefaultPriceItemDto;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseSkuAndSupplierItemDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.*;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 采购需求子单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Mapper
interface PurchaseChildOrderMapper extends BaseDataMapper<PurchaseChildOrderPo> {

    IPage<PurchaseProductSearchVo> searchProductPurchase(Page<Void> page, @Param("dto") PurchaseProductSearchDto dto);

    List<PurchaseChildOrderChangeSearchVo> purchaseChildOrderChangeSearch(List<String> supplierCodeList, List<PurchaseBizType> purchaseBizTypeList,
                                                                          LocalDateTime warehousingTime, LocalDateTime warehousingTimeStart,
                                                                          LocalDateTime warehousingTimeEnd, PurchaseOrderStatus purchaseOrderStatus);

    Integer getChildExportTotals(@Param("dto") PurchaseProductSearchDto dto);

    IPage<PurchaseChildExportVo> getChildExportList(Page<Void> page, @Param("dto") PurchaseProductSearchDto dto);

    Integer getSkuParentExportTotals(@Param("dto") PurchaseProductSearchDto dto);

    IPage<PurchaseChildSkuExportVo> getSkuChildExportList(Page<Void> page, @Param("dto") PurchaseProductSearchDto dto);

    Integer getRawParentExportTotals(@Param("dto") PurchaseProductSearchDto dto);

    IPage<PurchaseChildRawExportVo> getRawChildExportList(Page<Void> page, @Param("dto") PurchaseProductSearchDto dto);

    List<PurchaseChildOrderVo> getPurchaseChildListIgnoreDelete(@Param("purchaseParentOrderNo") String purchaseParentOrderNo,
                                                                @Param("purchaseOrderStatusList") List<PurchaseOrderStatus> purchaseOrderStatusList,
                                                                @Param("authSupplierCode") List<String> authSupplierCode);

    List<PurchaseChildOrderExportBo> getItemBatchPurchaseChildOrderNo(@Param("purchaseChildOrderNoList") List<String> purchaseChildOrderNoList);

    List<PurchaseSkuPriceVo> getPurchasePriceBySkuAndSupplier(@Param("purchaseSkuAndSupplierItemList") List<PurchaseSkuAndSupplierItemDto> purchaseSkuAndSupplierItemList,
                                                              @Param("notInStatusList") List<PurchaseOrderStatus> notInStatusList,
                                                              @Param("purchaseBizType") PurchaseBizType purchaseBizType,
                                                              @Param("defaultTime") LocalDateTime defaultTime);

    List<PurchaseChildOrderPo> getListBySupplierProduct(@Param("list") List<SupplierProductComparePo> list);

    List<PurchaseCheckSupplierCodeVo> getListByLastFewDays(@Param("days") Integer days);

    List<PurchaseChildOrderPo> getSplitPurchase();

    List<SkuUndeliveredCntVo> getPurchaseUndeliveredCntBySku(@Param("skuList") List<String> skuList,
                                                             @Param("purchaseOrderStatus") PurchaseOrderStatus purchaseOrderStatus);

    List<PurchaseChildOrderPo> getCanFinishPurchase();

    List<PurchaseDefaultPriceItemVo> getDefaultPriceByNoList(@Param("dtoList") List<PurchaseDefaultPriceItemDto> purchaseDefaultPriceItemList);

    List<PurchaseLatestPriceItemBo> getPurchasePriceBySkuList(@Param("skuList") List<String> skuList);

    Integer getRawSkuChildExportTotals(@Param("dto") PurchaseProductSearchDto dto);

    IPage<PurchaseChildSkuRawExportVo> getRawSkuChildExportList(Page<Void> page, @Param("dto") PurchaseProductSearchDto dto);

}
