package com.hete.supply.scm.server.supplier.purchase.dao;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 采购发货单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Mapper
interface PurchaseDeliverOrderItemMapper extends BaseDataMapper<PurchaseDeliverOrderItemPo> {

    /**
     * 根据采购母单号获取发货总数
     *
     * @param purchaseParentOrderNo
     * @param deliverOrderStatusList
     * @return
     */
    int getDeliverCntByPurchaseParentNo(@Param("purchaseParentOrderNo") String purchaseParentOrderNo,
                                        @Param("deliverOrderStatusList") List<DeliverOrderStatus> deliverOrderStatusList);
}
