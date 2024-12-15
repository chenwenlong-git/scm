package com.hete.supply.scm.server.supplier.purchase.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseDeliverListDto;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseDeliverExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 采购发货单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Mapper
interface PurchaseDeliverOrderMapper extends BaseDataMapper<PurchaseDeliverOrderPo> {

    Integer getExportTotals(@Param("dto") PurchaseDeliverListDto dto);

    IPage<PurchaseDeliverExportVo> getExportList(Page<Void> page, @Param("dto") PurchaseDeliverListDto dto);

    List<PurchaseDeliverOrderPo> getListBySupplierProduct(@Param("list") List<SupplierProductComparePo> list);

    BigDecimal getMonthGoodsPayment(@Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("deliverOrderStatus") DeliverOrderStatus deliverOrderStatus,
                                    @Param("supplierCode") String supplierCode);

    BigDecimal getInTransitMoney(@Param("deliverOrderStatusList") List<DeliverOrderStatus> deliverOrderStatusList,
                                 @Param("supplierCode") String supplierCode);

    IPage<PurchaseDeliverVo> getDeliverList(Page<PurchaseDeliverOrderPo> page,
                                            @Param("dto") PurchaseDeliverListDto dto);
}
